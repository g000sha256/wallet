package ru.g000sha256.wallet.model.dbo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class LoyaltyCardDbo(
    @SerializedName("balance") val balance: Int,
    @SerializedName("grade") val grade: String
) : Parcelable {

    object Grade {

        const val SILVER = "silver"
        const val GOLD = "gold"
        const val PLATINUM = "platinum"

    }

}