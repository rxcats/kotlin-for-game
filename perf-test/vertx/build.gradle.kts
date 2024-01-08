dependencies {
    implementation(libs.spring.boot.starter)
    testImplementation(libs.spring.boot.starter.test)

    implementation("io.vertx:vertx-core:4.5.1")
    implementation("io.vertx:vertx-web:4.5.1")
    implementation("io.vertx:vertx-lang-kotlin:4.5.1")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:4.5.1")
    implementation(project(":perf-test:module-redis"))
}
