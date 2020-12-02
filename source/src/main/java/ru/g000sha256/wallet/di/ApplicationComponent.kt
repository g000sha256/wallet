package ru.g000sha256.wallet.di

import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton
import ru.g000sha256.wallet.MainApplication
import ru.g000sha256.wallet.feature.card.di.CardModule
import ru.g000sha256.wallet.feature.card.di.CardSubComponent
import ru.g000sha256.wallet.feature.cards.di.CardsModule
import ru.g000sha256.wallet.feature.cards.di.CardsSubComponent

@Component(modules = [ApplicationModule::class])
@Singleton
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun mainApplication(mainApplication: MainApplication): Builder

        fun build(): ApplicationComponent

    }

    fun plusCardSubComponent(cardModule: CardModule): CardSubComponent

    fun plusCardsSubComponent(cardsModule: CardsModule): CardsSubComponent

}