package ru.g000sha256.wallet.feature.cards

import ru.g000sha256.wallet.feature.cards.adapter.card.CardsAdapterCardListener

interface CardsViewModel : CardsAdapterCardListener {

    fun clickOnClear()

    fun clickOnRetry()

    fun clickOnSettings()

    fun dialogCanceled()

    fun refresh()

    fun selectShowCategories(showCategories: Boolean)

    fun selectSort(cardsSort: CardsSort)

    fun updateSearch(text: CharSequence)

}