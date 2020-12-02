package ru.g000sha256.wallet.feature.cards.di

import dagger.Subcomponent
import ru.g000sha256.wallet.feature.cards.CardsFragment

@Subcomponent(modules = [CardsModule::class])
interface CardsSubComponent {

    fun inject(cardsFragment: CardsFragment)

}