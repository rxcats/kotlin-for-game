package io.github.rxcats.redisson.cache

import com.fasterxml.jackson.module.kotlin.readValue
import io.github.rxcats.core.Json
import io.github.rxcats.redisson.entity.CacheResult
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component

@Component
class RedissonCacheClient(
    val redisson: RedissonClient
) {

    final inline fun <reified T : Any> get(key: String): CacheResult<T> {
        val bucket = redisson.getBucket<String>(key)
        val data = bucket.get() ?: return CacheResult.of()
        return CacheResult.of(Json.readValue<T>(data))
    }

    final fun set(key: String, value: Any, ttl: Duration = Duration.ZERO) {
        val bucket = redisson.getBucket<String>(key)
        val data = Json.writeValueAsString(value)
        if (ttl.inWholeMilliseconds <= 0) {
            bucket.set(data)
        } else {
            bucket.set(data, ttl.inWholeMilliseconds, TimeUnit.MILLISECONDS)
        }
    }

    final fun delete(key: String) {
        val bucket = redisson.getBucket<String>(key)
        bucket.delete()
    }

    final inline fun <reified T : Any> withCache(key: String, ttl: Duration, action: () -> T?): CacheResult<T> {
        var result = get<T>(key)

        if (result.isNull) {
            result = CacheResult.of(action())
        }

        if (result.isNull) return result

        set(key, result.getOrThrow(), ttl)

        return result
    }

    final fun remainTimeToLive(key: String): Long {
        val bucket = redisson.getBucket<String>(key)
        return bucket.remainTimeToLive()
    }

}
