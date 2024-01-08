plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.serialization").version("1.9.21")
    id("io.ktor.plugin") version "2.3.7"
}

application {
    mainClass.set("io.github.rxcats.KtorApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(libs.spring.boot.starter)
    implementation("io.ktor:ktor-server-compression-jvm:2.3.7")
    testImplementation(libs.spring.boot.starter.test)

    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")

    implementation("org.redisson:redisson:3.23.4")
    runtimeOnly("org.apache.commons:commons-pool2")

    implementation("io.ktor:ktor-client-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-compression")
}
