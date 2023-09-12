dependencies {
    implementation(libs.spring.boot.starter)
    api(libs.mybatis.plus.boot.starter)
    runtimeOnly(libs.mysql.connector.j)
    implementation(libs.spring.boot.starter.aop)
    implementation(project(":core"))
    api(project(":module-mybatis-plus"))
    testImplementation(libs.spring.boot.starter.test)
}
