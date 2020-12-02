package ru.g000sha256.wallet.model.dto

import com.google.gson.annotations.SerializedName

class CertificateDto(
    @SerializedName("value") val value: Int,
    @SerializedName("expireDate") val expireDate: Long
)