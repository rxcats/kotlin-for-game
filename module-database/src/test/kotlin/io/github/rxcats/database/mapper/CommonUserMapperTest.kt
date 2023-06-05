package io.github.rxcats.database.mapper

import io.github.rxcats.TestConfig
import io.github.rxcats.database.TestData
import io.github.rxcats.database.component.RoutingQuery
import io.github.rxcats.database.type.DbType
import io.github.rxcats.mybatisplus.extensions.selectCount
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestConfig::class, CommonUserMapper::class])
class CommonUserMapperTest {

    @Autowired
    private lateinit var commonUserMapper: CommonUserMapper

    companion object {
        @JvmStatic
        @AfterAll
        fun afterAll(
            @Autowired commonUserMapper: CommonUserMapper
        ) {
            RoutingQuery(DbType.COMMON.get()) {
                commonUserMapper.deleteById(TestData.commonUser)
                commonUserMapper.deleteById(TestData.commonUser2)
            }
        }
    }

    @Test
    fun basicTest() {
        RoutingQuery(DbType.COMMON.get()) {
            val user = TestData.commonUser
            val user2 = TestData.commonUser2

            val insertCnt = commonUserMapper.insert(user)
            assertThat(insertCnt).isEqualTo(1)

            val insertCnt2 = commonUserMapper.insert(user2)
            assertThat(insertCnt2).isEqualTo(1)

            val after = commonUserMapper.selectById(user.userId)

            assertThat(after).isNotNull()
            assertThat(after?.nickname).isEqualTo(user.nickname)

            val after2 = commonUserMapper.selectById(user2.userId)

            assertThat(after2).isNotNull()
            assertThat(after2?.nickname).isEqualTo(user2.nickname)

            val cnt = commonUserMapper.selectCount {
                CommonUser::userId inValues listOf(TestData.commonUser.userId, TestData.commonUser2.userId)
            }

            assertThat(cnt).isEqualTo(2)
        }
    }
}
