package ru.g000sha256.wallet.feature.cards.di

import dagger.Module
import dagger.Provides
import ru.g000sha256.wallet.feature.cards.CardsState

@Module
class CardsModule(private val cardsState: CardsState) {

    @Provides
    fun provideCardsState(): CardsState {
        return cardsState
    }

}