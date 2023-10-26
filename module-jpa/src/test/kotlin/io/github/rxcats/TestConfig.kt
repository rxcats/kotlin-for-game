package io.github.rxcats

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.JpaRepository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
@ComponentScan
@Configuration(proxyBeanMethods = false)
class TestConfig

@Entity
@Table(name = "user")
class User {
    @Id
    var id: Long? = null
    var name: String? = null
    var age: Int? = null
    var email: String? = null

    override fun toString(): String {
        return "User(id=$id, name=$name, age=$age, email=$email)"
    }

}

interface UserRepository: JpaRepository<User, Long>, KotlinJdslJpqlExecutor {



}
