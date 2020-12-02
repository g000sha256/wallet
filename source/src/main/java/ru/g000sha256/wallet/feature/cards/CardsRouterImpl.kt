package ru.g000sha256.wallet.feature.cards

import androidx.fragment.app.Fragment
import ru.g000sha256.wallet.Router
import ru.g000sha256.wallet.extension.doIfAlive
import ru.g000sha256.wallet.model.dbo.CardDbo

class CardsRouterImpl(private val fragment: Fragment) : CardsRouter {

    override fun openCard(card: CardDbo) {
        fragment.activity.doIfAlive {
            val router = it as? Router ?: return@doIfAlive
            router.openCard(card)
        }
    }

}