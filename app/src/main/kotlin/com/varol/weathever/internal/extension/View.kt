package com.varol.weathever.internal.extension

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat.getSystemService


fun View.isVisible() = this.visibility == View.VISIBLE

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun AppCompatEditText.hideKeyboard() {
    val inputMethodManager =
        (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

// https://medium.com/@fabionegri/make-snackbar-great-again-51edf7c940d4
fun View?.findSuitableParent(): ViewGroup? {
    var view = this
    var fallback: ViewGroup? = null
    do {
        if (view is CoordinatorLayout) {
            return view
        } else if (view is FrameLayout) {
            if (view.id == android.R.id.content) {
                return view
            } else {
                fallback = view
            }
        }

        if (view != null) {
            val parent = view.parent
            view = if (parent is View) parent else null
        }
    } while (view != null)

    return fallback
}

