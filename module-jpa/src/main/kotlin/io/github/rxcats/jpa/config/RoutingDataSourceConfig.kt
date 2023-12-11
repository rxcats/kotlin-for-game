package io.github.rxcats.jpa.config

import com.zaxxer.hikari.HikariDataSource
import io.github.rxcats.core.loggerK
import io.github.rxcats.jpa.component.ShardKeyHelper
import io.github.rxcats.jpa.component.impl.CRC32ShardKeyHelper
import jakarta.persistence.EntityManagerFactory
import org.hibernate.cfg.AvailableSettings
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.orm.hibernate5.SpringBeanContainer
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration(proxyBeanMethods = false)
@EnableAutoConfiguration
@EnableConfigurationProperties(RoutingDataSourceProperties::class)
class RoutingDataSourceConfig {
    private val log by loggerK

    @Bean("routingDataSource")
    fun routingDataSource(properties: RoutingDataSourceProperties): RoutingDataSource {
        val dataSourceMap = linkedMapOf<Any, Any>()

        log.info("driverClassName={}", properties.driverClassName)

        for ((dbname, info) in properties.info) {
            log.info("dbname={}, info={}", dbname, info)
        }

        for ((dbname, hikari) in properties.hikari) {
            log.info("dbname={}, hikariConfigValue={}", dbname, hikari)
        }

        for ((dbname, hikari) in properties.hikari) {
            dataSourceMap[dbname] = HikariDataSource().apply {
                driverClassName = properties.driverClassName
                jdbcUrl = hikari.jdbcUrl
                username = hikari.username
                password = hikari.password
                poolName = dbname
                maximumPoolSize = hikari.maxPoolSize
                connectionTimeout = hikari.connectionTimeout.toMillis()
                isReadOnly = hikari.replica
                isRegisterMbeans = true
            }
        }

        val router = RoutingDataSource()

        // Default DataSource 는 첫번째 것으로 설정
        dataSourceMap.firstNotNullOf {
            log.info("defaultDataSource={}", it.key)
            router.setDefaultTargetDataSource(it.value)
        }

        router.setTargetDataSources(dataSourceMap)

        return router
    }

    @Primary
    @Bean("entityManagerFactory")
    fun entityManagerFactory(
        @Qualifier("dataSource") dataSource: DataSource,
        builder: EntityManagerFactoryBuilder,
        beanFactory: ConfigurableListableBeanFactory
    ): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(dataSource)
            .packages("io.github.rxcats")
            .properties(emptyMap<String, String>())
            .build().apply {
                this.jpaPropertyMap[AvailableSettings.BEAN_CONTAINER] = SpringBeanContainer(beanFactory)
            }
    }

    @Primary
    @DependsOn("routingDataSource")
    @Bean("dataSource")
    fun dataSource(@Qualifier("routingDataSource") routingDataSource: RoutingDataSource): DataSource {
        return LazyConnectionDataSourceProxy(routingDataSource)
    }

    @Bean
    @ConditionalOnMissingBean
    fun transactionManager(
        @Qualifier("dataSource") dataSource: DataSource,
        @Qualifier("entityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager().apply {
            this.entityManagerFactory = entityManagerFactory
        }
    }

    @Bean
    @ConditionalOnMissingBean
    fun shardKeyHelper(properties: RoutingDataSourceProperties): ShardKeyHelper {
        return CRC32ShardKeyHelper(properties)
    }
}
