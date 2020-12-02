package ru.g000sha256.wallet.model.dto

import com.google.gson.annotations.SerializedName

class CardDto(
    @SerializedName("search") val search: List<String>,
    @SerializedName("id") val id: Long,
    @SerializedName("barcode") val barcode: String,
    @SerializedName("category") val category: String,
    @SerializedName("kind") val kind: String,
    @SerializedName("name") val name: String,
    @SerializedName("number") val number: String,
    @SerializedName("texture") val texture: TextureDto,
    @SerializedName("certificate") val certificate: CertificateDto?,
    @SerializedName("loyaltyCard") val loyaltyCard: LoyaltyCardDto?
)