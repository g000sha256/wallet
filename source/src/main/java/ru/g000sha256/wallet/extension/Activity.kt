package ru.g000sha256.wallet.extension

import android.app.Activity

fun Activity?.doIfAlive(callback: (Activity) -> Unit) {
    this ?: return
    if (isFinishing) return
    callback(this)
}