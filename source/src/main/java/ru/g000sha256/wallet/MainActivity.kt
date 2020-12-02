package ru.g000sha256.wallet

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.OrientationEventListener
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import ru.g000sha256.wallet.extension.isLargeScreen
import ru.g000sha256.wallet.feature.card.createCardFragment
import ru.g000sha256.wallet.feature.cards.CardsFragment
import ru.g000sha256.wallet.model.dbo.CardDbo

class MainActivity : FragmentActivity(), Router {

    private var orientationEventListener: OrientationEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = getSystemUiVisibility()
        super.onCreate(savedInstanceState)
        orientationEventListener = enableOrientationEventListener()
        if (savedInstanceState != null) return
        val cardsFragment = CardsFragment()
        supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, cardsFragment)
            .commitNow()
    }

    override fun onDestroy() {
        orientationEventListener?.disable()
        super.onDestroy()
    }

    override fun back() {
        onBackPressed()
    }

    override fun openCard(card: CardDbo) {
        val cardFragment = createCardFragment(card)
        supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, cardFragment)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun enableOrientationEventListener(): OrientationEventListener? {
        if (!resources.isLargeScreen) return null
        val orientationEventListener = object : OrientationEventListener(this) {

            private var lastOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            override fun onOrientationChanged(orientation: Int) {
                if (orientation < 0) return
                val newOrientation: Int
                when {
                    orientation <= 45 -> newOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    orientation <= 135 -> newOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                    orientation <= 225 -> newOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                    orientation <= 315 -> newOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    else -> newOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
                if (newOrientation != lastOrientation) {
                    lastOrientation = newOrientation
                    requestedOrientation = newOrientation
                }
            }

        }
        orientationEventListener.enable()
        return orientationEventListener
    }

    private fun getSystemUiVisibility(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        return View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

}