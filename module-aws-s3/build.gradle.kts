dependencies {
    api(libs.awssdk.s3)
    implementation(libs.spring.boot.starter)
    implementation(project(":core"))
    implementation(project(":module-aws-auth"))
    testImplementation(libs.spring.boot.starter.test)
}
