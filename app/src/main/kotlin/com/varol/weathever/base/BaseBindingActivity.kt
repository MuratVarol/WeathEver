package com.varol.weathever.base

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import com.borusan.mannesmann.pipeline.internal.util.functional.lazyThreadSafetyNone

abstract class BaseBindingActivity<VM : BaseAndroidViewModel, B : androidx.databinding.ViewDataBinding> :
    BaseActivity<VM>() {

    protected val binder by lazyThreadSafetyNone<B> {
        DataBindingUtil.setContentView(this, layoutId)
    }

    @get:LayoutRes
    abstract val layoutId: Int
}