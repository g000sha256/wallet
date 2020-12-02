package ru.g000sha256.wallet.common

import android.content.Context
import javax.inject.Inject

class StringProvider @Inject constructor(private val context: Context) {

    fun getQuantityString(pluralId: Int, count: Int): String {
        return context.resources.getQuantityString(pluralId, count, count)
    }

    fun getString(stringId: Int): String {
        return context.resources.getString(stringId)
    }

}