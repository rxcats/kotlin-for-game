package io.github.rxcats.jpa.config

import java.time.Duration

data class HikariConfigValue(
    val jdbcUrl: String,
    val username: String,
    val password: String,
    val maxPoolSize: Int,
    val connectionTimeout: Duration,
    val replica: Boolean = false,
) {
    override fun toString(): String {
        return "HikariConfigValue(jdbcUrl=$jdbcUrl, username=$username, password=\uD83D\uDD11, maxPoolSize=$maxPoolSize, connectionTimeout=${connectionTimeout.toMillis()}, replica=$replica)"
    }
}

data class ShardInfoConfigValue(
    val shardTargets: List<Int> = emptyList(),
)
