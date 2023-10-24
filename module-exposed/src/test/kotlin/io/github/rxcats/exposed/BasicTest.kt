package io.github.rxcats.exposed

import io.github.rxcats.TestConfig
import io.github.rxcats.UserTable
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestConfig::class])
class BasicTest {
    @Test
    fun basicTest() {
        transaction {
            SchemaUtils.create(UserTable)

            UserTable.deleteWhere {
                this.id eq 1152090187195554628
            }

            val after = UserTable.insert {
                it[id] = 1152090187195554628
                it[name] = "user1152090187195554628"
                it[age] = 50
                it[email] = "1152090187195554628@email.com"
            }

            assertThat(after.insertedCount).isEqualTo(1)

            UserTable.update(
                where = {
                    UserTable.id eq 1152090187195554628
                },
                body = {
                    it[age] = 40
                }
            )

            val user = UserTable.select {
                UserTable.id eq 1152090187195554628
            }.first()

            assertThat(user[UserTable.id]).isEqualTo(1152090187195554628)
            assertThat(user[UserTable.age]).isEqualTo(40)

            SchemaUtils.drop(UserTable)
        }
    }

}
