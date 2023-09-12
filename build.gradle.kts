import io.spring.gradle.dependencymanagement.dsl.ImportsHandler
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun ImportsHandler.mavenBom(dep: Provider<MinimalExternalModuleDependency>) = this.mavenBom(dep.get().toString())
fun PluginAware.apply(dep: Provider<PluginDependency>) = this.apply(plugin = dep.get().pluginId)

val os = DefaultNativePlatform.getCurrentOperatingSystem() ?: error("cannot find os information")
val arch = DefaultNativePlatform.getCurrentArchitecture() ?: error("cannot find arch information")

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
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
    apply(rootProject.libs.plugins.spring.boot)
    apply(rootProject.libs.plugins.spring.dependency.management)
    apply(rootProject.libs.plugins.kotlin.jvm)
    apply(rootProject.libs.plugins.kotlin.spring)

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
