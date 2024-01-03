package io.github.rxcats.database.component

import com.mybatisflex.core.datasource.DataSourceKey
import com.mybatisflex.core.row.Db

object RoutingQuery {
    operator fun <T> invoke(db: String, useTransaction: Boolean = false, callback: () -> T): T {
        try {
            DataSourceKey.use(db)
            return if (useTransaction) {
                Db.txWithResult { callback() }
            } else {
                return callback()
            }
        } finally {
            DataSourceKey.clear()
        }
    }

}
