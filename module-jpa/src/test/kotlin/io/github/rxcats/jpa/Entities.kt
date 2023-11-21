package io.github.rxcats.jpa

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@Entity
@Table(name = "common_user")
data class CommonUser(
    @Id
    @Column(name = "user_id")
    var userId: String = "",

    @Column(name = "nickname")
    var nickname: String = "",

    @Column(name = "shard_no")
    var shardNo: Int = 0,

    @CreatedDate
    @Column(updatable = false, name = "created_at")
    var createdAt: LocalDateTime? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        return userId == other.userId
    }

    override fun hashCode(): Int {
        return userId.hashCode()
    }
}

@EntityListeners(AuditingEntityListener::class)
@Table(name = "user")
@Entity
data class User(
    @Id
    @Column(name = "user_id")
    var userId: String = "",

    @Column(name = "nickname")
    var nickname: String = "",

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        return userId == other.userId
    }

    override fun hashCode(): Int {
        return userId.hashCode()
    }
}

interface CommonUserRepository : CrudRepository<CommonUser, String>, KotlinJdslJpqlExecutor

interface UserRepository : CrudRepository<User, String>, KotlinJdslJpqlExecutor
