package io.github.rxcats.mybatisplus.extensions

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.service.IService
import io.github.rxcats.mybatisplus.dsl.KtQueryWrapperDslBuilder
import io.github.rxcats.mybatisplus.dsl.KtUpdateWrapperDslBuilder
import java.util.function.Function

inline fun <reified T : Any> IService<T>.remove(wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit): Boolean {
    return remove(buildKtQueryWrapper(wrapper))
}

inline fun <reified T : Any> IService<T>.update(wrapper: KtUpdateWrapperDslBuilder<T>.() -> Unit): Boolean {
    return update(buildKtUpdateWrapper(wrapper))
}

inline fun <reified T : Any> IService<T>.update(entity: T, wrapper: KtUpdateWrapperDslBuilder<T>.() -> Unit): Boolean {
    return update(entity, buildKtUpdateWrapper(wrapper))
}

inline fun <reified T : Any> IService<T>.getOne(wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit): T? {
    return getOne(buildKtQueryWrapper(wrapper), true)
}

inline fun <reified T : Any> IService<T>.getOne(wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit, throwEx: Boolean): T? {
    return getOne(buildKtQueryWrapper(wrapper), throwEx)
}

inline fun <reified T : Any> IService<T>.getMap(wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit): Map<String, Any>? {
    return getMap(buildKtQueryWrapper(wrapper))
}

inline fun <reified T : Any, V> IService<T>.getObj(
    wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit,
    mapper: Function<in Any?, V>?
): V {
    return getObj(buildKtQueryWrapper(wrapper), mapper)
}

inline fun <reified T : Any> IService<T>.count(wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit): Long {
    return count(buildKtQueryWrapper(wrapper))
}

inline fun <reified T : Any> IService<T>.list(wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit): MutableList<T>? {
    return list(buildKtQueryWrapper(wrapper))
}

inline fun <reified T : Any, E : IPage<T>> IService<T>.page(
    page: E,
    wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit
): E {
    return page(page, buildKtQueryWrapper(wrapper))
}

inline fun <reified T : Any> IService<T>.listMaps(wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit): MutableList<MutableMap<String, Any>>? {
    return listMaps(buildKtQueryWrapper(wrapper))
}

inline fun <reified T : Any> IService<T>.listObjs(wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit): MutableList<Any>? {
    return listObjs(buildKtQueryWrapper(wrapper))
}

inline fun <reified T : Any, V> IService<T>.listObjs(
    wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit,
    mapper: Function<in Any?, V>
): List<V> {
    return listObjs(buildKtQueryWrapper(wrapper), mapper)
}

inline fun <reified T : Any, E : IPage<Map<String, Any>>> IService<T>.pageMaps(
    page: E,
    wrapper: KtQueryWrapperDslBuilder<T>.() -> Unit
): E {
    return pageMaps(page, buildKtQueryWrapper(wrapper))
}
