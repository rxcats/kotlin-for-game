dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.slf4j:slf4j-api")

    api("com.fasterxml.jackson.module:jackson-module-kotlin")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
