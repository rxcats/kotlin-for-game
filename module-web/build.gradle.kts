dependencies {
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.web)
    implementation(libs.servlet.api)
    implementation(project(":core"))
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.webmvc)
}
