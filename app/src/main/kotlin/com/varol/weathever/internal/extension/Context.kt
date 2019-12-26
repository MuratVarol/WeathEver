package com.varol.weathever.internal.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast


fun Context?.getActivity(): Activity? {
    return when (this) {
        is Activity -> return this
        is ContextWrapper -> return this.baseContext.getActivity()
        else -> null
    }
}

fun Context?.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

val Context.networkInfo: NetworkInfo?
    get() = (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
