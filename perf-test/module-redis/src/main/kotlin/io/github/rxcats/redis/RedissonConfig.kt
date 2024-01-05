package io.github.rxcats.redis

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.api.RedissonReactiveClient
import org.redisson.codec.Kryo5Codec
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class RedissonConfig {
    @Bean
    fun redissonDefaultConfig(): Config {
        val config = Config()
        config.codec = Kryo5Codec()
        config.threads = 0
        config.nettyThreads = 0
        val ssc = config.useSingleServer()
        ssc.address = "redis://localhost:6379"
        ssc.connectTimeout = 5000
        ssc.database = 0
        ssc.password = null
        ssc.clientName = "redissonClient"
        ssc.connectionPoolSize = 8
        ssc.connectionMinimumIdleSize = 1
        ssc.subscriptionConnectionPoolSize = 1
        ssc.subscriptionConnectionMinimumIdleSize = 1
        return config
    }

    @Bean(destroyMethod = "shutdown")
    fun redissonClient(redissonDefaultConfig: Config): RedissonClient {
        return Redisson.create(redissonDefaultConfig)
    }

    @Bean(destroyMethod = "shutdown")
    fun redissonReactiveClient(
        redissonClient: RedissonClient
    ): RedissonReactiveClient {
        return redissonClient.reactive()
    }
}
