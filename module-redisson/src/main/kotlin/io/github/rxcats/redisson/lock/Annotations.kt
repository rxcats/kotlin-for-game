package io.github.rxcats.redisson.lock

/**
 * waitForLockTime : default 5s (5 seconds)
 *
 * i.e) 1ms -> 1 milliseconds
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RedissonLock(
    val name: String = "RedissonLock",
    val waitForLockTime: String = "5s",
    val maxRetry: Int = 3
)
