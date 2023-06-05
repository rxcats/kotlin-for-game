package io.github.rxcats.aws.dynamodb

enum class DynamoDefine(val count: Int) {
    BATCH_GET_MAX_ITEM(100),
    BATCH_WRITE_MAX_ITEM(25),

    TRAN_GET_MAX_ITEM(100),
    TRAN_WRITE_MAX_ITEM(100),
    ;
}
