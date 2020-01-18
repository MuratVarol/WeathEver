package com.varol.weathever.internal.listeners

import java.io.Serializable

interface ToolbarListener : Serializable {
    fun onRightIconClicked() {}
    fun onLeftIconClicked() {}
}