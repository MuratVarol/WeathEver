package com.varol.weathever.internal.listeners

import android.view.View

interface ItemClickListener<T> {
    fun onItemClick(view: View, item: T, position: Int)
}