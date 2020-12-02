package ru.g000sha256.wallet.feature.card

import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import ru.g000sha256.wallet.R
import ru.g000sha256.wallet.common.StringProvider
import ru.g000sha256.wallet.model.dbo.CardDbo
import ru.g000sha256.wallet.model.dbo.LoyaltyCardDbo
import ru.g000sha256.wallet.view_model.BaseViewModel

class CardViewModelImpl @Inject constructor(
    private val cardState: CardState,
    private val stringProvider: StringProvider,
    locale: Locale
) : BaseViewModel<CardRouter, CardView>(), CardViewModel {

    private val simpleDateFormat = SimpleDateFormat("d MMMM yyyy", locale)

    private var isAnimate = false

    override fun attach(router: CardRouter, view: CardView) {
        super.attach(router, view)
        val card = cardState.card
        view.updateTitle(card.name)
        view.updateCardImages(card.texture.back, card.texture.front)
        view.updateCardState(isBackVisible = cardState.isFlipped)
        view.updateCodeImage(card.barcode)
        when (card.kind) {
            CardDbo.Kind.CERTIFICATE -> {
                val certificate = card.certificate!!
                view.updateInfo(
                    title = stringProvider.getString(R.string.feature_card_certificate),
                    title1 = stringProvider.getString(R.string.feature_card_number),
                    description1 = card.number,
                    title2 = stringProvider.getString(R.string.feature_card_category),
                    description2 = card.category,
                    title3 = stringProvider.getString(R.string.feature_card_date),
                    description3 = simpleDateFormat.format(certificate.expireDate * 1000),
                    title4 = stringProvider.getString(R.string.feature_card_balance),
                    description4 = stringProvider.getQuantityString(R.plurals.feature_card, certificate.value)
                )
            }
            else -> {
                val loyaltyCard = card.loyaltyCard!!
                view.updateInfo(
                    title = stringProvider.getString(R.string.feature_card_loyalty),
                    title1 = stringProvider.getString(R.string.feature_card_number),
                    description1 = card.number,
                    title2 = stringProvider.getString(R.string.feature_card_category),
                    description2 = card.category,
                    title3 = stringProvider.getString(R.string.feature_card_status),
                    description3 = getLoyaltyStatus(loyaltyCard.grade),
                    title4 = stringProvider.getString(R.string.feature_card_balance),
                    description4 = stringProvider.getQuantityString(R.plurals.feature_card, loyaltyCard.balance)
                )
            }
        }
    }

    override fun animationEnded() {
        isAnimate = false
    }

    override fun clickOnBack() {
        router?.back()
    }

    override fun clickOnCard() {
        val view = view ?: return
        if (isAnimate) return
        isAnimate = true
        if (cardState.isFlipped) view.animateCardToFront() else view.animateCardToBack()
        cardState.isFlipped = !cardState.isFlipped
    }

    private fun getLoyaltyStatus(grade: String): String {
        when (grade) {
            LoyaltyCardDbo.Grade.GOLD -> return stringProvider.getString(R.string.feature_card_status_gold)
            LoyaltyCardDbo.Grade.PLATINUM -> return stringProvider.getString(R.string.feature_card_status_platinum)
            LoyaltyCardDbo.Grade.SILVER -> return stringProvider.getString(R.string.feature_card_status_silver)
            else -> return stringProvider.getString(R.string.feature_card_status_default)
        }
    }

    private fun CardView.updateInfo(
        title: String,
        title1: String,
        description1: String,
        title2: String,
        description2: String,
        title3: String,
        description3: String,
        title4: String,
        description4: String
    ) {
        updateInfoTitle(title)
        updateInfo1Title(title1)
        updateInfo1Description(description1)
        updateInfo2Title(title2)
        updateInfo2Description(description2)
        updateInfo3Title(title3)
        updateInfo3Description(description3)
        updateInfo4Title(title4)
        updateInfo4Description(description4)
    }

}