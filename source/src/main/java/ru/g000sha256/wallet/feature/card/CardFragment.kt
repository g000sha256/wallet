package ru.g000sha256.wallet.feature.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.RequestManager
import javax.inject.Inject
import ru.g000sha256.wallet.R
import ru.g000sha256.wallet.applicationComponent
import ru.g000sha256.wallet.extension.dp
import ru.g000sha256.wallet.extension.isLargeScreen
import ru.g000sha256.wallet.extension.setOnInsetsChangedCallback
import ru.g000sha256.wallet.feature.card.di.CardModule
import ru.g000sha256.wallet.model.dbo.CardDbo
import ru.g000sha256.wallet.view_model.ViewModelProviderFactory

private const val EXTRA_CARD = "extra_card"
private const val EXTRA_STATE = "extra_state"

fun createCardFragment(card: CardDbo): CardFragment {
    val cardFragment = CardFragment()
    val bundle = Bundle()
    bundle.putParcelable(EXTRA_CARD, card)
    cardFragment.arguments = bundle
    return cardFragment
}

class CardFragment : Fragment() {

    @Inject lateinit var cardState: CardState
    @Inject lateinit var requestManager: RequestManager
    @Inject lateinit var viewModelProviderFactory: ViewModelProviderFactory<CardViewModelImpl>

    private val cardViewModel: CardViewModelImpl by lazy { viewModelProviderFactory.get(viewModelStore) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cardState = savedInstanceState?.getParcelable(EXTRA_STATE) ?: createCardState()
        val cardModule = CardModule(cardState)
        applicationComponent
            .plusCardSubComponent(cardModule)
            .inject(this)
        setBrightness(1F)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (resources.isLargeScreen) {
            val view = inflater.inflate(R.layout.feature_card_large, container, false)
            view.setOnClickListener { cardViewModel.clickOnBack() }
            view.setOnInsetsChangedCallback { bottom, top ->
                val padding = 32.dp
                view.setPadding(padding, padding + top, padding, padding + bottom)
            }
            val contentView = view.findViewById<FrameLayout>(R.id.content_view)
            contentView.clipToOutline = true
            contentView.background = resources.getDrawable(R.drawable.feature_card_bg, view.context.theme)
            contentView.foreground = resources.getDrawable(R.drawable.feature_card_fg, view.context.theme)
            return view
        } else {
            val view = inflater.inflate(R.layout.feature_card_small, container, false)
            view.setBackgroundResource(R.color.gray33)
            return view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cardRouter = CardRouterImpl(this)
        val cardView = CardViewImpl(cardViewModel, requestManager, view)
        cardViewModel.attach(cardRouter, cardView)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(EXTRA_STATE, cardState)
    }

    override fun onDestroyView() {
        cardViewModel.detach()
        super.onDestroyView()
    }

    override fun onDestroy() {
        if (isRemoving) setBrightness(-1F)
        super.onDestroy()
    }

    private fun createCardState(): CardState {
        val card = arguments?.getParcelable<CardDbo>(EXTRA_CARD)!!
        return CardState(card)
    }

    private fun setBrightness(brightness: Float) {
        val activity = requireActivity()
        val window = activity.window
        val layoutParams = window.attributes
        layoutParams.screenBrightness = brightness
        window.attributes = layoutParams
    }

}