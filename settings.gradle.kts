pluginManagement {
    repositories {
        gradlePluginPortal()
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
