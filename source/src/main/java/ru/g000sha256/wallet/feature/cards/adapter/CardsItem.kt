package ru.g000sha256.wallet.feature.cards.adapter

sealed class CardsItem {

    abstract fun areItemsTheSame(cardsItem: CardsItem): Boolean

    abstract fun areContentsTheSame(cardsItem: CardsItem): Boolean

    data class Card(val id: Long, val image: String) : CardsItem() {

        override fun areItemsTheSame(cardsItem: CardsItem): Boolean {
            return cardsItem is Card && cardsItem.id == id
        }

        override fun areContentsTheSame(cardsItem: CardsItem): Boolean {
            return cardsItem == this
        }

    }

    data class Carousel(val cards: List<Card>, val hash: String) : CardsItem() {

        override fun areItemsTheSame(cardsItem: CardsItem): Boolean {
            return cardsItem is Carousel && cardsItem.hash == hash
        }

        override fun areContentsTheSame(cardsItem: CardsItem): Boolean {
            return cardsItem == this
        }

    }

    data class Header(val text: String) : CardsItem() {

        override fun areItemsTheSame(cardsItem: CardsItem): Boolean {
            return cardsItem is Header
        }

        override fun areContentsTheSame(cardsItem: CardsItem): Boolean {
            return cardsItem == this
        }

    }

}