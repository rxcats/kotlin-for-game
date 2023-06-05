package io.github.rxcats.mybatisplus.dsl

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper

@Suppress("UNUSED", "UNCHECKED_CAST", "UNUSED_PARAMETER")
abstract class AbstractWrapperDslBuilder<T, C, W : AbstractWrapper<T, C, W>, Children : AbstractWrapperDslBuilder<T, C, W, Children>>
    (val baseWrapper: W) {

    @Suppress("LeakingThis")
    private val typedThis = this as Children

    infix fun C.eq(value: Any?): Children =
        typedThis.apply { baseWrapper.eq(this@eq, value) }

    infix fun C.eqIfNotNull(value: Any?): Children =
        typedThis.apply { baseWrapper.eq(value != null, this@eqIfNotNull, value) }

    infix fun C.ne(value: Any?): Children =
        typedThis.apply { baseWrapper.ne(this@ne, value) }

    infix fun C.gt(value: Any?): Children =
        typedThis.apply { baseWrapper.gt(this@gt, value) }

    infix fun C.ge(value: Any?): Children =
        typedThis.apply { baseWrapper.ge(this@ge, value) }

    infix fun C.lt(value: Any?): Children =
        typedThis.apply { baseWrapper.lt(this@lt, value) }

    infix fun C.le(value: Any?): Children =
        typedThis.apply { baseWrapper.le(this@le, value) }

    infix fun C.like(value: Any?): Children =
        typedThis.apply { baseWrapper.like(this@like, value) }

    infix fun C.notLike(value: Any?): Children =
        typedThis.apply { baseWrapper.notLike(this@notLike, value) }

    infix fun C.likeLeft(value: Any?): Children =
        typedThis.apply { baseWrapper.likeLeft(this@likeLeft, value) }

    infix fun C.likeRight(value: Any?): Children =
        typedThis.apply { baseWrapper.likeRight(this@likeRight, value) }

    infix fun <T : Any?, R : Any?> C.between(valuePair: Pair<T, R>): Children =
        typedThis.apply { baseWrapper.between(this@between, valuePair.first, valuePair.second) }

    infix fun <T : Any?, R : Any?> C.notBetween(valuePair: Pair<T, R>): Children =
        typedThis.apply { baseWrapper.notBetween(this@notBetween, valuePair.first, valuePair.second) }

    infix fun C.inValues(collection: Collection<*>): Children =
        typedThis.apply { baseWrapper.`in`(this@inValues, collection) }

    infix fun C.inValues(sql: String): Children =
        typedThis.apply { baseWrapper.inSql(this@inValues, sql) }

    infix fun C.notIn(collection: Collection<*>): Children =
        typedThis.apply { baseWrapper.notIn(this@notIn, collection) }

    infix fun C.notIn(sql: String): Children =
        typedThis.apply { baseWrapper.notInSql(this@notIn, sql) }

    @Suppress("EXTENSION_SHADOWED_BY_MEMBER")
    operator fun Collection<*>.contains(column: C): Boolean = false.also { column inValues this@contains }
    operator fun String.contains(column: C): Boolean = false.also { column inValues this@contains }

    fun groupBy(vararg columns: C): Children =
        typedThis.apply { baseWrapper.groupBy(columns.toList()) }

    infix fun groupBy(column: C): Children =
        typedThis.apply { baseWrapper.groupBy(column) }

    infix fun exists(sql: String) =
        typedThis.apply { baseWrapper.exists(sql) }

    infix fun notExists(sql: String): Children =
        typedThis.apply { baseWrapper.notExists(sql) }

    fun having(sql: String, vararg params: Any): Children =
        typedThis.apply { baseWrapper.having(sql, *params) }

    fun orderBy(isAsc: Boolean, vararg columns: C): Children =
        typedThis.apply { baseWrapper.orderBy(true, isAsc, columns.toList()) }

    fun orderByAsc(vararg columns: C): Children = orderBy(true, *columns)

    fun orderByDesc(vararg columns: C): Children = orderBy(false, *columns)

    fun sql(sql: String, vararg values: Any?): Children =
        typedThis.apply { baseWrapper.apply(sql, *values) }

    infix fun sql(sql: String): Children =
        typedThis.apply { baseWrapper.apply(sql) }

    infix fun sql(builderAction: StringBuilder.() -> Unit): Children =
        typedThis.apply { sql(buildString(builderAction)) }

    operator fun String.unaryMinus(): Children = sql(this)
    operator fun (StringBuilder.() -> Unit).unaryMinus(): Children = sql(this)
    operator fun invoke(sql: String, vararg values: Any?): Children = sql(sql, *values)

    infix fun first(sql: String): Children =
        typedThis.apply { baseWrapper.first(sql) }

    infix fun last(sql: String): Children =
        typedThis.apply { baseWrapper.last(sql) }

    infix fun limit(limit: Int): Children = last("LIMIT $limit")

    infix fun comment(right: String): Children =
        typedThis.apply { baseWrapper.comment(right) }

    infix fun and(consumer: Children.() -> Unit): Children =
        typedThis.apply {
            baseWrapper.and(true) { consumer(newInstance(it)) }
        }

    infix fun and(right: Children): Children = typedThis

    infix fun Boolean.and(right: Children): Children = typedThis

    infix fun Children.and(right: String): Children = typedThis

    infix fun (Children.() -> Unit).and(consumer: Children.() -> Unit): Children =
        typedThis.nested(this@and).apply {
            baseWrapper.and(true) { consumer(newInstance(it)) }
        }

    fun and(): Children = typedThis
    val and get(): Children = typedThis

    infix fun or(consumer: Children.() -> Unit): Children = typedThis.apply {
        baseWrapper.or(true) { consumer(newInstance(it)) }
    }

    infix fun (Children.() -> Unit).or(consumer: Children.() -> Unit): Children =
        typedThis.nested(this@or).apply {
            baseWrapper.or(true) { consumer(newInstance(it)) }
        }

    fun or(): Children = typedThis.apply { baseWrapper.or(true) }
    val or get(): Children = or()

    infix fun nested(consumer: Children.() -> Unit): Children = typedThis.apply {
        baseWrapper.nested(true) { consumer(newInstance(it)) }
    }

    operator fun (Children.() -> Unit).not(): Children = nested(this)


    protected abstract fun newInstance(baseWrapper: W): Children
}
