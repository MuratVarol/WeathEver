package com.varol.weathever.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
import com.varol.weathever.R
import com.varol.weathever.internal.extension.observeNonNull
import com.varol.weathever.internal.extension.showPopup
import com.varol.weathever.internal.navigation.NavigationCommand
import com.varol.weathever.internal.popup.PopUpType
import com.varol.weathever.internal.popup.PopupUiModel
import com.varol.weathever.internal.util.Failure
import com.varol.weathever.internal.view.informbar.InformBar
import com.varol.weathever.internal.view.informbar.InformBarModel
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binder = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binder.lifecycleOwner = viewLifecycleOwner
        binder.setVariable(BR.viewModel, viewModel)
        initialize()

        return binder.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeNavigation()
        observeFailure()
        observeInform()
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
            is NavigationCommand.Popup -> {
                with(command) {
                    showPopup(model, callback)
                }
            }
            is NavigationCommand.Back -> findNavController().navigateUp()
        }
    }

    private fun observeFailure() {
        viewModel.failure.observeNonNull(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { failure -> handleFailure(failure) }
        }
    }

    private fun observeInform() {
        viewModel.inform.observeNonNull(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { informBarModel ->
                showInformBarMessage(informBarModel)
            }
        }
    }

    fun startInstalledAppDetailsActivity() {
        val startSettingsIntent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            addCategory(Intent.CATEGORY_DEFAULT)
            data = Uri.parse("package: ${context?.applicationContext?.packageName}")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }
        activity?.startActivity(startSettingsIntent)
    }

    protected fun handleFailure(failure: Failure) {
        val (title, message) = when (failure) {
            is Failure.NoConnectivityError ->
                Pair("", getString(R.string.error_message_network_connection))
            is Failure.UnknownHostError ->
                Pair("", getString(R.string.error_message_unknown_host))
            is Failure.ServerError ->
                Pair("", failure.message)
            is Failure.ParsingDataError,
            is Failure.EmptyResponse ->
                Pair("", getString(R.string.error_message_invalid_response))
            is Failure.UnknownError ->
                Pair("", failure.message ?: getString(R.string.error_unknown))
            is Failure.HttpError ->
                Pair("", getString(R.string.error_message_http, failure.code.toString()))
            is Failure.TimeOutError ->
                Pair("", getString(R.string.error_message_timeout))
            else ->
                Pair("", failure.message ?: failure.toString())
        }

        showPopup(
            PopupUiModel(
                title = title,
                message = message,
                popUpType = PopUpType.Error
            ), null
        )
    }


    private fun showInformBarMessage(informBarModel: InformBarModel) {
        view?.let {
            with(informBarModel) {
                InformBar.make(it, message, informType, duration).show()
            }
        }
    }

    open fun getExtras(): FragmentNavigator.Extras = FragmentNavigatorExtras()

}