dependencies {
    implementation(libs.spring.boot.starter)
    runtimeOnly(libs.mysql.connector.j)
    implementation(libs.spring.boot.starter.aop)
    implementation(project(":core"))
    api(project(":module-mybatis-flex"))
    testImplementation(libs.spring.boot.starter.test)
}
