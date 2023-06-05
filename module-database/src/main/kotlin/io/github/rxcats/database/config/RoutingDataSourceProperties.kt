package io.github.rxcats.database.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.db")
class RoutingDataSourceProperties(
    val driverClassName: String = "com.mysql.cj.jdbc.Driver",
    val info: LinkedHashMap<String, ShardInfoConfigValue>,
    val hikari: LinkedHashMap<String, HikariConfigValue>,
)
