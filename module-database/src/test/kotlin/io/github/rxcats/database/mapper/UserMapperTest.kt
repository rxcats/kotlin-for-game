package io.github.rxcats.database.mapper

import io.github.rxcats.TestConfig
import io.github.rxcats.database.TestData
import io.github.rxcats.database.component.RoutingQuery
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

            RoutingQuery("userdb1") {
                userMapper.deleteById("1000001")
            }

            RoutingQuery("userdb2") {
                userMapper.deleteById("1000002")
            }
        }
    }

    @Test
    fun insert() {
        val r1 = RoutingQuery("userdb1") {
            userMapper.insert(TestData.user)
        }
        assertThat(r1).isEqualTo(1)

        val r2 = RoutingQuery("userdb2") {
            userMapper.insert(TestData.user2)
        }
        assertThat(r2).isEqualTo(1)
    }

    @Test
    fun insertDuplicateKeyException() {
        assertThrows<DuplicateKeyException> {
            RoutingQuery("userdb1") {
                userMapper.insert(TestData.user)
            }
        }
    }

    @Test
    fun select() {
        val r1 = RoutingQuery("userdb1") {
            userMapper.selectOneById("1000001")
        }

        val r2 = RoutingQuery("userdb2") {
            userMapper.selectOneById("1000002")
        }

        assertThat(r1).isNotNull()
        assertThat(r1?.userId).isEqualTo("1000001")

        assertThat(r2).isNotNull()
        assertThat(r2?.userId).isEqualTo("1000002")
    }

}
