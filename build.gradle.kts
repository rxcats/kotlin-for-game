import io.spring.gradle.dependencymanagement.dsl.ImportsHandler
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.incremental.createDirectory

fun ImportsHandler.mavenBom(dep: Provider<MinimalExternalModuleDependency>) = this.mavenBom(dep.get().toString())
fun PluginAware.apply(dep: Provider<PluginDependency>) = this.apply(plugin = dep.get().pluginId)

val os = DefaultNativePlatform.getCurrentOperatingSystem() ?: error("cannot find os information")
val arch = DefaultNativePlatform.getCurrentArchitecture() ?: error("cannot find arch information")
val javaVersion = JavaVersion.VERSION_21

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
}

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

allprojects {
    group = "io.github.rxcats"
    version = "0.0.1"

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
    }
}

subprojects {
    apply(rootProject.libs.plugins.spring.boot)
    apply(rootProject.libs.plugins.spring.dependency.management)
    apply(rootProject.libs.plugins.kotlin.jvm)
    apply(rootProject.libs.plugins.kotlin.spring)

    java {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    dependencies {
        implementation(rootProject.libs.bundles.kotlin)
        implementation(rootProject.libs.bundles.kotlin.coroutines)

        if (os.isMacOsX && arch.isArm) {
            runtimeOnly(variantOf(rootProject.libs.netty.dns.macos) { classifier("osx-aarch_64") })
        }
    }

    dependencyManagement {
        imports {
            mavenBom(rootProject.libs.aws.bom)
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = javaVersion.toString()
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<Delete> {
        delete("build", "out")
    }
}

// Task for creating module directories
// After creating the directory, you need to add the module information to `settings.gradle.kts`
tasks.register("createModule") {
    doLast {
        val moduleName = providers.systemProperty("moduleName").orNull

        if (moduleName.isNullOrBlank()) {
            error("require moduleName property `./gradlew ${this.name} -DmoduleName={moduleName}`")
        }

        // main
        file("${rootDir.absolutePath}/${moduleName}/src/main/kotlin/io/github/rxcats").createDirectory()
        file("${rootDir.absolutePath}/${moduleName}/src/main/resources").createDirectory()
        file("${rootDir.absolutePath}/${moduleName}/src/main/resources/application.properties").createNewFile()

        // test
        file("${rootDir.absolutePath}/${moduleName}/src/test/kotlin/io/github/rxcats").createDirectory()
        file("${rootDir.absolutePath}/${moduleName}/src/test/resources").createDirectory()
        file("${rootDir.absolutePath}/${moduleName}/src/test/resources/application.properties").createNewFile()

        // build.gradle.kts
        val gradleBuildDefaultDeps = """
            dependencies {
                implementation(libs.spring.boot.starter)
                testImplementation(libs.spring.boot.starter.test)
            }
        """.trimIndent()
        file("${rootDir.absolutePath}/${moduleName}/build.gradle.kts").writeText(gradleBuildDefaultDeps, Charsets.UTF_8)

        println("""##### After add `include(":$moduleName")` to `settings.gradle.kts` #####""")
    }
}
