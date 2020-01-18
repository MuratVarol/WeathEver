package com.varol.weathever.internal.view

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.varol.weathever.R
import com.varol.weathever.internal.extension.gone
import com.varol.weathever.internal.extension.isVisible
import com.varol.weathever.internal.extension.visible

class RootConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var exclusiveTouch = false
    private var touchableInProgress = false
    private var consumeTouches = false
    private var blurBackground = false
    private var progressBar: ProgressBar? = null

    companion object {
        const val DURATION_TEMP_LOCK_UI_MS = 300L
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RootConstraintLayout)

        exclusiveTouch =
            typedArray.getBoolean(R.styleable.RootConstraintLayout_exclusiveTouch, true)
        touchableInProgress =
            typedArray.getBoolean(R.styleable.RootConstraintLayout_touchableInProgress, false)
        blurBackground =
            typedArray.getBoolean(R.styleable.RootConstraintLayout_blurBackground, true)

        typedArray.recycle()

        initView()
    }

    private fun initView() {
        isClickable = true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (ev?.actionMasked == MotionEvent.ACTION_DOWN) shouldConsumeTouches() else false
    }

    private fun shouldConsumeTouches(): Boolean {
        return (exclusiveTouch && shouldLockScreen()) ||
                ((progressBar?.isVisible() ?: false) && !touchableInProgress)
    }

    fun showProgress() {
        if (progressBar == null) {
            progressBar = ProgressBar(
                context,
                null,
                android.R.attr.progressBarStyle
            )
            progressBar?.let { progress ->
                progress.id = ViewCompat.generateViewId()
                progress.indeterminateDrawable?.setColorFilter(
                    ContextCompat.getColor(context, R.color.colorPrimary),
                    PorterDuff.Mode.MULTIPLY
                )
                val layout = this as ConstraintLayout

                layout.addView(progress)

                val constraintSet = ConstraintSet().apply {
                    clone(layout)
                    connect(
                        progress.id,
                        ConstraintSet.END,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.END
                    )
                    connect(
                        progress.id,
                        ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.TOP
                    )
                    connect(
                        progress.id,
                        ConstraintSet.BOTTOM,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.BOTTOM
                    )
                    connect(
                        progress.id,
                        ConstraintSet.START,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.START
                    )
                    constrainHeight(progress.id, ConstraintSet.WRAP_CONTENT)
                    constrainWidth(progress.id, ConstraintSet.WRAP_CONTENT)
                }
                constraintSet.applyTo(layout)
            }
        }
        if (blurBackground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            foreground =
                ContextCompat.getDrawable(context, R.drawable.background_shadow)
        }
        progressBar?.visible()
    }

    fun hideProgress() {
        progressBar?.let { progressBar ->
            if (progressBar.isVisible()) {
                progressBar.gone()
            }
        }
        if (blurBackground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            foreground = null
        }
    }

    private fun shouldLockScreen(): Boolean {
        if (!consumeTouches) {
            consumeTouches = true

            Handler().postDelayed({
                consumeTouches = false
            }, DURATION_TEMP_LOCK_UI_MS)

            return false
        }
        return true
    }
}