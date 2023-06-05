package io.github.rxcats.aws.dynamodb.ops

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class DynamoDbTableMeta(
    val tableName: String = ""
)
