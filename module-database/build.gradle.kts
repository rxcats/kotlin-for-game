dependencies {
    implementation(project(":core"))
    api(project(":module-mybatis-plus"))
    api("com.baomidou:mybatis-plus-boot-starter")
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
