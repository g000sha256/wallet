package ru.g000sha256.wallet.feature.cards

import androidx.recyclerview.widget.DiffUtil
import ru.g000sha256.wallet.feature.cards.adapter.CardsItem

class CardsDiffUtilCallback(
    private val newCardsItems: List<CardsItem>,
    private val oldCardsItems: List<CardsItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldCardsItems.size
    }

    override fun getNewListSize(): Int {
        return newCardsItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newCardsItem = newCardsItems[newItemPosition]
        val oldCardsItem = oldCardsItems[oldItemPosition]
        return oldCardsItem.areItemsTheSame(newCardsItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newCardsItem = newCardsItems[newItemPosition]
        val oldCardsItem = oldCardsItems[oldItemPosition]
        return oldCardsItem.areContentsTheSame(newCardsItem)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val newCardsItem = newCardsItems[newItemPosition]
        if (newCardsItem is CardsItem.Carousel) return Unit
        return null
    }

}