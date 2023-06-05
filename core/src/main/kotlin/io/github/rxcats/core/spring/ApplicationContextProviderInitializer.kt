package io.github.rxcats.core.spring

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class ApplicationContextProviderInitializer : ApplicationContextAware {
    override fun setApplicationContext(ctx: ApplicationContext) {
        ApplicationContextProvider.setApplicationContext(ctx)
    }
}
