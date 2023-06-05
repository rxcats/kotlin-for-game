package io.github.rxcats.aws.dynamodb.ops

import io.github.rxcats.aws.dynamodb.DynamoDefine
import io.github.rxcats.core.spring.ApplicationContextProvider
import io.github.rxcats.core.spring.getBean
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.DescribeTableEnhancedResponse
import software.amazon.awssdk.enhanced.dynamodb.model.Page
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import kotlin.reflect.KClass

abstract class DynamoOperations<T : Any>(
    protected val entityType: KClass<T>
) {
    protected val enhancedClient: DynamoDbEnhancedClient

    protected val table: DynamoDbTable<T>

    init {
        this.enhancedClient = ApplicationContextProvider.getBean()
        this.table = this.enhancedClient.tableOf(entityType)
    }

    open fun describeTable(): Result<DescribeTableEnhancedResponse> = runCatching {
        table.describeTable()
    }

    open fun createTableIfNotExists() {
        this.describeTable().onFailure {
            table.createTable()
        }
    }

    open fun deleteTable() {
        this.describeTable().onSuccess {
            table.deleteTable()
        }
    }

    open fun findByKey(key: Key): T? {
        return table.getItem(key)
    }

    open fun find(entity: T): T? {
        return table.getItem(entity)
    }

    open fun findByKeys(keys: List<Key>): List<T> {
        if (keys.isEmpty()) return emptyList()

        val merged = mutableListOf<T>()

        keys.chunked(DynamoDefine.BATCH_GET_MAX_ITEM.count) { chunked ->
            val builder = ReadBatch.builder(entityType.java)
                .mappedTableResource(table)

            chunked.forEach {
                builder.addGetItem(it)
            }

            val result = enhancedClient.batchGetItem { req ->
                req.readBatches(builder.build())
            }

            val list = result.resultsForTable(table)
                .stream()
                .toList()

            merged.addAll(list)
        }

        return merged
    }

    open fun findAllByKey(key: Key): List<T> {
        val result = table.query { req ->
            req.queryConditional(QueryConditional.sortBeginsWith(key))
        }

        return result.items().toList()
    }

    private fun pageable(
        requestBuilder: QueryEnhancedRequest.Builder,
        indexName: String? = null,
        lastEvaluatedKey: Map<String, AttributeValue>? = null
    ): Page<T> {
        if (!lastEvaluatedKey.isNullOrEmpty()) {
            requestBuilder.exclusiveStartKey(lastEvaluatedKey)
        }

        return if (indexName == null) {
            table.query(requestBuilder.build())
        } else {
            table
                .index(indexName)
                .query(requestBuilder.build())
        }.stream()
            .findFirst()
            .get()
    }

    private fun <R> pageableWithTable(
        table: DynamoDbTable<R>,
        requestBuilder: QueryEnhancedRequest.Builder,
        indexName: String? = null,
        lastEvaluatedKey: Map<String, AttributeValue>? = null
    ): Page<R> {
        if (!lastEvaluatedKey.isNullOrEmpty()) {
            requestBuilder.exclusiveStartKey(lastEvaluatedKey)
        }

        return if (indexName == null) {
            table.query(requestBuilder.build())
        } else {
            table
                .index(indexName)
                .query(requestBuilder.build())
        }.stream()
            .findFirst()
            .get()
    }

    private fun queryConditionalBeginsWith(key: Key, scanIndexForward: Boolean = false, limit: Int = 10): QueryEnhancedRequest.Builder {
        return QueryEnhancedRequest.builder()
            .queryConditional(QueryConditional.sortBeginsWith(key))
            .limit(limit)
            .scanIndexForward(scanIndexForward)
    }

    open fun findByKeyLimit(key: Key, limit: Int = 10): List<T> {
        val request = queryConditionalBeginsWith(key, false, limit)

        val result = pageable(request)

        return result.items()
    }

    open fun <R> findByKeyLimitWithTable(table: DynamoDbTable<R>, key: Key, limit: Int = 10): List<R> {
        val request = queryConditionalBeginsWith(key, false, limit)

        val result = pageableWithTable(table, request)

        return result.items()
    }

    open fun findByKeyPageable(key: Key, limit: Int = 10, lastEvaluatedKey: Map<String, AttributeValue>? = null): Page<T> {
        val request = queryConditionalBeginsWith(key, false, limit)
        return pageable(request, null, lastEvaluatedKey)
    }

    open fun findByQueryConditionalPageable(
        queryConditional: QueryConditional,
        indexName: String? = null,
        scanIndexForward: Boolean = false,
        limit: Int = 10,
        lastEvaluatedKey: Map<String, AttributeValue>? = null
    ): Page<T> {
        val request = QueryEnhancedRequest.builder()
            .queryConditional(queryConditional)
            .limit(limit)
            .scanIndexForward(scanIndexForward)

        return pageable(request, indexName, lastEvaluatedKey)
    }

    open fun save(entity: T): T {
        return table.updateItem(entity)
    }

    open fun saveAll(entities: List<T>) {
        if (entities.isEmpty()) return

        entities.map {
            WriteBatch.builder(it::class.java)
                .mappedTableResource(table)
                .addPutItem(it)
                .build()
        }.chunked(DynamoDefine.BATCH_WRITE_MAX_ITEM.count) { chunked ->
            enhancedClient.batchWriteItem { req -> req.writeBatches(chunked) }
        }
    }

    open fun deleteByKey(key: Key) {
        table.deleteItem(key)
    }

    open fun delete(entity: T) {
        table.deleteItem(entity)
    }

    open fun deleteAll(entities: List<T>) {
        if (entities.isEmpty()) return

        entities.map {
            WriteBatch.builder(it::class.java)
                .mappedTableResource(table)
                .addDeleteItem(it)
                .build()
        }.chunked(DynamoDefine.BATCH_WRITE_MAX_ITEM.count) { chunked ->
            enhancedClient.batchWriteItem { req -> req.writeBatches(chunked) }
        }
    }

}
