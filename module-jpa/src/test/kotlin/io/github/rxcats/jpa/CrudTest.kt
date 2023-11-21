package io.github.rxcats.jpa

import io.github.rxcats.TestConfig
import io.github.rxcats.jpa.component.RoutingQuery
import io.github.rxcats.jpa.type.DbType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest(classes = [TestConfig::class])
class CrudTest {

    @Autowired
    private lateinit var commonUserRepository: CommonUserRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    companion object {
        @JvmStatic
        @AfterAll
        fun afterAll(
            @Autowired commonUserRepository: CommonUserRepository,
            @Autowired userRepository: UserRepository,
        ) {
            RoutingQuery.transaction(DbType.COMMON.get()) {
                commonUserRepository.deleteById(TestData.commonUser.userId)
                commonUserRepository.deleteById(TestData.commonUser2.userId)
            }

            RoutingQuery.transaction(DbType.USER.get(1)) {
                userRepository.deleteById(TestData.user.userId)
                userRepository.deleteById(TestData.user2.userId)
            }
        }
    }

    @Test
    fun basicTest() {
        RoutingQuery.transaction(DbType.COMMON.get()) {
            val user = TestData.commonUser
            val user2 = TestData.commonUser2

            commonUserRepository.save(user)
            commonUserRepository.save(user2)

            val after = commonUserRepository.findByIdOrNull(user.userId)
            assertThat(after?.userId).isEqualTo(user.userId)
        }

        RoutingQuery.transaction(DbType.USER.get(1)) {
            val user = TestData.user
            val user2 = TestData.user2

            userRepository.save(user)
            userRepository.save(user2)

            val after = userRepository.findByIdOrNull(user.userId)
            assertThat(after?.userId).isEqualTo(user.userId)

            val (first, second) = userRepository.findAll {
                select(path(User::userId))
                    .from(entity(User::class))
                    .orderBy(path(User::userId).desc())
            }

            assertThat(first).isEqualTo(user2.userId)
            assertThat(second).isEqualTo(user.userId)
        }
    }
}
