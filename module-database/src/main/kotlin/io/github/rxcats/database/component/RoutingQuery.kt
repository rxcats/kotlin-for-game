package io.github.rxcats.database.component

import io.github.rxcats.core.spring.ApplicationContextProvider
import io.github.rxcats.core.spring.getBean
import io.github.rxcats.database.config.RoutingDataSourceContextHolder
import io.github.rxcats.database.config.RoutingDataSourceTransactionTemplate
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus

inline fun <T> PlatformTransactionManager.transaction(readOnly: Boolean = false, callback: () -> T?): T? {
    val transactionTemplate = RoutingDataSourceTransactionTemplate(this)
    var transactionStatus: TransactionStatus? = null

    try {
        transactionStatus = transactionTemplate.start(readOnly)
        val result = callback()
        transactionTemplate.commit(transactionStatus)
        return result
    } catch (e: Exception) {
        transactionStatus?.let { ts ->
            transactionTemplate.rollback(ts)
        }
        throw e
    }
}

object RoutingQuery {
    inline operator fun <T> invoke(db: String, callback: () -> T?): T? {
        try {
            RoutingDataSourceContextHolder.set(db)
            return callback()
        } finally {
            RoutingDataSourceContextHolder.clear()
        }
    }

    inline fun <T> transaction(db: String, readOnly: Boolean = false, callback: () -> T?): T? {
        val transactionManager = ApplicationContextProvider.getBean<PlatformTransactionManager>()

        try {
            RoutingDataSourceContextHolder.set(db)
            return transactionManager.transaction(readOnly, callback)
        } finally {
            RoutingDataSourceContextHolder.clear()
        }
    }
}
