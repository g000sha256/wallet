package ru.g000sha256.wallet.model.dbo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class CardDbo(
    @SerializedName("search") val search: List<String>,
    @SerializedName("id") val id: Long,
    @SerializedName("barcode") val barcode: String,
    @SerializedName("category") val category: String,
    @SerializedName("kind") val kind: String,
    @SerializedName("name") val name: String,
    @SerializedName("number") val number: String,
    @SerializedName("texture") val texture: TextureDbo,
    @SerializedName("certificate") val certificate: CertificateDbo?,
    @SerializedName("loyaltyCard") val loyaltyCard: LoyaltyCardDbo?,
    @SerializedName("watchCount") var watchCount: Int,
    @SerializedName("lastDate") var lastDate: Long
) : Parcelable {

    object Kind {

        const val CERTIFICATE = "certificate"

    }

}