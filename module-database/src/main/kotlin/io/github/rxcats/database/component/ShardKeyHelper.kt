package io.github.rxcats.database.component

import io.github.rxcats.database.type.DbType

interface ShardKeyHelper {
    fun shardNo(dbType: DbType, hashKey: Any): Int
}
