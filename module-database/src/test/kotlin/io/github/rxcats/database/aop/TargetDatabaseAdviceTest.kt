package io.github.rxcats.database.aop

import io.github.rxcats.TestConfig
import io.github.rxcats.database.type.DbType
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Repository

@SpringBootTest(classes = [TestConfig::class, TargetDatabaseAdviceTest.AopRepository::class])
class TargetDatabaseAdviceTest {
    @Repository
    internal class AopRepository {
        @TargetDatabase(db = DbType.USER)
        fun missingShardKey() {

        }

        @TargetDatabase(db = DbType.USER)
        fun withShardHashKey(@ShardHashKey userId: String): Int {
            return 1
        }

        @TargetDatabase(db = DbType.USER)
        fun withShardNo(@ShardNo shardNo: Int): Int {
            return 1
        }
    }

    @Autowired
    private lateinit var repository: AopRepository

    @Test
    fun missingShardKeyErrorTest() {
        assertThrows<IllegalStateException> {
            repository.missingShardKey()
        }
    }

    @Test
    fun withShardHashKeyTest() {
        val result = repository.withShardHashKey(UUID.randomUUID().toString())
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun withShardNoTest() {
        val result = repository.withShardNo(1)
        assertThat(result).isEqualTo(1)
    }

}
