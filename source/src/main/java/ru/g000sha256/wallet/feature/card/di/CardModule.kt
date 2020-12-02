package ru.g000sha256.wallet.feature.card.di

import dagger.Module
import dagger.Provides
import ru.g000sha256.wallet.feature.card.CardState

@Module
class CardModule(private val cardState: CardState) {

    @Provides
    fun provideCardState(): CardState {
        return cardState
    }

}