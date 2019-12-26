package com.varol.weathever.internal.util

import android.content.Context
import com.varol.weathever.internal.extension.networkInfo
import javax.inject.Singleton

@Singleton
class NetworkHandler(private val context: Context) {
    val isConnected: Boolean
        get() = context.networkInfo?.isConnected ?: false
}