package ru.g000sha256.wallet.feature.cards.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import ru.g000sha256.wallet.R
import ru.g000sha256.wallet.extension.isEmpty
import ru.g000sha256.wallet.feature.cards.adapter.card.CardsAdapterCardListener
import ru.g000sha256.wallet.feature.cards.adapter.card.CardsAdapterCardViewHolder
import ru.g000sha256.wallet.feature.cards.adapter.carousel.CardsAdapterCarouselViewHolder
import ru.g000sha256.wallet.feature.cards.adapter.header.CardsAdapterHeaderViewHolder

class CardsAdapter(
    private val cardsAdapterCardListener: CardsAdapterCardListener,
    private val carouselCardWidth: Int,
    private val listCardWidth: Int,
    private val requestManager: RequestManager,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    object ViewType {

        const val CARD = 1
        const val CAROUSEL = 2
        const val HEADER = 3

    }

    private val cardsItems = ArrayList<CardsItem>()

    override fun getItemCount(): Int {
        return cardsItems.size
    }

    override fun getItemViewType(position: Int): Int {
        val cardsItem = cardsItems[position]
        when (cardsItem) {
            is CardsItem.Card -> return ViewType.CARD
            is CardsItem.Carousel -> return ViewType.CAROUSEL
            is CardsItem.Header -> return ViewType.HEADER
            else -> throw IllegalArgumentException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ViewType.CARD -> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.feature_cards_item_card, parent, false)
                return CardsAdapterCardViewHolder(requestManager, cardsAdapterCardListener, listCardWidth, view)
            }
            ViewType.CAROUSEL -> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.feature_cards_item_carousel, parent, false)
                return CardsAdapterCarouselViewHolder(cardsAdapterCardListener, carouselCardWidth, requestManager, view)
            }
            ViewType.HEADER -> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.feature_cards_item_header, parent, false)
                return CardsAdapterHeaderViewHolder(view)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        when (holder) {
            is CardsAdapterCardViewHolder -> {
                val cardsItem = cardsItems[position] as CardsItem.Card
                holder.bind(cardsItem)
            }
            is CardsAdapterCarouselViewHolder -> {
                val cardsItem = cardsItems[position] as CardsItem.Carousel
                holder.bind(fromPayload = !payloads.isEmpty, cardsItem)
            }
            is CardsAdapterHeaderViewHolder -> {
                val cardsItem = cardsItems[position] as CardsItem.Header
                holder.bind(cardsItem)
            }
            else -> throw IllegalArgumentException()
        }
    }

    fun setCardsItems(cardsItems: List<CardsItem>) {
        this.cardsItems.clear()
        this.cardsItems.addAll(cardsItems)
    }

}