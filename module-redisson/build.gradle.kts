dependencies {
    implementation(libs.spring.boot.starter)
    implementation(libs.redisson)
    implementation(libs.spring.boot.starter.aop)
    compileOnly(libs.commons.pool2)
    implementation(project(":core"))
    testImplementation(libs.spring.boot.starter.test)
}
