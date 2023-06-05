dependencies {
    implementation("org.springframework.boot:spring-boot-starter")

    implementation("org.springframework:spring-web")
    implementation("jakarta.servlet:jakarta.servlet-api")
    implementation(project(":core"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-webmvc")
}
