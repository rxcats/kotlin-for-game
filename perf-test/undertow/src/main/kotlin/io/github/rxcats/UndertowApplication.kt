package io.github.rxcats

import io.github.rxcats.redis.RedissonConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(RedissonConfig::class)
@SpringBootApplication(proxyBeanMethods = false)
class UndertowApplication

fun main(args: Array<String>) {
    runApplication<UndertowApplication>(*args)
}
