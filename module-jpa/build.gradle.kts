dependencies {
    implementation(libs.spring.boot.starter)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("com.linecorp.kotlin-jdsl:jpql-dsl:3.0.0")
    implementation("com.linecorp.kotlin-jdsl:jpql-render:3.0.0")
    implementation("com.linecorp.kotlin-jdsl:spring-data-jpa-support:3.0.0")

    runtimeOnly(libs.mysql.connector.j)
    testImplementation(libs.spring.boot.starter.test)
}
