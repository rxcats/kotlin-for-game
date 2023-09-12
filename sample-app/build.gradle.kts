import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(libs.spring.boot.starter.web) {
        val tomcat = libs.spring.boot.starter.tomcat.get()
        exclude(group = tomcat.module.group, module = tomcat.module.name)
    }
    implementation(libs.spring.boot.starter.undertow) {
        val undertow = libs.undertow.websockets.get()
        exclude(group = undertow.module.group, module = undertow.module.name)
    }

    implementation(libs.spring.boot.starter.validation)

    implementation(project(":core"))
    implementation(project(":module-database"))

    testImplementation(libs.spring.boot.starter.test)
}

tasks.withType<Jar> { enabled = false }
tasks.withType<BootJar> { enabled = true }
