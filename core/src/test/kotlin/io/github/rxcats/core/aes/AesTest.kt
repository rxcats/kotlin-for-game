package io.github.rxcats.core.aes

import io.github.rxcats.core.loggerK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AesTest {
    private val log by loggerK

    @Test
    fun simpleTest() {
        val json = """{"id":1,"name":"kotlin"}"""

        val encoded = Aes.encode(json)
        log.info("encoded isSuccess: {}", encoded.isSuccess)
        log.info("encoded: {}", encoded.getOrNull())
        assertThat(encoded.isSuccess).isTrue

        val decoded = Aes.decode(encoded.getOrThrow())
        log.info("decoded isSuccess: {}", decoded.isSuccess)
        log.info("decoded: {}", decoded.getOrNull())
        assertThat(decoded.isSuccess).isTrue
        assertThat(decoded.getOrNull()).isEqualTo(json)
    }

}
