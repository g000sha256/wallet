package ru.g000sha256.wallet.extension

import android.widget.TextView

fun TextView.updateText(text: CharSequence?) {
    if (text == this.text) return
    this.text = text
}