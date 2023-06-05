package io.github.rxcats.aws.dynamodb.ops

import io.github.rxcats.aws.dynamodb.DynamoDefine
import io.github.rxcats.core.loggerK
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.model.TransactGetItemsEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest

class DynamoTransactionOperations(
    private val enhancedClient: DynamoDbEnhancedClient
) {
    companion object {
        private val log by loggerK
    }

    private fun tranLoadRequestBuild(targets: List<TranLoadItem<*>>): Pair<TransactGetItemsEnhancedRequest.Builder, List<DynamoDbTable<Any>>> {
        val request = TransactGetItemsEnhancedRequest.builder()

        val requestTables = mutableListOf<DynamoDbTable<Any>>()

        for (target in targets) {
            val kClass = target.item::class
            val table = enhancedClient.tableOf<Any>(kClass)
            val key = table.keyFrom(target.item)
            request.addGetItem(table, key)
            requestTables.add(table)
        }

        return Pair(request, requestTables)
    }

    private fun tranLoadItems(targets: List<TranLoadItem<*>>): List<Any?> {
        val (request, requestTables) = tranLoadRequestBuild(targets)

        val result = enhancedClient.transactGetItems(request.build())

        return result.mapIndexed { index, document ->
            document.getItem(requestTables[index])
        }
    }

    fun transactionLoad(targets: List<TranLoadItem<*>>): TranLoadResult {
        if (targets.isEmpty()) return TranLoadResult()

        return if (targets.size > DynamoDefine.TRAN_GET_MAX_ITEM.count) {
            val result = mutableListOf<Any?>()
            targets.chunked(DynamoDefine.TRAN_GET_MAX_ITEM.count).forEach {
                result.addAll(tranLoadItems(it))
            }
            TranLoadResult(result)
        } else {
            TranLoadResult(tranLoadItems(targets))
        }
    }

    private fun buildTranWriteRequest(targets: List<TranWriteItem<*>>): TransactWriteItemsEnhancedRequest {
        val request = TransactWriteItemsEnhancedRequest.builder()

        for (target in targets) {
            val kClass = target.item::class
            val table = enhancedClient.tableOf<Any>(kClass)

            when (target.mode) {
                TranWriteMode.UPDATE -> request.addUpdateItem(table, target.item)
                TranWriteMode.DELETE -> request.addDeleteItem(table, target.item)
            }
        }

        return request.build()
    }

    private fun tranWriteItems(targets: List<TranWriteItem<*>>) {
        enhancedClient.transactWriteItems(buildTranWriteRequest(targets))
    }

    fun transactionWrite(targets: List<TranWriteItem<*>>) {
        if (targets.isEmpty()) return

        if (targets.size > DynamoDefine.TRAN_WRITE_MAX_ITEM.count) {
            targets.chunked(DynamoDefine.TRAN_WRITE_MAX_ITEM.count) {
                tranWriteItems(it)
            }
        } else {
            tranWriteItems(targets)
        }
    }

}
