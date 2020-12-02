package ru.g000sha256.wallet.model.dto

import com.google.gson.annotations.SerializedName

class LoyaltyCardDto(
    @SerializedName("balance") val balance: Int,
    @SerializedName("grade") val grade: String
)