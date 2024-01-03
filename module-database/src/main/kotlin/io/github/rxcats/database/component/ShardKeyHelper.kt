package io.github.rxcats.database.component

interface ShardKeyHelper {
    fun shardNo(hashKey: Any): Int
}
