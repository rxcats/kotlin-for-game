package io.github.rxcats.jpa.config

import io.github.rxcats.core.loggerK
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.DefaultTransactionDefinition

@JvmInline
value class RoutingDataSourceTransactionTemplate(
    private val transactionManager: PlatformTransactionManager
) {

    companion object {
        private val log by loggerK
    }

    fun start(readOnly: Boolean = false): TransactionStatus {
        log.info("start transaction")
        val def = DefaultTransactionDefinition().apply {
            isReadOnly = readOnly
        }
        return transactionManager.getTransaction(def)
    }

    fun commit(status: TransactionStatus) {
        if (!status.isCompleted) {
            log.info("commit transaction")
            transactionManager.commit(status)
        }
    }

    fun rollback(status: TransactionStatus) {
        if (!status.isCompleted) {
            log.info("rollback transaction")
            transactionManager.rollback(status)
        }
    }
}
