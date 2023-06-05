package io.github.rxcats.redisson.lock

import io.github.rxcats.core.loggerK
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component

@Component
class RedissonLockClient(
    val redisson: RedissonClient
) {
    val log by loggerK

    final inline fun <T> withLock(
        name: String = "RedissonLock",
        maxRetry: Int = 3,
        waitForLockTime: Duration = 5.seconds,
        action: () -> T
    ): Result<T> {

        val traceId = UUID.randomUUID().toString()

        var maxRetryCnt = maxRetry

        while (maxRetry > 0) {
            log.info("RedissonLock({}) ({})", traceId, maxRetryCnt)

            --maxRetryCnt

            val lock = redisson.getLock(name)

            try {
                val acquired = lock.tryLock(waitForLockTime.inWholeMilliseconds, TimeUnit.MILLISECONDS)

                if (acquired) {
                    try {
                        return Result.success(action())
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

        return Result.failure(IllegalStateException("try lock failure ({$traceId})"))
    }
}
