dependencies {
    implementation(libs.spring.boot.starter)
    testImplementation(libs.spring.boot.starter.test)

    api(libs.mybatis.flex.spring.boot.starter) {
        val mybatisSpring = libs.mybatis.spring.get()
        exclude(group = mybatisSpring.module.group, module = mybatisSpring.module.name)
    }
    api(libs.mybatis.spring)
    api(libs.mybatis.flex.kotlin.extensions)
    api(libs.hikari.cp)
    runtimeOnly(libs.mysql.connector.j)
}
