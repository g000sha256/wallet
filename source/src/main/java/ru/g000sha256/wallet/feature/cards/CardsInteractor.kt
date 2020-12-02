package ru.g000sha256.wallet.feature.cards

import com.bumptech.glide.RequestManager
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.g000sha256.wallet.common.Api
import ru.g000sha256.wallet.common.CoroutineDispatchers
import ru.g000sha256.wallet.common.Storage
import ru.g000sha256.wallet.model.dbo.CardDbo
import ru.g000sha256.wallet.model.dbo.CertificateDbo
import ru.g000sha256.wallet.model.dbo.LoyaltyCardDbo
import ru.g000sha256.wallet.model.dbo.TextureDbo
import ru.g000sha256.wallet.model.dto.CardDto
import ru.g000sha256.wallet.model.dto.CertificateDto
import ru.g000sha256.wallet.model.dto.LoyaltyCardDto
import ru.g000sha256.wallet.model.dto.TextureDto

private const val SORT_ADD_DATE = 0
private const val SORT_LAST_USED = 1
private const val SORT_NAME = 2
private const val SORT_POPULAR = 3

class CardsInteractor @Inject constructor(
    private val api: Api,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val coroutineScope: CoroutineScope,
    private val locale: Locale,
    private val requestManager: RequestManager,
    private val storage: Storage
) {

    var showCategories: Boolean
        get() = storage.showCategories
        set(value) {
            storage.showCategories = value
        }

    var cardsSort: CardsSort
        get() {
            when (storage.sort) {
                SORT_ADD_DATE -> return CardsSort.ADD_DATE
                SORT_LAST_USED -> return CardsSort.LAST_USED
                SORT_NAME -> return CardsSort.NAME
                SORT_POPULAR -> return CardsSort.POPULAR
                else -> throw IllegalArgumentException()
            }
        }
        set(value) {
            when (value) {
                CardsSort.ADD_DATE -> storage.sort = SORT_ADD_DATE
                CardsSort.LAST_USED -> storage.sort = SORT_LAST_USED
                CardsSort.NAME -> storage.sort = SORT_NAME
                CardsSort.POPULAR -> storage.sort = SORT_POPULAR
            }
        }

    private var cards: List<CardDbo>? = null

    suspend fun getCards(): Pair<Boolean, List<CardDbo>> {
        val cards = restore() ?: return false to load()
        return true to cards
    }

    suspend fun refresh(): List<CardDbo> {
        return load()
    }

    fun save(cards: List<CardDbo>) {
        this.cards = cards
        coroutineScope.launch(coroutineDispatchers.ioCoroutineDispatcher) { storage.save(cards) }
    }

    private suspend fun load(): List<CardDbo> {
        val cards = withContext(coroutineDispatchers.ioCoroutineDispatcher) {
            return@withContext api
                .getCards(this)
                .onEach { loadImage(it.barcode) }
                .onEach { loadImage(it.texture.front) }
                .onEach { loadImage(it.texture.back) }
                .map { it.map() }
        }
        save(cards)
        return cards
    }

    private suspend fun restore(): List<CardDbo>? {
        var cards = cards
        if (cards == null) {
            cards = withContext(coroutineDispatchers.ioCoroutineDispatcher) { storage.restore() }
            this.cards = cards
        }
        return cards
    }

    private fun loadImage(image: String) {
        requestManager
            .load(image)
            .submit()
    }

    private fun CardDto.createSearch(): List<String> {
        return search
            .toMutableList()
            .apply {
                add(category)
                add(name)
            }
            .map { it.toLowerCase(locale) }
    }

    private fun CardDto.map(): CardDbo {
        val card = cards?.firstOrNull { it.id == id }
        return CardDbo(
            search = createSearch(),
            id = id,
            barcode = barcode,
            category = category,
            kind = kind,
            name = name,
            number = number,
            texture = texture.map(),
            certificate = certificate.map(),
            loyaltyCard = loyaltyCard.map(),
            watchCount = card?.watchCount ?: 0,
            lastDate = card?.lastDate ?: id
        )
    }

    private fun TextureDto.map(): TextureDbo {
        return TextureDbo(back, front)
    }

    private fun CertificateDto?.map(): CertificateDbo? {
        this ?: return null
        return CertificateDbo(value, expireDate)
    }

    private fun LoyaltyCardDto?.map(): LoyaltyCardDbo? {
        this ?: return null
        return LoyaltyCardDbo(balance, grade)
    }

}