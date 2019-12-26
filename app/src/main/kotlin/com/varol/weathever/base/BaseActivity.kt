package com.varol.weathever.base

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.borusan.mannesmann.pipeline.internal.util.functional.lazyThreadSafetyNone
import dagger.android.support.DaggerAppCompatActivity
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseActivity<VM : BaseAndroidViewModel> : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Suppress("UNCHECKED_CAST")
    protected val viewModel by lazyThreadSafetyNone {
        val persistentViewModelClass = (javaClass.genericSuperclass as ParameterizedType)
                .actualTypeArguments.first() as Class<VM>
        return@lazyThreadSafetyNone ViewModelProviders.of(this, viewModelFactory)
                .get(persistentViewModelClass)
    }
}