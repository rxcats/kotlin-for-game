package io.github.rxcats.mybatisplus.extensions

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.core.metadata.IPage
import io.github.rxcats.mybatisplus.dsl.KtQueryWrapperDslBuilder
import io.github.rxcats.mybatisplus.dsl.KtUpdateWrapperDslBuilder

inline fun <reified T : Any> BaseMapper<T>.delete(wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit): Int {
    return delete(buildKtQueryWrapper(wrapper))
}

inline fun <reified T : Any> BaseMapper<T>.update(entity: T?, wrapper: KtUpdateWrapperDslBuilder<T>.() -> Unit): Int {
    return update(entity, buildKtUpdateWrapper(wrapper))
}

inline fun <reified T : Any> BaseMapper<T>.selectOne(wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit): T? {
    return selectOne(buildKtQueryWrapper(wrapper))
}

inline fun <reified T : Any> BaseMapper<T>.selectCount(wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit): Long {
    return selectCount(buildKtQueryWrapper(wrapper))
}

inline fun <reified T : Any> BaseMapper<T>.selectCount(): Long {
    return selectCount(null)
}

inline fun <reified T : Any> BaseMapper<T>.selectList(wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit): List<T> {
    return selectList(buildKtQueryWrapper(wrapper))
}

inline fun <reified T : Any> BaseMapper<T>.selectList(): List<T> {
    return selectList(null)
}

inline fun <reified T : Any> BaseMapper<T>.selectMaps(wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit): List<Map<String, Any>> {
    return selectMaps(buildKtQueryWrapper(wrapper))
}

inline fun <reified T : Any> BaseMapper<T>.selectObjs(wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit): List<Any> {
    return selectObjs(buildKtQueryWrapper(wrapper))
}

inline fun <reified T : Any, E : IPage<T>> BaseMapper<T>.selectPage(
    page: E,
    wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit
): E {
    return selectPage(page, buildKtQueryWrapper(wrapper))
}

inline fun <reified T : Any, E : IPage<Map<String, Any>>> BaseMapper<T>.selectMapsPage(
    page: E,
    wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit
): E {
    return selectMapsPage(page, buildKtQueryWrapper(wrapper))
}
