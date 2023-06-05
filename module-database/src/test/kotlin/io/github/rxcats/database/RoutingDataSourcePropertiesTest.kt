package io.github.rxcats.database

import io.github.rxcats.database.config.RoutingDataSourceConfig
import io.github.rxcats.database.config.RoutingDataSourceProperties
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [RoutingDataSourceConfig::class])
class RoutingDataSourcePropertiesTest {

    @Autowired
    private lateinit var properties: RoutingDataSourceProperties

    @Test
    fun test() {
        assertThat(properties).isNotNull()
        assertThat(properties.driverClassName).isEqualTo("mysql.cj.jdbc.Driver")
    }

}
