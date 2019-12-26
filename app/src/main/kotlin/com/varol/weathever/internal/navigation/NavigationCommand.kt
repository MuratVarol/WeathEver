package com.varol.weathever.internal.navigation

import androidx.navigation.NavDirections

/**
 * A simple sealed class to handle more properly
 * navigation from a [AndroidViewModel]
 */
sealed class NavigationCommand {
    data class To(val directions: NavDirections) : NavigationCommand()
    object Back : NavigationCommand()
}