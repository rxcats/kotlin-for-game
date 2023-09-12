pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.version.toml"))
        }
    }
}

rootProject.name = "kotlin-for-game"

include("core")
include("module-mybatis-plus")
include("module-database")
include("module-redisson")
include("module-web")

include("module-aws-auth")
include("module-aws-dynamodb")
include("module-aws-s3")

include("sample-app")
