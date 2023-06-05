package io.github.rxcats.redisson.lock

import io.github.rxcats.core.loggerK
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient

@Aspect
class RedissonLockAdvice(
    private val redisson: RedissonClient
) {
    private val log by loggerK

    @Around("@annotation(io.github.rxcats.redisson.lock.RedissonLock)")
    fun process(pjp: ProceedingJoinPoint): Any? {
        val signature = pjp.signature as MethodSignature
        val annotation = signature.method.getAnnotation(RedissonLock::class.java)
        val name = annotation.name.ifBlank { "RedissonLock" }
        val wait = annotation.waitForLockTime
        val duration = Duration.parse(wait)
        var maxRetryCnt = annotation.maxRetry
        val traceId = UUID.randomUUID().toString()

        while (maxRetryCnt > 0) {
            log.info("RedissonLock({}) ({})", traceId, maxRetryCnt)

            --maxRetryCnt

            val lock = redisson.getLock(name)

            try {
                val acquired = lock.tryLock(duration.inWholeMilliseconds, TimeUnit.MILLISECONDS)

                if (acquired) {
                    try {
                        return pjp.proceed()
                    } finally {
                        log.info("RedissonLock({}) unlock", traceId)
                        lock.unlock()
                    }
                }
            } catch (e: InterruptedException) {
                log.error("RedissonLock({}) interrupted", traceId)
            }
        }

        log.error("RedissonLock({}) failure", traceId)
        throw IllegalStateException("try lock failure ({$traceId})")
    }
}
