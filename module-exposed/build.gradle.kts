dependencies {
    implementation(libs.spring.boot.starter)
    implementation(libs.exposed)
    runtimeOnly(libs.mysql.connector.j)
    implementation(project(":core"))
    testImplementation(libs.spring.boot.starter.test)
}
