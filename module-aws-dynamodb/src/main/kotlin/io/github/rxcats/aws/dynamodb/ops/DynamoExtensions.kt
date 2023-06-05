package io.github.rxcats.aws.dynamodb.ops

import io.github.rxcats.aws.dynamodb.DynamoTableOverrideConfig
import io.github.rxcats.core.spring.ApplicationContextProvider
import io.github.rxcats.core.spring.getBean
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import kotlin.reflect.KClass

fun getTableNameFromAnnotation(type: KClass<*>): String {
    val dynamoDbTable = type.annotations.firstOrNull {
        it is DynamoDbTableMeta
    } as? DynamoDbTableMeta
    return dynamoDbTable?.tableName ?: ""
}

inline fun <reified T> DynamoDbEnhancedClient.tableOf(): DynamoDbTable<T> {
    val prefixTableName = ApplicationContextProvider.getBean<DynamoTableOverrideConfig>().tableNamePrefix
    val type = T::class
    val bean = TableSchema.fromBean(type.java)
    val entityTableName = getTableNameFromAnnotation(type)
    val tableName = "${prefixTableName}${entityTableName}"
    return this.table(tableName, bean) as DynamoDbTable<T>
}

@Suppress("UNCHECKED_CAST")
fun <T> DynamoDbEnhancedClient.tableOf(type: KClass<*>): DynamoDbTable<T> {
    val prefixTableName = ApplicationContextProvider.getBean<DynamoTableOverrideConfig>().tableNamePrefix
    val bean = TableSchema.fromBean(type.java)
    val entityTableName = getTableNameFromAnnotation(type)
    val tableName = "${prefixTableName}${entityTableName}"
    return this.table(tableName, bean) as DynamoDbTable<T>
}
