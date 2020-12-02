package ru.g000sha256.wallet.feature.cards

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.RequestManager
import javax.inject.Inject
import ru.g000sha256.wallet.R
import ru.g000sha256.wallet.applicationComponent
import ru.g000sha256.wallet.feature.cards.di.CardsModule
import ru.g000sha256.wallet.view_model.ViewModelProviderFactory

private const val EXTRA_STATE = "extra_state"

class CardsFragment : Fragment(R.layout.feature_cards) {

    @Inject lateinit var cardsState: CardsState
    @Inject lateinit var requestManager: RequestManager
    @Inject lateinit var viewModelProviderFactory: ViewModelProviderFactory<CardsViewModelImpl>

    private val cardsViewModel: CardsViewModelImpl by lazy { viewModelProviderFactory.get(viewModelStore) }

    private var cardsView: CardsViewImpl? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cardsState = savedInstanceState?.getParcelable(EXTRA_STATE) ?: CardsState()
        val cardsModule = CardsModule(cardsState)
        applicationComponent
            .plusCardsSubComponent(cardsModule)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cardsRouter = CardsRouterImpl(this)
        val cardsView = CardsViewImpl(cardsViewModel, requestManager, view)
        cardsViewModel.attach(cardsRouter, cardsView)
        this.cardsView = cardsView
    }

    override fun onStart() {
        super.onStart()
        view?.requestApplyInsets()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(EXTRA_STATE, cardsState)
    }

    override fun onDestroyView() {
        cardsViewModel.detach()
        cardsView?.destroy()
        cardsView = null
        super.onDestroyView()
    }

}