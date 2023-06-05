package io.github.rxcats.redisson

import io.github.rxcats.redisson.lock.RedissonLockAdvice
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.api.RedissonReactiveClient
import org.redisson.codec.Kryo5Codec
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableAutoConfiguration
@Configuration(proxyBeanMethods = false)
class RedissonConfig {
    @ConfigurationProperties(prefix = "app.cache.redis")
    @Bean("cacheRedisProperties")
    fun cacheRedisProperties(): RedisProperties = RedisProperties()

    @Bean
    fun redissonDefaultConfig(@Qualifier("cacheRedisProperties") cacheRedisProperties: RedisProperties): Config {
        val config = Config()
        config.codec = Kryo5Codec()
        val ssc = config.useSingleServer()
        ssc.address = cacheRedisProperties.url
        ssc.connectTimeout = 5000
        ssc.database = cacheRedisProperties.database
        ssc.password = cacheRedisProperties.password
        ssc.clientName = "redissonClient"
        return config
    }

    @Bean(destroyMethod = "shutdown")
    fun redissonClient(redissonDefaultConfig: Config): RedissonClient {
        return Redisson.create(redissonDefaultConfig)
    }

    @Bean(destroyMethod = "shutdown")
    fun redissonReactiveClient(
        redissonDefaultConfig: Config
    ): RedissonReactiveClient {
        return Redisson.create(redissonDefaultConfig).reactive()
    }

    @ConditionalOnMissingBean
    @Bean
    fun redissonLockAdvice(redissonClient: RedissonClient): RedissonLockAdvice {
        return RedissonLockAdvice(redissonClient)
    }

}
