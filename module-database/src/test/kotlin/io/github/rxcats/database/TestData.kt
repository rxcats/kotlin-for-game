package io.github.rxcats.database

import io.github.rxcats.database.mapper.User
import java.time.LocalDateTime

object TestData {
    val user = User("1000001", "Guest1000001", LocalDateTime.now())
    val user2 = User("1000002", "Guest1000002", LocalDateTime.now())
}
