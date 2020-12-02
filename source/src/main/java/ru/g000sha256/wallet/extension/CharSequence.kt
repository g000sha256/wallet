package ru.g000sha256.wallet.extension

val CharSequence?.isNotNullAndEmpty: Boolean
    get() = this != null && length > 0