package io.github.rxcats.mybatisplus.dsl

import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper

class KtUpdateWrapperDslBuilder<T : Any>(baseWrapper: KtUpdateWrapper<T>) :
    AbstractKtWrapperDslBuilder<T, KtUpdateWrapper<T>, KtUpdateWrapperDslBuilder<T>>(
        baseWrapper
    ) {
    override fun newInstance(baseWrapper: KtUpdateWrapper<T>): KtUpdateWrapperDslBuilder<T> =
        KtUpdateWrapperDslBuilder(baseWrapper)

    infix fun set(sql: String) {
        baseWrapper.setSql(sql)
    }
}
