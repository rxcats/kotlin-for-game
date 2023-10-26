package io.github.rxcats

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories
@EnableJpaAuditing
@EnableAutoConfiguration
@Configuration(proxyBeanMethods = false)
class JpaConfig
