package io.github.rxcats.jpa.component

import io.github.rxcats.jpa.type.DbType

interface ShardKeyHelper {
    fun shardNo(dbType: DbType, hashKey: Any): Int
}
