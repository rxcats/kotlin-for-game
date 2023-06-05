package io.github.rxcats.database.aop

import io.github.rxcats.database.type.DbType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TargetDatabase(
    val db: DbType = DbType.COMMON,
    val useTransaction: Boolean = false,
    val readOnly: Boolean = false,
)

/**
 * DB 샤드 선택시 Hash 알고리즘을 이용할 경우에 사용
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ShardHashKey

/**
 * DB 샤드 선택시 샤드 번호를 지정하여 이용할 경우 사용
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ShardNo
