import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version Deps.springBootVersion
    id("io.spring.dependency-management") version Deps.springDependencyManagementVersion
    kotlin("jvm") version Deps.kotlinVersion
    kotlin("plugin.spring") version Deps.kotlinVersion
}

java.sourceCompatibility = JavaVersion.VERSION_17

allprojects {
    group = "io.github.rxcats"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
        runtimeOnly(group = "io.netty", name = "netty-resolver-dns-native-macos", classifier = "osx-aarch_64")
    }

    dependencyManagement {
        imports {
            mavenBom("software.amazon.awssdk:bom:${Deps.awsJavaSdkV2Version}")
        }

        dependencies {
            dependency("com.baomidou:mybatis-plus-boot-starter:${Deps.mybatisPlusVersion}")
            dependency("com.baomidou:mybatis-plus-extension:${Deps.mybatisPlusVersion}")
            dependency("com.ninja-squad:springmockk:${Deps.springMockkVersion}")
            dependency("org.redisson:redisson:${Deps.redissonVersion}")
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<Delete> {
        delete("build", "out")
    }
}
