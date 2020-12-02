package ru.g000sha256.wallet.feature.cards

import androidx.recyclerview.widget.DiffUtil
import java.util.Locale
import java.util.TreeSet
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.g000sha256.wallet.R
import ru.g000sha256.wallet.common.CoroutineDispatchers
import ru.g000sha256.wallet.common.StringProvider
import ru.g000sha256.wallet.extension.isEmpty
import ru.g000sha256.wallet.extension.isNotNullAndEmpty
import ru.g000sha256.wallet.feature.cards.adapter.CardsItem
import ru.g000sha256.wallet.model.dbo.CardDbo
import ru.g000sha256.wallet.view_model.BaseViewModel

private const val KEY_LOAD = "key_load"
private const val KEY_POST_ITEMS = "key_post_items"
private const val KEY_SEARCH = "key_search"

class CardsViewModelImpl @Inject constructor(
    private val cardsInteractor: CardsInteractor,
    private val cardsState: CardsState,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val coroutineScope: CoroutineScope,
    private val locale: Locale,
    private val stringProvider: StringProvider
) : BaseViewModel<CardsRouter, CardsView>(), CardsViewModel {

    private var cardsItems = emptyList<CardsItem>()

    override fun attach(router: CardsRouter, view: CardsView) {
        super.attach(router, view)
        cardsItems = emptyList()
        view.updateSearch(cardsState.search)
        view.visibleClear(isVisible = cardsState.search.isNotNullAndEmpty)
        if (cardsState.hasError) {
            view.showError(withAnimation = false)
            view.visibleRefresh(isVisible = false)
        } else {
            val hasJob = jobs.contains(KEY_LOAD)
            val cards = cardsState.cards
            if (cards == null) {
                view.showProgress(withAnimation = false)
                view.visibleRefresh(isVisible = false)
                if (!hasJob) load()
            } else {
                view.visibleRefresh(isVisible = hasJob)
                postCardsItems(withAnimation = false, cards)
            }
        }
        if (cardsState.dialogShowed) clickOnSettings()
    }

    override fun clickOnCard(id: Long) {
        val cards = cardsState.cards ?: return
        val card = cards.firstOrNull { it.id == id } ?: return
        card.watchCount++
        card.lastDate = System.currentTimeMillis()
        cardsInteractor.save(cards)
        view?.hideKeyboard()
        postCardsItems(withAnimation = true, cards)
        router?.openCard(card)
    }

    override fun clickOnClear() {
        view?.hideKeyboard()
        view?.updateSearch(text = null)
    }

    override fun clickOnRetry() {
        cardsState.hasError = false
        view?.showProgress(withAnimation = true)
        load()
    }

    override fun clickOnSettings() {
        cardsState.dialogShowed = true
        view?.showSettings(cardsInteractor.cardsSort, cardsInteractor.showCategories)
    }

    override fun dialogCanceled() {
        cardsState.dialogShowed = false
    }

    override fun refresh() {
        refresh(isBackground = false)
    }

    override fun selectShowCategories(showCategories: Boolean) {
        cardsInteractor.showCategories = showCategories
        val cards = cardsState.cards ?: return
        postCardsItems(withAnimation = true, cards)
    }

    override fun selectSort(cardsSort: CardsSort) {
        cardsInteractor.cardsSort = cardsSort
        val cards = cardsState.cards ?: return
        postCardsItems(withAnimation = true, cards)
    }

    override fun updateSearch(text: CharSequence) {
        val cards = cardsState.cards ?: return
        val search = text
            .trim()
            .toString()
            .toLowerCase(locale)
        view?.visibleClear(isVisible = search.isNotNullAndEmpty)
        if (search == cardsState.search) return
        cardsState.search = search
        jobs[KEY_SEARCH] = coroutineScope.launch {
            delay(200L)
            postCardsItems(withAnimation = true, cards)
            jobs.remove(KEY_SEARCH)
        }
    }

    private fun createCardsItems(allCards: List<CardDbo>): List<CardsItem> {
        val cardsSort = cardsInteractor.cardsSort
        val search = cardsState.search
        when {
            search.isNullOrEmpty() -> {
                val certificateCards = ArrayList<CardDbo>()
                val loyaltyCards = ArrayList<CardDbo>()
                val categories = TreeSet<String>()
                allCards.forEach {
                    when (it.kind) {
                        CardDbo.Kind.CERTIFICATE -> certificateCards.add(it)
                        else -> {
                            loyaltyCards.add(it)
                            categories.add(it.category)
                        }
                    }
                }
                val cardsItems = ArrayList<CardsItem>()
                if (!loyaltyCards.isEmpty) {
                    if (cardsSort != CardsSort.POPULAR) {
                        val text = stringProvider.getString(R.string.feature_cards_header_popular)
                        var cardsItem: CardsItem = CardsItem.Header(text)
                        cardsItems.add(cardsItem)
                        val cards = loyaltyCards
                            .sort(CardsSort.POPULAR)
                            .toCards()
                        cardsItem = CardsItem.Carousel(cards, text)
                        cardsItems.add(cardsItem)
                    }
                    if (cardsSort != CardsSort.LAST_USED) {
                        val text = stringProvider.getString(R.string.feature_cards_header_last)
                        var cardsItem: CardsItem = CardsItem.Header(text)
                        cardsItems.add(cardsItem)
                        val cards = loyaltyCards
                            .sort(CardsSort.LAST_USED)
                            .toCards()
                        cardsItem = CardsItem.Carousel(cards, text)
                        cardsItems.add(cardsItem)
                    }
                }
                if (!certificateCards.isEmpty) {
                    val text = stringProvider.getString(R.string.feature_cards_header_certificates)
                    var cardsItem: CardsItem = CardsItem.Header(text)
                    cardsItems.add(cardsItem)
                    val cards = certificateCards
                        .sort(cardsSort)
                        .toCards()
                    cardsItem = CardsItem.Carousel(cards, text)
                    cardsItems.add(cardsItem)
                }
                if (!loyaltyCards.isEmpty) {
                    if (cardsInteractor.showCategories) {
                        categories.forEach {
                            var cardsItem: CardsItem = CardsItem.Header(it)
                            cardsItems.add(cardsItem)
                            val cards = loyaltyCards
                                .filter { card -> card.category == it }
                                .sort(cardsSort)
                                .toCards()
                            cardsItem = CardsItem.Carousel(cards, it)
                            cardsItems.add(cardsItem)
                        }
                    } else {
                        if (!cardsItems.isEmpty) {
                            val text = stringProvider.getString(R.string.feature_cards_header_all)
                            val cardsItem = CardsItem.Header(text)
                            cardsItems.add(cardsItem)
                        }
                        val cards = loyaltyCards
                            .sort(cardsSort)
                            .toCards()
                        cardsItems.addAll(cards)
                    }
                }
                return cardsItems
            }
            else -> {
                return allCards
                    .filter { card ->
                        card.search.forEach {
                            val contains = it.contains(search)
                            if (contains) return@filter true
                        }
                        return@filter false
                    }
                    .sort(cardsSort)
                    .toCards()
            }
        }
    }

    private fun load() {
        jobs[KEY_LOAD] = coroutineScope.launch(
            context = CoroutineExceptionHandler { _, _ ->
                cardsState.hasError = true
                view?.showError(withAnimation = true)
                jobs.remove(KEY_LOAD)
            },
            block = {
                val result = cardsInteractor.getCards()
                val cards = result.second
                cardsState.cards = cards
                postCardsItems(withAnimation = true, cards)
                jobs.remove(KEY_LOAD)
                if (result.first) refresh(isBackground = true)
            }
        )
    }

    private fun postCardsItems(withAnimation: Boolean, cards: List<CardDbo>) {
        view ?: return
        jobs[KEY_POST_ITEMS] = coroutineScope.launch {
            val oldCardsItems = cardsItems
            withContext(coroutineDispatchers.ioCoroutineDispatcher) {
                val newCardsItems = createCardsItems(cards)
                val cardsDiffUtilCallback = CardsDiffUtilCallback(newCardsItems, oldCardsItems)
                val diffResult = DiffUtil.calculateDiff(cardsDiffUtilCallback)
                withContext(coroutineDispatchers.mainCoroutineDispatcher) {
                    cardsItems = newCardsItems
                    view?.showContent(withAnimation, diffResult, newCardsItems)
                    jobs.remove(KEY_POST_ITEMS)
                }
            }
        }
    }

    private fun refresh(isBackground: Boolean) {
        if (!isBackground) view?.visibleRefresh(isVisible = true)
        jobs[KEY_LOAD] = coroutineScope.launch(
            context = CoroutineExceptionHandler { _, _ ->
                if (!isBackground) {
                    view?.visibleRefresh(isVisible = false)
                    view?.showToast(R.string.feature_cards_error_message)
                }
                jobs.remove(KEY_LOAD)
            },
            block = {
                val cards = cardsInteractor.refresh()
                cardsState.cards = cards
                if (!isBackground) view?.visibleRefresh(isVisible = false)
                postCardsItems(withAnimation = true, cards)
                jobs.remove(KEY_LOAD)
            }
        )
    }

    private fun List<CardDbo>.sort(cardsSort: CardsSort): List<CardDbo> {
        when (cardsSort) {
            CardsSort.ADD_DATE -> return sortedByDescending { it.id }
            CardsSort.NAME -> return sortedBy { it.name }
            CardsSort.POPULAR -> return sortedByDescending { it.watchCount }
            CardsSort.LAST_USED -> return sortedByDescending { it.lastDate }
            else -> return this
        }
    }

    private fun List<CardDbo>.toCards(): List<CardsItem.Card> {
        return map { CardsItem.Card(it.id, it.texture.front) }
    }

}