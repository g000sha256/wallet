package ru.g000sha256.wallet.feature.cards

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.g000sha256.wallet.model.dbo.CardDbo

@Parcelize
class CardsState(
    var dialogShowed: Boolean = false,
    var hasError: Boolean = false,
    var cards: List<CardDbo>? = null,
    var search: String? = null
) : Parcelable