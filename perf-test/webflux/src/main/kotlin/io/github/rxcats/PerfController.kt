package io.github.rxcats

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import org.redisson.api.RedissonReactiveClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PerfController {

    @Autowired
    private lateinit var redissonClient: RedissonReactiveClient

    @PostConstruct
    fun init() {
        runBlocking {
            redissonClient.getBucket<String>("webflux:test1").set("test1").awaitSingleOrNull()
            redissonClient.getBucket<String>("webflux:test2").set("test2").awaitSingleOrNull()
            redissonClient.getBucket<String>("webflux:test3").set("test3").awaitSingleOrNull()
        }
    }

    @GetMapping("/webflux")
    suspend fun webflux(): Map<String, Any?> {
        val data = listOf(
            redissonClient.getBucket<String>("webflux:test1").get().awaitSingleOrNull(),
            redissonClient.getBucket<String>("webflux:test2").get().awaitSingleOrNull(),
            redissonClient.getBucket<String>("webflux:test3").get().awaitSingleOrNull(),
        )

        return mapOf("message" to data)
    }

}
