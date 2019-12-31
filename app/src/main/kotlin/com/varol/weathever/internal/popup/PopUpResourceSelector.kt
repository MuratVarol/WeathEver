package com.varol.weathever.internal.popup

import androidx.annotation.Keep
import com.varol.weathever.R
import java.io.Serializable

@Keep
sealed class PopUpType : Serializable {
    object Info : PopUpType()
    object Warning : PopUpType()
    object Error : PopUpType()
}

class PopUpResourceSelector {
    companion object {
        private const val WARNING_LOTTIE = "lottie/popup_warning_lottie.json"
        private const val INFORM_LOTTIE = "lottie/popup_inform_lottie.json"
        private const val ERROR_LOTTIE = "lottie/popup_error_lottie.json"

        fun getIcon(popUpType: PopUpType): String {
            return when (popUpType) {
                is PopUpType.Warning -> WARNING_LOTTIE
                is PopUpType.Info -> INFORM_LOTTIE
                is PopUpType.Error -> ERROR_LOTTIE
            }
        }

        fun getBgImage(popUpType: PopUpType): Int {
            return when (popUpType) {
                is PopUpType.Warning -> R.drawable.bg_popup_warning
                is PopUpType.Info -> R.drawable.bg_popup_info
                is PopUpType.Error -> R.drawable.bg_popup_error_round
            }
        }
    }
}