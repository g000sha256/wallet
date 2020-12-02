package ru.g000sha256.wallet.feature.card

import androidx.fragment.app.Fragment
import ru.g000sha256.wallet.Router
import ru.g000sha256.wallet.extension.doIfAlive

class CardRouterImpl(private val fragment: Fragment) : CardRouter {

    override fun back() {
        fragment.activity.doIfAlive {
            val router = it as? Router ?: return@doIfAlive
            router.back()
        }
    }

}