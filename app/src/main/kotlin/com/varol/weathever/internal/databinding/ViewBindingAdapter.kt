package com.varol.weathever.internal.databinding

import android.content.Context
import android.graphics.Outline
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import android.view.animation.AnimationUtils
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.marginLeft
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.varol.weathever.R
import com.varol.weathever.internal.extension.EMPTY
import com.varol.weathever.internal.extension.hideKeyboard
import com.varol.weathever.internal.extension.toFormattedDate
import com.varol.weathever.internal.extension.toShortDateUiString
import com.varol.weathever.internal.view.CustomTypefaceSpan
import com.varol.weathever.internal.view.RootConstraintLayout
import java.util.*

private const val DRAWABLE_LEFT = 0

@BindingAdapter("visibility", "gone", requireAll = false)
fun View.setVisibility(visible: Boolean, isGone: Boolean = true) {
    visibility = if (visible) View.VISIBLE else {
        if (isGone) View.GONE else View.INVISIBLE
    }
}

@BindingAdapter("hideIfNull")
fun View.hideIfNull(value: Any?) {
    visibility = if (value == null) View.GONE else View.VISIBLE
}

@BindingAdapter("isAtTheTop")
fun View.bringToFront(value: Boolean) {
    if (value)
        ViewCompat.setTranslationZ(this, 100f)
}

@BindingAdapter("enableBlinking")
fun View.blink(value: Boolean) {
    val fadeAnim = AnimationUtils.loadAnimation(context, R.anim.fade_in)
    if (value) {
        startAnimation(fadeAnim)
    } else {
        clearAnimation()
    }
}

@BindingAdapter("setShortDateFormat", requireAll = true)
fun AppCompatTextView.setDateTextView(date: Date?) {
    text = date?.toShortDateUiString()
}

@BindingAdapter("setShortDateFormat", requireAll = true)
fun AppCompatTextView.setDateTextView(timeInMillis: Long?) {
    text = timeInMillis?.toFormattedDate()
}

@BindingAdapter("celsiusText")
fun AppCompatTextView.appendCelsius(celsiusText: String?) {
    celsiusText?.let {
        text = String.format(
            "%s %s", celsiusText, context.getString(R.string.celsius)
                .padStart(0)
        )
            .spanLastWord(context)
    } ?: run {
        text = String.format(
            "%s %s",
            context.getString(R.string.dash),
            context.getString(R.string.celsius)
                .padStart(0)
        ).spanLastWord(context)
    }
}

@BindingAdapter("windText")
fun AppCompatTextView.appendMeterPerSec(windText: String?) {
    windText?.let {
        text =
            String.format(
                "%s %s", windText, context.getString(R.string.meter_per_sec)
                    .padStart(0)
            )
                .spanLastWord(context)
    } ?: run {
        text = String.format(
            "%s %s",
            context.getString(R.string.dash),
            context.getString(R.string.meter_per_sec)
                .padStart(0)
        ).spanLastWord(context)
    }
}

@BindingAdapter("percentText")
fun AppCompatTextView.appendPercent(percentText: String?) {
    percentText?.let {
        text = String.format(
            "%s %s", percentText, context.getString(R.string.percent)
                .padStart(0)
        )
            .spanLastWord(context)
    } ?: run {
        text = String.format(
            "%s %s",
            context.getString(R.string.dash),
            context.getString(R.string.percent)
                .padStart(0)
        ).spanLastWord(context)
    }
}

private fun String.spanLastWord(context: Context): Spanned {
    val span = SpannableString(this)
    val blankIndex = this.indexOfLast { it == ' ' }
    val boldFont: Typeface? = ResourcesCompat.getFont(context, R.font.nunito_extrabold)
    span.setSpan(
        RelativeSizeSpan(0.5f),
        blankIndex,
        this.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    boldFont?.let {
        span.setSpan(
            CustomTypefaceSpan(boldFont),
            blankIndex,
            this.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return span
}


@BindingAdapter("query")
fun AppCompatEditText.onSearchQueryChanged(
    query: String?
) {

    val drawableRes = if (query.isNullOrEmpty()) R.drawable.ic_search else R.drawable.ic_cancel_mono

    setCompoundDrawablesWithIntrinsicBounds(drawableRes, 0, 0, 0)
    setOnTouchListener(object : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (event?.action == MotionEvent.ACTION_UP &&
                event.x <= (compoundDrawables[DRAWABLE_LEFT].bounds.width() + paddingLeft + marginLeft)
            ) {
                setText(String.EMPTY)
                return true
            }
            return false
        }
    })
}


@BindingAdapter("hideKeyboardOnLostFocus")
fun AppCompatEditText.hideKeyboardOnLostFocus(
    closable: Boolean
) {
    if (closable) {
        setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                hideKeyboard()
        }
    }
}

@BindingAdapter("setGoButtonIfQueryNotBlank")
fun AppCompatImageView.setIconResource(
    query: String?
) {
    val drawableRes = if (query.isNullOrEmpty()) R.drawable.ic_location else R.drawable.ic_search
    setImageResource(drawableRes)
}


@BindingAdapter("lottieFile")
fun LottieAnimationView.setLottieFile(resource: String) {
    setAnimation(resource)
}

@BindingAdapter("hideIfNullOrEmpty")
fun View.setVisible(text: CharSequence?) {
    visibility = if (text != null && text.isNotEmpty()) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("curveRadius")
fun curveView(view: View?, curveRadius: Float = 20f) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        view?.outlineProvider =
            object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    view?.let { view ->
                        outline?.setRoundRect(
                            0,
                            0,
                            view.width,
                            (view.height + curveRadius).toInt(),
                            curveRadius
                        )
                    }
                }
            }
        view?.clipToOutline = true
    }
}

@BindingAdapter("progressVisibility")
fun setProgressStatus(rootConstraintLayout: RootConstraintLayout, isVisible: Boolean) {
    if (isVisible) rootConstraintLayout.showProgress() else rootConstraintLayout.hideProgress()
}

@BindingAdapter("drawableResource")
fun AppCompatImageView.setDrawable(@DrawableRes resource: Int) {
    Glide.with(this.context)
        .load(resource)
        .into(this)
}