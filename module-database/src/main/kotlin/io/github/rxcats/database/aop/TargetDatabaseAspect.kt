package io.github.rxcats.database.aop

import io.github.rxcats.core.loggerK
import io.github.rxcats.database.component.RoutingQuery
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature

@Aspect
class TargetDatabaseAspect {
    private val log by loggerK

    @Around("@annotation(io.github.rxcats.database.aop.TargetDatabase)")
    fun process(pjp: ProceedingJoinPoint): Any? {
        val signature = pjp.signature as MethodSignature
        val annotation = signature.method.getAnnotation(TargetDatabase::class.java)
        val useTransaction = annotation.useTransaction
        val db = annotation.db

        log.info("signature={}, db={}, useTransaction={}", signature, db, useTransaction)

        return RoutingQuery(db = db, useTransaction = useTransaction) {
            pjp.proceed()
        }
    }
}
