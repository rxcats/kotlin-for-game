dependencies {
    api(libs.awssdk.dynamodb)
    api(libs.awssdk.dynamodb.enhanced)
    implementation(libs.spring.boot.starter)
    implementation(project(":core"))
    implementation(project(":module-aws-auth"))
    testImplementation(libs.spring.boot.starter.test)
}
