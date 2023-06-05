package io.github.rxcats.redisson

import io.github.rxcats.core.loggerK
import io.github.rxcats.redisson.lock.RedissonLockClient
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode

@SpringBootTest(classes = [RedissonConfig::class, RedissonLockClient::class])
@TestConstructor(autowireMode = AutowireMode.ALL)
class RedissonLockClientTest(
    private val lockClient: RedissonLockClient
) {

    private val log by loggerK

    @Test
    fun lockTest() {
        lockClient.withLock {
            Thread.sleep(50)
            log.info("runWithLock")
        }
    }

}
