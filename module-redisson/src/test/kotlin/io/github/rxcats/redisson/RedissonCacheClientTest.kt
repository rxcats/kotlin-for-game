package io.github.rxcats.redisson

import io.github.rxcats.core.loggerK
import io.github.rxcats.redisson.cache.RedissonCacheClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.time.Instant
import kotlin.time.Duration.Companion.seconds

@SpringBootTest(classes = [RedissonConfig::class, RedissonCacheClient::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RedissonCacheClientTest(
    private val cacheClient: RedissonCacheClient
) {
    private val log by loggerK

    data class SomeObject(
        val id: Long = 0,
        val name: String = "",
        val attr1: String? = null,
        val date: Instant? = null,
        val now: Instant? = Instant.now()
    )

    @Test
    fun setWithTtl() {
        val key = "test:1"

        cacheClient.set(key, SomeObject(), 1.seconds)

        val after = cacheClient.get<SomeObject>(key)
        log.info("{}", after)
        assertThat(after).isNotNull()

        assertThat(after?.attr1).isNull()
        assertThat(after?.date).isNull()

        cacheClient.delete(key)

        val deleted = cacheClient.get<SomeObject>(key)
        assertThat(deleted).isNull()
    }

    @Test
    fun withCache() {
        val key = "test:2"

        val after = cacheClient.withCache(key, 1.seconds) {
            SomeObject()
        }

        log.info("{}", after)
        assertThat(after).isNotNull()

        cacheClient.delete(key)

        val deleted = cacheClient.get<SomeObject>(key)
        assertThat(deleted).isNull()
    }

    @Test
    fun doesNotExistsKeyRemainTimeToLive() {
        val key = "test:3"

        cacheClient.delete(key)

        val doesNotExistsRemain = cacheClient.remainTimeToLive(key)
        assertThat(doesNotExistsRemain).isEqualTo(-2)
    }

    @Test
    fun remainTimeToLive() {
        val key = "test:3"

        cacheClient.delete(key)

        cacheClient.set(key, SomeObject(), 1.seconds)

        val remain = cacheClient.remainTimeToLive(key)
        assertThat(remain).isLessThanOrEqualTo(1000)
    }

    @Test
    fun noAssociatedExpireRemainTimeToLive() {
        val key = "test:3"

        cacheClient.delete(key)

        cacheClient.set(key, SomeObject())

        val remain = cacheClient.remainTimeToLive(key)
        assertThat(remain).isEqualTo(-1)
    }

}
