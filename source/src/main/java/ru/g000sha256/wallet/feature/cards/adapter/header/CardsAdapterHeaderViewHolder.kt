package ru.g000sha256.wallet.feature.cards.adapter.header

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.g000sha256.wallet.extension.updateText
import ru.g000sha256.wallet.feature.cards.adapter.CardsItem

class CardsAdapterHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val textView = view as TextView

    fun bind(cardsItem: CardsItem.Header) {
        textView.updateText(cardsItem.text)
    }

}