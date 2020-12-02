package ru.g000sha256.wallet.feature.cards

import androidx.recyclerview.widget.DiffUtil
import ru.g000sha256.wallet.feature.cards.adapter.CardsItem

interface CardsView {

    fun hideKeyboard()

    fun showContent(withAnimation: Boolean, diffResult: DiffUtil.DiffResult, cardsItems: List<CardsItem>)

    fun showError(withAnimation: Boolean)

    fun showProgress(withAnimation: Boolean)

    fun showSettings(cardsSort: CardsSort, showCategories: Boolean)

    fun showToast(messageId: Int)

    fun updateSearch(text: CharSequence?)

    fun visibleClear(isVisible: Boolean)

    fun visibleRefresh(isVisible: Boolean)

}