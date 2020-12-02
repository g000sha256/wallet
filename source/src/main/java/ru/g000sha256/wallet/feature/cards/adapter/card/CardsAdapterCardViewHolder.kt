package ru.g000sha256.wallet.feature.cards.adapter.card

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import ru.g000sha256.wallet.R
import ru.g000sha256.wallet.feature.cards.adapter.CardsItem

class CardsAdapterCardViewHolder(
    private val requestManager: RequestManager,
    cardsAdapterCardListener: CardsAdapterCardListener,
    cardWidth: Int,
    view: View
) : RecyclerView.ViewHolder(view) {

    private val imageView = view.findViewById<ImageView>(R.id.image_view)

    private lateinit var cardsItem: CardsItem.Card

    init {
        view.clipToOutline = true
        view.layoutParams.width = cardWidth
        view.setOnClickListener { cardsAdapterCardListener.clickOnCard(cardsItem.id) }
    }

    fun bind(cardsItem: CardsItem.Card) {
        this.cardsItem = cardsItem
        requestManager
            .load(cardsItem.image)
            .into(imageView)
    }

}