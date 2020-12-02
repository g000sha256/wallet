package ru.g000sha256.wallet.model.dbo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class CertificateDbo(
    @SerializedName("value") val value: Int,
    @SerializedName("expireDate") val expireDate: Long
) : Parcelable