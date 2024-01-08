package io.github.rxcats

import io.github.rxcats.redis.RedissonConfig
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.kotlin.core.json.Json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.coroutines.coAwait
import io.vertx.kotlin.coroutines.vertxFuture
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import org.redisson.api.RedissonReactiveClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service

@Import(RedissonConfig::class)
@SpringBootApplication(proxyBeanMethods = false)
class VertxApplication {

    @Bean
    fun vertx(): Vertx {
        return Vertx.vertx()
    }

    @Bean
    fun httpServer(vertx: Vertx): HttpServer {
        return vertx.createHttpServer()
    }

    @Bean
    fun router(vertx: Vertx): Router {
        return Router.router(vertx)
    }

}

fun main(args: Array<String>) {
    val ctx = runApplication<VertxApplication>(*args)
    val env = ctx.getBean<Environment>()
    val httpServer = ctx.getBean<HttpServer>()
    val router = ctx.getBean<Router>()
    val perfHandler = ctx.getBean<PerfHandler>()
    val port = env["server.port"]?.toInt() ?: 8080

    router.get("/vertx").respond {
        vertxFuture {
            val data = Json.obj(perfHandler.data()).encode()
            it.end(data).coAwait()
        }
    }

    router.errorHandler(500) {
        println("500 error")
    }

    httpServer.requestHandler(router)
        .listen(port)
}

@Service
class PerfHandler {
    @Autowired
    private lateinit var redissonClient: RedissonReactiveClient

    @PostConstruct
    fun init() {
        runBlocking {
            redissonClient.getBucket<String>("vertx:test1").set("test1").awaitSingleOrNull()
            redissonClient.getBucket<String>("vertx:test2").set("test2").awaitSingleOrNull()
            redissonClient.getBucket<String>("vertx:test3").set("test3").awaitSingleOrNull()
        }
    }

    suspend fun data(): Map<String, List<String?>> {
        val data = listOf(
            redissonClient.getBucket<String>("vertx:test1").get().awaitSingleOrNull(),
            redissonClient.getBucket<String>("vertx:test2").get().awaitSingleOrNull(),
            redissonClient.getBucket<String>("vertx:test3").get().awaitSingleOrNull(),
        )
        return mapOf("message" to data)
    }
}
