package ru.g000sha256.wallet.extension

import android.content.res.Configuration
import android.content.res.Resources

val Resources.isLargeScreen: Boolean
    get() {
        val screenLayout = configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        return screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE || screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE
    }