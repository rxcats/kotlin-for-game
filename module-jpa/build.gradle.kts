dependencies {
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.bundles.jpql.jdsl)

    implementation(project(":core"))

    runtimeOnly(libs.mysql.connector.j)
    testImplementation(libs.spring.boot.starter.test)
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}
