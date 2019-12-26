package com.varol.weathever.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.borusan.mannesmann.pipeline.internal.util.functional.lazyThreadSafetyNone
import com.varol.weathever.BR
import com.varol.weathever.internal.navigation.NavigationCommand
import dagger.android.support.DaggerFragment
import java.lang.reflect.ParameterizedType
import javax.inject.Inject


abstract class BaseFragment<VM : BaseAndroidViewModel, B : androidx.databinding.ViewDataBinding> :
    DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var binder: B
    @get:LayoutRes
    abstract val layoutId: Int

    open fun initialize() {}

    @Suppress("UNCHECKED_CAST")
    protected open val viewModel by lazyThreadSafetyNone {
        val persistentViewModelClass = (javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments.first() as Class<VM>
        return@lazyThreadSafetyNone ViewModelProviders.of(this, viewModelFactory)
            .get(persistentViewModelClass)
    }

    protected inline fun <reified VM : ViewModel> viewModels(): Lazy<VM> {
        return viewModels { viewModelFactory }
    }


    protected inline fun <reified VM : ViewModel> navGraphViewModels(@IdRes navGraphId: Int): Lazy<VM> {
        return navGraphViewModels(navGraphId) { viewModelFactory }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binder.lifecycleOwner = viewLifecycleOwner
        binder.setVariable(BR.viewModel, viewModel)
        initialize()

        return binder.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeNavigation()
    }

    private fun observeNavigation() {
        viewModel.navigation.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { command ->
                handleNavigation(command)
            }
        })
    }

    protected open fun handleNavigation(command: NavigationCommand) {
        when (command) {
            is NavigationCommand.To -> {
                with(command) {
                    directions.let {
                        findNavController().navigate(it, getExtras())
                    }
                }
            }
            is NavigationCommand.Back -> findNavController().navigateUp()
        }
    }

    open fun getExtras(): FragmentNavigator.Extras = FragmentNavigatorExtras()

}