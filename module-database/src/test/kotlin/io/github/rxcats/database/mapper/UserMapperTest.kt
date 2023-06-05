package io.github.rxcats.database.mapper

import io.github.rxcats.TestConfig
import io.github.rxcats.database.TestData
import io.github.rxcats.database.component.RoutingQuery
import io.github.rxcats.database.type.DbType
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DuplicateKeyException

@SpringBootTest(classes = [TestConfig::class, UserMapper::class])
class UserMapperTest {

    @Autowired
    private lateinit var userMapper: UserMapper

    companion object {
        @JvmStatic
        @BeforeAll
        fun beforeAll(
            @Autowired userMapper: UserMapper
        ) {
            RoutingQuery(DbType.USER.get(1)) {
                userMapper.deleteById(TestData.user)
            }

            RoutingQuery(DbType.USER.get(2)) {
                userMapper.deleteById(TestData.user2)
            }
        }
    }

    @Test
    fun insert() {
        val r1 = RoutingQuery(DbType.USER.get(1)) {
            userMapper.insert(TestData.user)
        }
        assertThat(r1).isEqualTo(1)

        val r2 = RoutingQuery(DbType.USER.get(2)) {
            userMapper.insert(TestData.user2)
        }
        assertThat(r2).isEqualTo(1)
    }

    @Test
    fun insertDuplicateKeyException() {
        assertThrows<DuplicateKeyException> {
            RoutingQuery(db = DbType.USER.get(1)) {
                userMapper.insert(TestData.user)
            }
        }
    }

    @Test
    fun select() {
        val r1 = RoutingQuery(DbType.USER.get(1)) {
            userMapper.selectById("1000001")
        }

        val r2 = RoutingQuery(DbType.USER.get(2)) {
            userMapper.selectById("1000002")
        }

        assertThat(r1).isNotNull()
        assertThat(r1?.userId).isEqualTo("1000001")

        assertThat(r2).isNotNull()
        assertThat(r2?.userId).isEqualTo("1000002")
    }

    @Test
    fun bulkInsert() {
        RoutingQuery(DbType.USER.get(1)) {
            val users = mutableListOf<User>()

            repeat(10) {
                users += User(userId = UUID.randomUUID().toString())
            }

            userMapper.bulkInsert(users)

            users.forEach { user ->
                val u = userMapper.selectById(user.userId)
                assertThat(u).isNotNull()

                userMapper.deleteById(user)
            }
        }
    }

    @Test
    fun upsertTest() {
        RoutingQuery(DbType.USER.get(1)) {
            val users = mutableListOf<User>()

            repeat(10) {
                users += User(userId = UUID.randomUUID().toString())
            }

            users.forEach { user ->
                userMapper.insert(user)

                user.nickname = UUID.randomUUID().toString().substring(0, 5)
            }

            users.forEach { user ->
                userMapper.upsert(user)
            }

            users.forEach { user ->
                val u = userMapper.selectById(user.userId)

                assertThat(u).isNotNull()
                assertThat(u.nickname).isNotBlank()

                userMapper.deleteById(user)
            }
        }
    }

}
