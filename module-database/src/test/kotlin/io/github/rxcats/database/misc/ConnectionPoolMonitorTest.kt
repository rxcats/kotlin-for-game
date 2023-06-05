package io.github.rxcats.database.misc

import io.github.rxcats.TestConfig
import io.github.rxcats.core.loggerK
import io.github.rxcats.database.component.RoutingQuery
import io.github.rxcats.database.config.RoutingDataSource
import io.github.rxcats.database.type.DbType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.PlatformTransactionManager

@SpringBootTest(classes = [TestConfig::class])
class ConnectionPoolMonitorTest {

    private val log by loggerK

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var routingDataSource: RoutingDataSource

    @Autowired
    private lateinit var transactionManager: PlatformTransactionManager

    @Test
    fun routingDataSourceConnectionPoolLogging() {
        routingDataSource.resolvedDataSourcesConnectionStatusLogging()

        assertThat(routingDataSource.targetDataSource).isNull()

        RoutingQuery(DbType.COMMON.get()) {
            routingDataSource.targetDataSourceConnectionStatusLogging()

            assertThat(routingDataSource.targetDataSource).isNotNull()
            assertThat(routingDataSource.targetDataSource?.poolName).isEqualTo(DbType.COMMON.get())

            val now = jdbcTemplate.queryForMap("select now() as now")
            val tz = jdbcTemplate.queryForMap("select @@global.time_zone as global_tz, @@session.time_zone as session_tz, @@system_time_zone as system_tz")

            log.info("tz={}, now={}", tz, now)

            routingDataSource.targetDataSourceConnectionStatusLogging()

            assertThat(now).containsKey("now")
        }

        assertThat(routingDataSource.targetDataSource).isNull()
    }

    @Test
    fun routingDataSourceConnectionPoolLoggingWithTransaction() {
        routingDataSource.resolvedDataSourcesConnectionStatusLogging()

        assertThat(routingDataSource.targetDataSource).isNull()

        RoutingQuery.transaction(DbType.COMMON.get()) {
            routingDataSource.targetDataSourceConnectionStatusLogging()

            assertThat(routingDataSource.targetDataSource).isNotNull()
            assertThat(routingDataSource.targetDataSource?.poolName).isEqualTo(DbType.COMMON.get())

            val now = jdbcTemplate.queryForMap("select now() as now")
            val tz = jdbcTemplate.queryForMap("select @@global.time_zone as global_tz, @@session.time_zone as session_tz, @@system_time_zone as system_tz")

            log.info("tz={}, now={}", tz, now)

            routingDataSource.targetDataSourceConnectionStatusLogging()

            assertThat(routingDataSource.targetDataSource?.hikariPoolMXBean?.activeConnections).isGreaterThanOrEqualTo(1)
            assertThat(now).containsKey("now")
        }

        routingDataSource.resolvedDataSourcesConnectionStatusLogging()

        assertThat(routingDataSource.targetDataSource).isNull()
    }

}
