package io.github.rxcats.mybatisplus.extensions

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import io.github.rxcats.mybatisplus.dsl.KtQueryWrapperDslBuilder
import io.github.rxcats.mybatisplus.dsl.KtUpdateWrapperDslBuilder

inline fun <reified T : Any> buildKtQueryWrapper(builder: KtQueryWrapperDslBuilder<T>.() -> Unit): KtQueryWrapper<T> {
    return KtQueryWrapperDslBuilder(KtQueryWrapper(T::class.java)).apply(builder).baseWrapper
}

inline fun <reified T : Any> buildKtUpdateWrapper(builder: KtUpdateWrapperDslBuilder<T>.() -> Unit): KtUpdateWrapper<T> {
    return KtUpdateWrapperDslBuilder(KtUpdateWrapper(T::class.java)).apply(builder).baseWrapper
}
