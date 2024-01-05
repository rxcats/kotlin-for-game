dependencies {
    implementation(libs.spring.boot.starter)
    testImplementation(libs.spring.boot.starter.test)

    api("org.redisson:redisson:3.23.4")
    runtimeOnly("org.apache.commons:commons-pool2")
}
