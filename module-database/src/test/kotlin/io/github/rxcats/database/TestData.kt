package io.github.rxcats.database

import io.github.rxcats.database.mapper.CommonUser
import io.github.rxcats.database.mapper.User
import java.time.LocalDateTime

object TestData {
    val commonUser = CommonUser("1000001", "Guest1000001", 1, LocalDateTime.now())
    val commonUser2 = CommonUser("1000002", "Guest1000002", 2, LocalDateTime.now())
    val user = User("1000001", "Guest1000001", LocalDateTime.now())
    val user2 = User("1000002", "Guest1000002", LocalDateTime.now())
}
