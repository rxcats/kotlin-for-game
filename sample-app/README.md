# Sample App

## 기능

- 로그인 샘플 API
- MySQL DB 기능
- 예외 핸들러

### 웹서버

- Tomcat 을 제외하고 Undertow 를 사용하도록 설정하였습니다.

```
# build.gradle.kts

implementation("org.springframework.boot:spring-boot-starter-web") {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
}
implementation("org.springframework.boot:spring-boot-starter-undertow") {
    exclude(group = "io.undertow", module = "undertow-websockets-jsr")
}
```

### DB 스키마 준비

- 앱 시작시 db/schema.sql 을 실행할 수 있도록 init 블럭에서 sql 을 실행하도록 하였습니다.

```kotlin
@MapperScan
@SpringBootApplication(proxyBeanMethods = false)
class SampleApplication : WebMvcConfigurer {
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
```

### 응답 메시지 구조

#### 성공 응답 메시지

```json
{
  "code": 200,
  "now": 1685953066028,
  "data": {
  }
}
````

#### 에러 응답 메시지

```json
{
  "code": 400,
  "now": 1685952610976,
  "error": "invalid accessToken"
}
````

### API

- 로그인과 로그인 실패 API 두가지 기능이 있습니다.

#### 로그인

```shell
curl -X POST -H "Content-Type: application/json" -d '{"userId": "user#1","accessToken": "accessToken"}' localhost:8080/auth/login

# 응답
{
  "code": 200,
  "now": 1685953066028,
  "data": {
    "userId": "user#1",
    "nickname": "GUEST",
    "createdAt": "2023-06-05T08:10:23",
    "updatedAt": "2023-06-05T08:17:46.024402"
  }
}
```

#### 로그인 실패 (invalid accessToken)

```shell
curl -X POST -H "Content-Type: application/json" -d '{"userId": "user#1","accessToken": "invalidAccessToken"}' localhost:8080/auth/login

# 응답
{
  "code": 400,
  "now": 1685952610976,
  "error": "invalid accessToken"
}
```

### 예외 핸들러

- @RestControllerAdvice 를 이용하여 Exception 에 해당되는 에러 메시지를 변환하여 응답으로 제공할 수 있습니다.

```kotlin
@RestControllerAdvice
class ApplicationExceptionHandler
```
