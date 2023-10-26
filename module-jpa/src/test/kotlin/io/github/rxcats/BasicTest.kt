package io.github.rxcats

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [JpaConfig::class])
class BasicTest {
    @Autowired
    private lateinit var repository: UserRepository

    @Test
    fun findByIdTest() {
        val result = repository.findById(30011)
        println(result)
    }

    @Test
    fun jpqlSelectTest() {
        val names = repository.findAll {
            select(
                path(User::name)
            ).from(
                entity(User::class)
            ).where(
                path(User::id).le(30017L)
                    .and(path(User::age).eq(20))
            )
        }

        println(names)
    }
}
