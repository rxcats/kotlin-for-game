package io.github.rxcats

import jakarta.annotation.PostConstruct
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PerfController {

    @Autowired
    private lateinit var redissonClient: RedissonClient

    @PostConstruct
    fun init() {
        redissonClient.getBucket<String>("undertow:test1").set("test1")
        redissonClient.getBucket<String>("undertow:test2").set("test2")
        redissonClient.getBucket<String>("undertow:test3").set("test3")
    }

    @GetMapping("/undertow")
    fun undertow(): Map<String, Any?> {
        val data =
            listOf(
                redissonClient.getBucket<String>("webflux:test1").get(),
                redissonClient.getBucket<String>("webflux:test2").get(),
                redissonClient.getBucket<String>("webflux:test3").get(),
            )

        return mapOf("message" to data)
    }

}
