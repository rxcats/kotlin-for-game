package io.github.rxcats.jpa.config

import com.zaxxer.hikari.HikariDataSource
import io.github.rxcats.core.loggerK
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

class RoutingDataSource : AbstractRoutingDataSource() {
    companion object {
        private val log by loggerK
    }

    override fun determineCurrentLookupKey(): Any? {
        return RoutingDataSourceContextHolder.get()
    }

    fun resolvedDataSourcesConnectionStatusLogging() {
        for ((dbname, ds) in this.resolvedDataSources) {
            val mx = (ds as HikariDataSource).hikariPoolMXBean
            log.info("dbname={}, activeConnections={}, idleConnections={}", dbname, mx.activeConnections, mx.idleConnections)
        }
    }

    fun targetDataSourceConnectionStatusLogging() {
        targetDataSource?.let { ds ->
            log.info("poolName={}, activeConnections={}, idleConnections={}", ds.poolName, ds.hikariPoolMXBean?.activeConnections, ds.hikariPoolMXBean.idleConnections)
        }
    }

    val targetDataSource: HikariDataSource?
        get() = RoutingDataSourceContextHolder.get()?.let {
            this.resolvedDataSources[it] as? HikariDataSource
        }

}
