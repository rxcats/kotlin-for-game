dependencies {
    implementation(libs.spring.boot.starter)
    implementation(libs.slf4j.api)

    api(libs.bundles.jackson)

    testImplementation(libs.spring.boot.starter.test)
}
