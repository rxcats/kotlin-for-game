package io.github.rxcats.database.mapper

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import java.time.LocalDateTime

data class CommonUser(
    @TableId
    var userId: String = "",

    var nickname: String = "",

    var shardNo: Int = 0,

    @TableField(fill = FieldFill.INSERT)
    var createdAt: LocalDateTime? = null
)

data class User(
    @TableId
    var userId: String = "",

    var nickname: String = "",

    @TableField(fill = FieldFill.INSERT)
    var createdAt: LocalDateTime? = null
)
