package com.varol.weathever.internal.popup

import android.text.Spanned
import androidx.annotation.Keep
import com.varol.weathever.internal.extension.toSpanned
import java.io.Serializable

@Keep
open class PopupUiModel(
    var title: String? = null,
    var message: String? = null,
    var editableHint: String? = null,
    var editableText: String? = null,
    var bgResourceId: Int = 0,
    var cancelable: Boolean = false,
    val addCancelButton: Boolean = false,
    var cancelButtonText: String? = null,
    var confirmButtonText: String? = null,
    val popUpType: PopUpType = PopUpType.Warning
) : Serializable {
    val bgResource: Int
        get() = if (bgResourceId == 0) PopUpResourceSelector.getBgImage(popUpType) else bgResourceId

    val iconResource: String
        get() = PopUpResourceSelector.getIcon(popUpType)

    val styledMessage: Spanned?
        get() = message?.toSpanned()
}

// TODO: WAITING KOTLIN 1.4 FOR SAM CONVERSIONS
@Keep
interface PopupCallback : Serializable {
    fun onConfirmClick() {}
    fun onConfirmClick(editableValue: String) {}
    fun onCancelClick() {}
    fun onDismiss() {}
}