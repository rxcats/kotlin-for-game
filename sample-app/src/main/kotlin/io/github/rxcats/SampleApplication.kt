package io.github.rxcats

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table
import com.mybatisflex.core.BaseMapper
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.rxcats.core.loggerK
import io.github.rxcats.database.component.RoutingQuery
import jakarta.validation.ConstraintViolationException
import jakarta.validation.constraints.NotBlank
import org.apache.ibatis.io.Resources
import org.apache.ibatis.jdbc.ScriptRunner
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.LocalDateTime

@MapperScan
@SpringBootApplication(proxyBeanMethods = false)
class SampleApplication : WebMvcConfigurer {
    @Bean
    fun objectMapper(): ObjectMapper = Jackson2ObjectMapperBuilder.json()
        .featuresToDisable(
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
        )
        .modules(
            JavaTimeModule(),
            Jdk8Module(),
            KotlinModule.Builder()
                .configure(KotlinFeature.NullIsSameAsDefault, true)
                .configure(KotlinFeature.NullToEmptyMap, true)
                .configure(KotlinFeature.NullToEmptyCollection, true)
                .build()
        )
        .build()

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedHeaders("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    }

    init {
        HikariDataSource(
            HikariConfig().apply {
                driverClassName = "com.mysql.cj.jdbc.Driver"
                jdbcUrl = "jdbc:mysql://localhost:3306"
                username = "root"
            }
        ).connection.use { conn ->
            val runner = ScriptRunner(conn)
            runner.setAutoCommit(true)
            runner.setStopOnError(true)
            runner.runScript(Resources.getResourceAsReader("db/schema.sql"))
        }
    }
}

fun main(args: Array<String>) {
    runApplication<SampleApplication>(*args)
}

data class LoginParam(
    @get:NotBlank
    val userId: String = "",

    @get:NotBlank
    val accessToken: String = ""
)

data class ApiResponse<out T : Any>(
    val code: Int = 200,

    val now: Long = System.currentTimeMillis(),

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val data: T? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val error: String? = null
)

@RequestMapping("/auth")
@RestController
class AuthController(
    private val service: AuthService
) {
    @PostMapping("/login")
    fun login(@Validated @RequestBody param: LoginParam): ApiResponse<User> {
        service.validateAccessTokenOrThrow(param.accessToken)

        var user = service.getUser(param.userId)

        if (user == null) {
            user = service.createUser(param.userId)
        } else {
            service.updateLoginUser(user)
        }

        return ApiResponse(data = user)
    }

    @RequestMapping("/error")
    fun error() {
        throw IllegalArgumentException("error response")
    }
}

@Table("user")
data class User(
    @Id
    var userId: String? = null,

    var nickname: String = "",

    var createdAt: LocalDateTime? = null,

    var updatedAt: LocalDateTime? = null
)

interface UserMapper : BaseMapper<User>

@Service
class AuthService(
    private val mapper: UserMapper
) {
    fun validateAccessTokenOrThrow(accessToken: String) {
        if (accessToken.startsWith("invalid")) {
            throw IllegalArgumentException("invalid accessToken")
        }
    }

    fun getUser(userId: String): User? = RoutingQuery("user1") {
        mapper.selectOneById(userId)
    }

    fun updateLoginUser(user: User): User = RoutingQuery("user1") {
        mapper.update(user)
        user
    }

    fun createUser(userId: String): User = RoutingQuery("user1") {
        val now = LocalDateTime.now()
        val user = User(userId = userId, nickname = "GUEST", createdAt = now, updatedAt = now)
        mapper.insert(user)
        user
    }
}

@RestControllerAdvice
class ApplicationExceptionHandler {
    private val log by loggerK

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleException(e: IllegalArgumentException): ApiResponse<Any> {
        log.error(e.message, e)
        return ApiResponse(code = 400, error = e.message)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleException(e: ConstraintViolationException): ApiResponse<Any> {
        log.error(e.message, e)

        for (rs in e.constraintViolations) {
            log.error("propertyPath={} message={} invalidValue={}", rs.propertyPath, rs.message, rs.invalidValue)
        }

        return ApiResponse(
            code = 400,
            error = e.message,
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(e: MethodArgumentNotValidException): ApiResponse<Any> {
        log.error(e.message, e)

        for (rs in e.bindingResult.fieldErrors) {
            log.error("field={} defaultMessage={} rejectedValue={}", rs.field, rs.defaultMessage, rs.rejectedValue)
        }

        return ApiResponse(
            code = 400,
            error = e.message,
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ApiResponse<Any> {
        log.error(e.message, e)
        return ApiResponse(
            code = 500,
            error = e.message
        )
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleException(e: NoHandlerFoundException): ApiResponse<Any> {
        log.error(e.message, e)
        return ApiResponse(
            code = 404,
            error = e.message
        )
    }

}
