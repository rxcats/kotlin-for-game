package io.github.rxcats.database.component.impl

import io.github.rxcats.database.component.ShardKeyHelper
import io.github.rxcats.database.config.RoutingDataSourceProperties
import io.github.rxcats.database.type.DbType
import java.util.zip.CRC32

class CRC32ShardKeyHelper(
    private val properties: RoutingDataSourceProperties
) : ShardKeyHelper {

    private val crc = CRC32()

    override fun shardNo(dbType: DbType, hashKey: Any): Int {
        if (!dbType.sharded) return 1

        val shardTargets = properties.info.getValue(dbType.dbname).shardTargets

        if (shardTargets.size <= 1) return 1

        crc.update(hashKey.toString().toByteArray())
        val shard = crc.value % shardTargets.size
        return shardTargets[shard.toInt()]
    }
}
