package com.varol.weathever.internal.view.informbar

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.ContentViewCallback
import com.varol.weathever.R

const val SHORT_ANIMATION = 500L
const val LONG_ANIMATION = 1000L

const val SHORT_DURATION = 3000
const val LONG_DURATION = 5000

enum class ShowDuration(val value: Int) {
    Short(SHORT_DURATION),
    Long(LONG_DURATION)
}

enum class InformType(val value: Int) {
    Inform(1),
    Warning(10),
    Error(100)
}

data class InformBarModel(
    val message: String,
    val duration: ShowDuration = ShowDuration.Short,
    val informType: InformType = InformType.Inform
)

class InformBarView : ConstraintLayout, ContentViewCallback {

    /**
    <declare-styleable name="InformBarView">

    <attr name="text" format="string" />

    <attr name="animDuration" format="integer">
    <enum name="Short" value="1" />
    <enum name="Long" value="10" />
    </attr>

    <attr name="informType" format="integer">
    <enum name="Inform" value="1" />
    <enum name="Warning" value="10" />
    <enum name="Error" value="100" />
    </attr>

    </declare-styleable>
     */

    var text: String? = ""
    var animDuration: Int? = ShowDuration.Short.value
    var informType: Int? = InformType.Inform.value

    private var interval = SHORT_ANIMATION

    private val informBarIcon: ImageView
    private val message: TextView

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        loadAttributes(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        loadAttributes(attributeSet)
    }

    private fun loadAttributes(attributeSet: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.InformBarView)

        text = typedArray.getString(R.styleable.InformBarView_text)
        animDuration = typedArray.getInt(R.styleable.InformBarView_informDuration, 1)
        informType = typedArray.getInt(R.styleable.InformBarView_informType, 1)

        typedArray.recycle()
        initView()
    }

    init {
        View.inflate(context, R.layout.view_informbar, this)
        this.message = findViewById(R.id.inform_message)
        this.informBarIcon = findViewById(R.id.inform_bar_icon)
        clipToPadding = false
    }

    fun setMessage(text: String) {
        message.text = text
    }

    fun initView() {
        interval = when (animDuration) {
            ShowDuration.Short.value -> SHORT_ANIMATION
            ShowDuration.Long.value -> LONG_ANIMATION
            else -> SHORT_ANIMATION
        }

        when (informType) {
            InformType.Inform.value -> {
                message.setBackgroundResource(R.drawable.background_informbar)
                informBarIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_pop_up_check
                    )
                )
            }
            InformType.Warning.value -> {
                message.setBackgroundResource(R.drawable.background_popup_warning)
                informBarIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_pop_up_warning
                    )
                )
            }
            InformType.Error.value -> {
                message.setBackgroundResource(R.drawable.background_popup_error)
                informBarIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_pop_up_error
                    )
                )
            }
        }
    }

    override fun animateContentOut(p0: Int, p1: Int) {}

    override fun animateContentIn(p0: Int, p1: Int) {
        val scaleX = ObjectAnimator.ofFloat(informBarIcon, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(informBarIcon, View.SCALE_Y, 0f, 1f)
        val animatorSet = AnimatorSet().apply {
            interpolator = OvershootInterpolator()
            duration = interval
            playTogether(scaleX, scaleY)
        }
        animatorSet.start()
    }
}