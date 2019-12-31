package com.varol.weathever.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.varol.weathever.internal.navigation.NavigationCommand
import com.varol.weathever.internal.util.Event
import com.varol.weathever.internal.util.Failure
import io.reactivex.disposables.CompositeDisposable

abstract class BaseAndroidViewModel : AndroidViewModel {

    protected val context: Context

    constructor(context: Context) : super(context.applicationContext as Application) {
        this.context = context.applicationContext as Application
    }

    private val _failure = MutableLiveData<Event<Failure>>()
    var failure = _failure

    private val _success = MutableLiveData<Event<String>>()
    var success = _success

    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> = _navigation

    fun navigate(directions: NavDirections) {
        _navigation.value = Event(NavigationCommand.To(directions))
    }

    fun navigateBack() {
        _navigation.value = Event(NavigationCommand.Back)
    }

    protected open fun handleFailure(failure: Failure) {
        this._failure.value = Event(failure)
    }

    protected val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
