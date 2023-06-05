package io.github.rxcats.redisson

import io.github.rxcats.core.loggerK
import io.github.rxcats.redisson.lock.RedissonLock
import org.junit.jupiter.api.Test
import org.redisson.api.RedissonClient
import org.redisson.api.listener.StatusListener
import org.redisson.client.codec.LongCodec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Service
import org.springframework.test.context.TestConstructor
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.IntStream

@SpringBootTest(classes = [RedissonConfig::class, RedissonLockAdviceTest.LockService::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RedissonLockAdviceTest(
    private val service: LockService,
    private val redisson: RedissonClient
) {
    private val log by loggerK

    private val lockChannel = "redisson_lock__channel:{LockTest}"

    @Service
    class LockService {
        private val log by loggerK

        @RedissonLock(name = "LockTest", waitForLockTime = "5s", maxRetry = 3)
        fun runWithLock() {
            Thread.sleep(50)
            log.info("runWithLock")
        }

    }

    class RedissonMessageListener : StatusListener {
        private val log by loggerK

        override fun onSubscribe(channel: String?) {
            log.info("onSubscribe {}", channel)
        }

        override fun onUnsubscribe(channel: String?) {
            log.info("onUnsubscribe {}", channel)
        }

    }

    @Test
    fun lockTest() {
        val topic = redisson.getTopic(lockChannel, LongCodec.INSTANCE)
        val listener = RedissonMessageListener()
        topic.addListener(listener)

        val startTime = System.currentTimeMillis()
        val succeededCount = AtomicInteger()
        val failureCount = AtomicInteger()

        IntStream.rangeClosed(1, 100).parallel().forEach {
            try {
                service.runWithLock()
                succeededCount.incrementAndGet()
            } catch (e: Exception) {
                failureCount.incrementAndGet()
                e.printStackTrace()
            }
        }

        log.info("succeeded: {}, failure: {}, time: {}", succeededCount, failureCount, System.currentTimeMillis() - startTime)
    }
}
