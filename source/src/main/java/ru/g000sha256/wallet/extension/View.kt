package ru.g000sha256.wallet.extension

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager

fun View.doOnPreDraw(callback: () -> Unit) {
    val onPreDrawListener = object : ViewTreeObserver.OnPreDrawListener {

        override fun onPreDraw(): Boolean {
            viewTreeObserver.removeOnPreDrawListener(this)
            callback()
            return true
        }

    }
    viewTreeObserver.addOnPreDrawListener(onPreDrawListener)
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.setHeight(height: Int) {
    val layoutParams = layoutParams ?: return
    if (height == layoutParams.height) return
    layoutParams.height = height
    requestLayout()
}

fun View.setMarginTop(margin: Int) {
    val layoutParams = layoutParams as? ViewGroup.MarginLayoutParams ?: return
    if (margin == layoutParams.topMargin) return
    layoutParams.topMargin = margin
    requestLayout()
}

fun View.setOnInsetsChangedCallback(callback: (bottom: Int, top: Int) -> Unit) {
    setOnApplyWindowInsetsListener { _, insets ->
        callback(insets.systemWindowInsetBottom, insets.systemWindowInsetTop)
        return@setOnApplyWindowInsetsListener insets
    }
}