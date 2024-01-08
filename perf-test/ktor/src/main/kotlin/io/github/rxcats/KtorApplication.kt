package io.github.rxcats

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.deflate
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.plugins.compression.minimumSize
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.api.RedissonReactiveClient
import org.redisson.codec.Kryo5Codec
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class KtorServerConfig {
    @Bean
    fun server(): NettyApplicationEngine {
        return embeddedServer(Netty, port = 19005, host = "0.0.0.0", module = Application::module)
    }
}

@SpringBootApplication(proxyBeanMethods = false)
class KtorApplication {
    @Autowired
    private lateinit var server: NettyApplicationEngine

    @PostConstruct
    fun start() {
        server.start(wait = true)
    }

    @PreDestroy
    fun stop() {
        server.stop()
    }
}

fun main(args: Array<String>) {
    runApplication<KtorApplication>(*args)
}

fun Application.module() {
    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024) // condition
        }
    }

    configureRouting()
}

fun Application.configureRouting() {
    val redissonConfig = RedissonConfig()
    val config = redissonConfig.redissonDefaultConfig()
    val redissonClient = redissonConfig.redissonClient(config)
    val redissonReactiveClient = redissonConfig.redissonReactiveClient(redissonClient)

    routing {
        get("/ktor") {
            val data = listOf(
                redissonReactiveClient.getBucket<String>("vertx:test1").get().awaitSingleOrNull(),
                redissonReactiveClient.getBucket<String>("vertx:test2").get().awaitSingleOrNull(),
                redissonReactiveClient.getBucket<String>("vertx:test3").get().awaitSingleOrNull(),
            )
            call.respond(Json.encodeToString(data))
        }
    }
}

class RedissonConfig {
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

    fun redissonClient(redissonDefaultConfig: Config): RedissonClient {
        return Redisson.create(redissonDefaultConfig)
    }

    fun redissonReactiveClient(
        redissonClient: RedissonClient
    ): RedissonReactiveClient {
        return redissonClient.reactive()
    }
}
