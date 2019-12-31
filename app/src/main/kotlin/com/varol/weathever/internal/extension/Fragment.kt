package com.varol.weathever.internal.extension

import androidx.fragment.app.Fragment
import com.varol.weathever.internal.popup.Popup
import com.varol.weathever.internal.popup.PopupCallback
import com.varol.weathever.internal.popup.PopupUiModel

fun Fragment.showPopup(uiModel: PopupUiModel, callback: PopupCallback? = null) {
    this.activity?.let {
        Popup(it, uiModel, callback).show()
    }
}