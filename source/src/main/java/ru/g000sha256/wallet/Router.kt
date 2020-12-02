package ru.g000sha256.wallet

import ru.g000sha256.wallet.model.dbo.CardDbo

interface Router {

    fun back()

    fun openCard(card: CardDbo)

}