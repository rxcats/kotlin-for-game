package io.github.rxcats.database.mapper

import com.mybatisflex.annotation.Id
import java.time.LocalDateTime


data class User(
    @Id
    var userId: String = "",

    var nickname: String = "",

    var createdAt: LocalDateTime? = null
)
