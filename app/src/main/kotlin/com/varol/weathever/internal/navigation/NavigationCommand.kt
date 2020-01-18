package com.varol.weathever.internal.navigation

import androidx.navigation.NavDirections
import com.varol.weathever.internal.popup.PopupCallback
import com.varol.weathever.internal.popup.PopupUiModel

/**
 * A simple sealed class to handle more properly
 * navigation from a [AndroidViewModel]
 */
sealed class NavigationCommand {
    data class To(val directions: NavDirections) : NavigationCommand()
    object Back : NavigationCommand()
    data class Popup(val model: PopupUiModel, val callback: PopupCallback? = null) :
        NavigationCommand()
}