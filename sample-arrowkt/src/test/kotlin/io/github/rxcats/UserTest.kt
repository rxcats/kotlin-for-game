package io.github.rxcats

class UserTest {

    object UserNotFound
    data class User(val id: UserId)

    @JvmInline
    value class UserId(val id: Long)

    private val userPool = mapOf(
        UserId(1) to User(UserId(1)),
        UserId(2) to User(UserId(2)),
        UserId(3) to User(UserId(3)),
        UserId(4) to User(UserId(4)),
        UserId(5) to User(UserId(5)),
    )

}
