package io.github.rxcats.mybatisplus.dsl

import com.baomidou.mybatisplus.extension.kotlin.AbstractKtWrapper
import kotlin.reflect.KProperty

abstract class AbstractKtWrapperDslBuilder<T, W : AbstractKtWrapper<T, W>, Children : AbstractKtWrapperDslBuilder<T, W, Children>>(
    baseWrapper: W
) : AbstractWrapperDslBuilder<T, KProperty<*>, W, Children>(baseWrapper)
