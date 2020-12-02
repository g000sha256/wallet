package ru.g000sha256.wallet.feature.card.di

import dagger.Subcomponent
import ru.g000sha256.wallet.feature.card.CardFragment

@Subcomponent(modules = [CardModule::class])
interface CardSubComponent {

    fun inject(cardFragment: CardFragment)

}