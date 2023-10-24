package io.github.rxcats

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@ComponentScan
@Configuration(proxyBeanMethods = false)
class TestConfig

object UserTable : Table("exposed_user") {
    val id: Column<Long> = long("id")
    val name: Column<String> = varchar("name", 30)
    val age: Column<Int> = integer("age")
    val email: Column<String> = varchar("email", 50)

    override val primaryKey = PrimaryKey(id)
}

