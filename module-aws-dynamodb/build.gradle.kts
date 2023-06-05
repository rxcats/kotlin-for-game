dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    api("software.amazon.awssdk:dynamodb")
    api("software.amazon.awssdk:dynamodb-enhanced")
    implementation(project(":core"))
    implementation(project(":module-aws-auth"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
