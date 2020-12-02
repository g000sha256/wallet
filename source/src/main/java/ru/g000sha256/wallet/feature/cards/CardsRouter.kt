package ru.g000sha256.wallet.feature.cards

import ru.g000sha256.wallet.model.dbo.CardDbo

interface CardsRouter {

    fun openCard(card: CardDbo)

}