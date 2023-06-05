package io.github.rxcats.mybatisplus.dsl

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import java.util.function.Predicate
import kotlin.reflect.KProperty

class KtQueryWrapperDslBuilder<T : Any>(baseWrapper: KtQueryWrapper<T>) :
    AbstractKtWrapperDslBuilder<T, KtQueryWrapper<T>, KtQueryWrapperDslBuilder<T>>(
        baseWrapper
    ) {
    override fun newInstance(baseWrapper: KtQueryWrapper<T>): KtQueryWrapperDslBuilder<T> =
        KtQueryWrapperDslBuilder(baseWrapper)

    fun select(vararg columns: KProperty<*>) = apply { baseWrapper.select(*columns) }
    fun select(entityClass: Class<T>, predicate: Predicate<TableFieldInfo>) =
        apply { baseWrapper.select(entityClass, predicate) }

    fun select(predicate: Predicate<TableFieldInfo>) = apply { baseWrapper.select(predicate) }
}
