package com.varol.weathever.internal.databinding

import android.text.Spanned
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.varol.weathever.internal.extension.toShortDateUiString
import java.util.*


@BindingAdapter("visibility", "gone", requireAll = false)
fun setVisibility(view: View, visible: Boolean, isGone: Boolean = true) {
    view.visibility = if (visible) View.VISIBLE else {
        if (isGone) View.GONE else View.INVISIBLE
    }
}

@BindingAdapter("hideIfNull")
fun setVisible(view: View, value: Any?) {
    view.visibility = if (value == null) View.GONE else View.VISIBLE
}

@BindingAdapter("setShortDateFormat", requireAll = true)
fun setDateTextView(textView: TextView, date: Date?) {
    textView.text = date?.toShortDateUiString()
}

@BindingAdapter("lottieFile")
fun setLottieFile(view: LottieAnimationView, resource: String) {
    view.setAnimation(resource)
}

@BindingAdapter("hideIfNullOrEmpty")
fun setVisible(view: View, text: CharSequence?) {
    view.visibility = if (text != null && text.isNotEmpty()) {
        View.VISIBLE
    } else {
        View.GONE
    }
}