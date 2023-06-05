import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow") {
        exclude(group = "io.undertow", module = "undertow-websockets-jsr")
    }
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation(project(":core"))
    implementation(project(":module-database"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Jar> { enabled = false }
tasks.withType<BootJar> { enabled = true }
