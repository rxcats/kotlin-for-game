package io.github.rxcats.database.aop

import io.github.rxcats.core.loggerK
import io.github.rxcats.database.component.ShardKeyHelper
import io.github.rxcats.database.component.transaction
import io.github.rxcats.database.config.RoutingDataSourceContextHolder
import io.github.rxcats.database.type.DbType
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.transaction.PlatformTransactionManager
import java.lang.reflect.Parameter

@Aspect
class TargetDatabaseAdvice(
    private val platformTransactionManager: PlatformTransactionManager,
    private val shardKeyHelper: ShardKeyHelper
) {

    private val log by loggerK

    @Around("@annotation(io.github.rxcats.datasourceroute.aop.TargetDatabase)")
    fun process(pjp: ProceedingJoinPoint): Any? {
        val signature = pjp.signature as MethodSignature
        val annotation = signature.method.getAnnotation(TargetDatabase::class.java)
        val useTransaction = annotation.useTransaction
        val readOnly = annotation.readOnly

        log.info("signature={}, db={}, useTransaction={}", signature, annotation.db, useTransaction)

        try {
            if (annotation.db.sharded) {
                val (hashKey, shardNo) = parseShardKeyParameters(signature.method.parameters, pjp.args)
                log.info("hashKey={}, shardNo={}", hashKey, shardNo)

                if (hashKey == null && shardNo == null) {
                    throw IllegalStateException("Parameter annotation ShardHashKey or ShardNo required")
                }

                // ShardHashKey(1), ShardNo(2) 순서로 적용 되도록 유도
                if (hashKey != null) {
                    if (hashKey.isBlank()) {
                        throw IllegalStateException("Parameter ShardHashKey required")
                    }

                    val shardNoByHashKey = calculateShardHashKey(annotation.db, hashKey)

                    log.info("ShardHashKey={}", shardNoByHashKey)

                    RoutingDataSourceContextHolder.set(annotation.db.get(shardNoByHashKey))
                } else {
                    if (shardNo == null) {
                        throw IllegalStateException("Parameter ShardNo required")
                    }

                    if (shardNo <= 0) {
                        throw IllegalStateException("Parameter ShardNo must be greater than zero")
                    }

                    log.info("ShardNo={}", shardNo)

                    RoutingDataSourceContextHolder.set(annotation.db.get(shardNo))
                }
            } else {
                RoutingDataSourceContextHolder.set(annotation.db.get())
            }

            return withTransaction(useTransaction, readOnly) { pjp.proceed() }
        } catch (t: Throwable) {
            throw t
        } finally {
            RoutingDataSourceContextHolder.clear()
        }
    }

    private fun parseShardKeyParameters(parameters: Array<Parameter>, args: Array<Any?>): Pair<String?, Int?> {
        var hashKey: String? = null
        var shardNo: Int? = null

        for ((idx, param) in parameters.withIndex()) {
            if (param.annotations.firstOrNull { it is ShardHashKey } != null) {
                hashKey = args[idx] as? String
            }

            if (param.annotations.firstOrNull { it is ShardNo } != null) {
                shardNo = args[idx] as? Int
            }
        }

        return Pair(hashKey, shardNo)
    }

    private fun calculateShardHashKey(dbType: DbType, shardHashKey: String): Int {
        return shardKeyHelper.shardNo(dbType, shardHashKey)
    }

    private inline fun withTransaction(useTransaction: Boolean, readOnly: Boolean, block: () -> Any?): Any? {
        return if (useTransaction) platformTransactionManager.transaction(readOnly, block) else block()
    }
}
