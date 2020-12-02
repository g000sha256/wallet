package ru.g000sha256.wallet.feature.card

interface CardView {

    fun animateCardToBack()

    fun animateCardToFront()

    fun updateCardImages(back: String, front: String)

    fun updateCardState(isBackVisible: Boolean)

    fun updateCodeImage(image: String)

    fun updateInfo1Description(text: String)

    fun updateInfo1Title(text: String)

    fun updateInfo2Description(text: String)

    fun updateInfo2Title(text: String)

    fun updateInfo3Description(text: String)

    fun updateInfo3Title(text: String)

    fun updateInfo4Description(text: String)

    fun updateInfo4Title(text: String)

    fun updateInfoTitle(text: String)

    fun updateTitle(text: String)

}