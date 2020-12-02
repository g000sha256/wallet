package ru.g000sha256.wallet.feature.card

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.g000sha256.wallet.model.dbo.CardDbo

@Parcelize
class CardState(val card: CardDbo, var isFlipped: Boolean = false) : Parcelable