package io.github.rxcats

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AgeProblemTest {
    @JvmInline value class Age(val age: Int)

    sealed interface AgeProblem {
        object InvalidAge: AgeProblem
        object NotLegalAdult: AgeProblem
    }

    private fun validAdult(age: Int): Either<AgeProblem, Age> = when {
        age < 0 -> AgeProblem.InvalidAge.left()
        age < 18 -> AgeProblem.NotLegalAdult.left()
        else -> Age(age).right()
    }

    @Test
    fun validAdultTest() {
        assertThat(validAdult(-1).isLeft()).isTrue()
        assertThat(validAdult(10).isLeft()).isTrue()

        assertThat(validAdult(18).isRight()).isTrue()
        assertThat(validAdult(18).getOrNull()?.age).isEqualTo(18)
    }

}
