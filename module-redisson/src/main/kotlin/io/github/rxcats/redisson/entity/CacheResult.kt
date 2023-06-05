package io.github.rxcats.redisson.entity

import java.io.Serializable

@Suppress("UNCHECKED_CAST")
@JvmInline
value class CacheResult<out T> private constructor(
    private val value: Any?
) : Serializable {
    val isNull: Boolean
        get() = value == null

    fun getOrNull(): T? = value as? T

    fun getOrThrow(): T {
        if (isNull) throw NullPointerException()
        return value as T
    }

    companion object {
        fun <T> of(data: T? = null): CacheResult<T> = CacheResult(data)
    }
}
