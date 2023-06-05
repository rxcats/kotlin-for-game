package io.github.rxcats.core.spring

import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext

object ApplicationContextProvider {
    private lateinit var ctx: ApplicationContext

    fun setApplicationContext(ctx: ApplicationContext) {
        this.ctx = ctx
    }

    operator fun invoke() = ctx
}

inline fun <reified T : Any> ApplicationContextProvider.getBean(): T = this().getBean()
