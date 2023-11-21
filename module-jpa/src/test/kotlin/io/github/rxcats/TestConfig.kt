package io.github.rxcats

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.ScriptUtils

@ComponentScan
@Configuration(proxyBeanMethods = false)
class TestConfig {
    init {
        HikariDataSource(
            HikariConfig().apply {
                driverClassName = "com.mysql.cj.jdbc.Driver"
                jdbcUrl = "jdbc:mysql://localhost:3306"
                username = "root"
            }
        ).connection.use { conn ->
            val resource = ClassPathResource("schema.sql")
            ScriptUtils.executeSqlScript(conn, resource)
        }
    }
}
