package ru.g000sha256.wallet.model.dto

import com.google.gson.annotations.SerializedName

class TextureDto(
    @SerializedName("back") val back: String,
    @SerializedName("front") val front: String
)