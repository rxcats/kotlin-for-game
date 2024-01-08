package org.example

import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.enterprise.event.Startup
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.api.RedissonReactiveClient
import org.redisson.codec.Kryo5Codec
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Path("/quarkus")
class ExampleResource {

    @Inject
    private lateinit var redissonClient: RedissonReactiveClient

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun quarkus(): Map<String, Any?> {
        val data = listOf(
            redissonClient.getBucket<String>("quarkus:test1").get().awaitSingleOrNull(),
            redissonClient.getBucket<String>("quarkus:test2").get().awaitSingleOrNull(),
            redissonClient.getBucket<String>("quarkus:test3").get().awaitSingleOrNull(),
        )

        return mapOf("message" to data)
    }
}

@ApplicationScoped
class SetupData {
    @Inject
    private lateinit var redissonClient: RedissonReactiveClient

    fun startUp(@Observes startup: Startup) {
    }

    @PostConstruct
    fun init() {
        runBlocking {
            redissonClient.getBucket<String>("quarkus:test1").set("test1").awaitSingleOrNull()
            redissonClient.getBucket<String>("quarkus:test2").set("test2").awaitSingleOrNull()
            redissonClient.getBucket<String>("quarkus:test3").set("test3").awaitSingleOrNull()
        }
    }
}

@Configuration
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

    @Bean
    fun redissonClient(redissonDefaultConfig: Config): RedissonClient {
        return Redisson.create(redissonDefaultConfig)
    }

    @Bean
    fun redissonReactiveClient(
        redissonClient: RedissonClient
    ): RedissonReactiveClient {
        return redissonClient.reactive()
    }
}
