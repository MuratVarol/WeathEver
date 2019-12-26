package com.varol.weathever.internal.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> MutableLiveData<T>.notifyDataChange() {
    this.value = this.value
}

fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, Observer {
        it?.let(observer)
    })
}

operator fun <T> MutableLiveData<MutableList<T>>.plusAssign(values: List<T>) {
    val value = this.value ?: mutableListOf()
    value.addAll(values)
    this.value = value
}

operator fun <T> MutableLiveData<MutableList<T>>.plusAssign(item: T) {
    val value = this.value ?: mutableListOf()
    value.add(item)
    this.value = value
}

fun <T> MutableLiveData<MutableList<T>>.removeItem(item: T) {
    val value = this.value ?: mutableListOf()
    value.remove(item)
    this.value = value
}
