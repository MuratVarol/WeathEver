package com.varol.weathever.internal.view

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

class CustomTypefaceSpan : TypefaceSpan {

    private val newType: Typeface

    constructor(type: Typeface) : super("") {
        newType = type
    }

    constructor(family: String, type: Typeface) : super(family) {
        newType = type
    }

    override fun updateDrawState(textPaint: TextPaint) {
        applyCustomTypeFace(textPaint, newType)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, newType)
    }

    private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
        val oldStyle: Int
        val old = paint.typeface
        oldStyle = old?.style ?: 0

        val fake = oldStyle and tf.style.inv()
        if (fake and Typeface.BOLD != 0) {
            paint.isFakeBoldText = true
        }

        if (fake and Typeface.ITALIC != 0) {
            paint.textSkewX = -0.25f
        }

        paint.typeface = tf
    }
}