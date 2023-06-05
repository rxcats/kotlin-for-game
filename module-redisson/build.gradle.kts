dependencies {
    implementation("org.springframework.boot:spring-boot-starter")

    implementation(project(":core"))
    implementation("org.redisson:redisson")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    compileOnly("org.apache.commons:commons-pool2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
