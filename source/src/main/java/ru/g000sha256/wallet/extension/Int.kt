package ru.g000sha256.wallet.extension

import android.content.res.Resources

val Int.dp: Int
    get() = dpF.toInt()

val Int.dpF: Float
    get() = this * resources.displayMetrics.density

private val resources by lazy { Resources.getSystem() }