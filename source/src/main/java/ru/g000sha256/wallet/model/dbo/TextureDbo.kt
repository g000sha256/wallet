package ru.g000sha256.wallet.model.dbo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class TextureDbo(
    @SerializedName("back") val back: String,
    @SerializedName("front") val front: String
) : Parcelable