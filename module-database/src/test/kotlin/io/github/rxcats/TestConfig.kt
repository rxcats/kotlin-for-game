package io.github.rxcats

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.apache.ibatis.io.Resources.getResourceAsReader
import org.apache.ibatis.jdbc.ScriptRunner
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@MapperScan
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
            val runner = ScriptRunner(conn)
            runner.setAutoCommit(true)
            runner.setStopOnError(true)
            runner.runScript(getResourceAsReader("schema.sql"))
        }
    }
}
