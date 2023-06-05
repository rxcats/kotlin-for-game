package io.github.rxcats.core

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.InputStream

object Json : ObjectMapper() {
    init {
        registerModules(
            KotlinModule.Builder()
                .configure(KotlinFeature.NullIsSameAsDefault, true)
                .configure(KotlinFeature.NullToEmptyMap, true)
                .configure(KotlinFeature.NullToEmptyCollection, true)
                .build(),
            JavaTimeModule(),
            Jdk8Module()
        )
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    fun <T> parse(json: String, type: Class<T>): T = readValue(json, type)

    inline fun <reified T> parse(src: String): T = readValue(src)

    inline fun <reified T> parse(src: InputStream): T = readValue(src)

    inline fun <reified T> parse(src: ByteArray): T = readValue(src)

    fun <T> convert(from: Any, type: Class<T>): T = convertValue(from, type)

    inline fun <reified T> convert(from: Any): T = convertValue(from)

    fun stringify(o: Any): String = writeValueAsString(o)

    fun byteArray(o: Any): ByteArray = writeValueAsBytes(o)

    fun prettyStringify(o: Any): String {
        return writerWithDefaultPrettyPrinter()
            .writeValueAsString(o)
    }
}
