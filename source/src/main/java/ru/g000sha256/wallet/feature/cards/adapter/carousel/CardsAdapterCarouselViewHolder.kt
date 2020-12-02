package ru.g000sha256.wallet.feature.cards.adapter.carousel

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import ru.g000sha256.wallet.extension.dpF
import ru.g000sha256.wallet.feature.cards.adapter.CardsAdapter
import ru.g000sha256.wallet.feature.cards.adapter.CardsItem
import ru.g000sha256.wallet.feature.cards.adapter.card.CardsAdapterCardListener

class CardsAdapterCarouselViewHolder(
    cardsAdapterCardListener: CardsAdapterCardListener,
    carouselCardWidth: Int,
    requestManager: RequestManager,
    view: View
) : RecyclerView.ViewHolder(view) {

    private val recyclerView = view as RecyclerView

    private val cardsAdapter = CardsAdapter(cardsAdapterCardListener, carouselCardWidth, carouselCardWidth, requestManager)

    private var scroll = 0

    init {
        recyclerView.adapter = cardsAdapter
        val itemDecoration = object : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val position = parent.getChildAdapterPosition(view)
                when (position) {
                    0 -> {
                        view.elevation = 0F
                        outRect.set(scroll, 0, 0, 0)
                    }
                    1 -> {
                        when {
                            scroll >= carouselCardWidth -> {
                                view.elevation = 0F
                                outRect.set(-carouselCardWidth, 0, 0, 0)
                            }
                            else -> {
                                view.elevation = 2.dpF
                                outRect.set(-scroll, 0, 0, 0)
                            }
                        }
                    }
                    else -> {
                        when {
                            scroll <= carouselCardWidth * (position - 1) -> {
                                view.elevation = 2.dpF
                                outRect.set(0, 0, 0, 0)
                            }
                            scroll <= carouselCardWidth * position -> {
                                view.elevation = 2.dpF
                                outRect.set(carouselCardWidth * (position - 1) - scroll, 0, 0, 0)
                            }
                            else -> {
                                view.elevation = 0F
                                outRect.set(-carouselCardWidth, 0, 0, 0)
                            }
                        }
                    }
                }
            }

        }
        recyclerView.addItemDecoration(itemDecoration)
        val onScrollListener = object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                scroll = recyclerView.computeHorizontalScrollOffset()
                recyclerView.invalidateItemDecorations()
            }

        }
        recyclerView.addOnScrollListener(onScrollListener)
    }

    fun bind(fromPayload: Boolean, cardsItem: CardsItem.Carousel) {
        cardsAdapter.setCardsItems(cardsItem.cards)
        cardsAdapter.notifyDataSetChanged()
        if (fromPayload) return
        recyclerView.scrollToPosition(0)
        scroll = 0
    }

}