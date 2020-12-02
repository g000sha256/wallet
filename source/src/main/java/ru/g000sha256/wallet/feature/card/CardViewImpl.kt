package ru.g000sha256.wallet.feature.card

import android.os.Build
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.bumptech.glide.RequestManager
import kotlin.math.max
import kotlin.math.min
import ru.g000sha256.wallet.R
import ru.g000sha256.wallet.extension.doOnPreDraw
import ru.g000sha256.wallet.extension.dp
import ru.g000sha256.wallet.extension.dpF
import ru.g000sha256.wallet.extension.isLargeScreen
import ru.g000sha256.wallet.extension.setHeight
import ru.g000sha256.wallet.extension.setMarginTop
import ru.g000sha256.wallet.extension.setOnInsetsChangedCallback
import ru.g000sha256.wallet.extension.updateText

class CardViewImpl(
    private val cardViewModel: CardViewModel,
    private val requestManager: RequestManager,
    view: View
) : CardView {

    private val cardBackImageView = view.findViewById<ImageView>(R.id.card_back_image_view)
    private val cardFrontImageView = view.findViewById<ImageView>(R.id.card_front_image_view)
    private val codeImageView = view.findViewById<ImageView>(R.id.code_image_view)
    private val info1DescriptionTextView = view.findViewById<TextView>(R.id.info_1_description_text_view)
    private val info1TitleTextView = view.findViewById<TextView>(R.id.info_1_title_text_view)
    private val info2DescriptionTextView = view.findViewById<TextView>(R.id.info_2_description_text_view)
    private val info2TitleTextView = view.findViewById<TextView>(R.id.info_2_title_text_view)
    private val info3DescriptionTextView = view.findViewById<TextView>(R.id.info_3_description_text_view)
    private val info3TitleTextView = view.findViewById<TextView>(R.id.info_3_title_text_view)
    private val info4DescriptionTextView = view.findViewById<TextView>(R.id.info_4_description_text_view)
    private val info4TitleTextView = view.findViewById<TextView>(R.id.info_4_title_text_view)
    private val infoTitleTextView = view.findViewById<TextView>(R.id.info_title_text_view)
    private val titleTextView = view.findViewById<TextView>(R.id.title_text_view)

    init {
        cardBackImageView.clipToOutline = true
        cardFrontImageView.clipToOutline = true
        cardFrontImageView.setOnClickListener { cardViewModel.clickOnCard() }
        val imageButton = view.findViewById<ImageButton>(R.id.image_button)
        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.nested_scroll_view)
        val bottomBarView = view.findViewById<View>(R.id.bottom_bar_view)
        val bottomDividerView = view.findViewById<View>(R.id.bottom_divider_view)
        val topBarView = view.findViewById<View>(R.id.top_bar_view)
        val topDividerView = view.findViewById<View>(R.id.top_divider_view)
        imageButton.setOnClickListener { cardViewModel.clickOnBack() }
        val onScrollChangeListener = NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            var scroll = max(0, scrollY)
            scroll = min(16.dp, scroll)
            topDividerView.alpha = scroll / 16.dpF
        }
        onScrollChangeListener.onScrollChange(nestedScrollView, 0, 0, 0, 0)
        nestedScrollView.setOnScrollChangeListener(onScrollChangeListener)
        if (view.resources.isLargeScreen) {
            bottomDividerView.visibility = View.GONE
            bottomBarView.setHeight(0)
            view.doOnPreDraw { nestedScrollView.setPadding(0, topBarView.height + topDividerView.height, 0, 0) }
        } else {
            view.setOnInsetsChangedCallback { bottom, top ->
                val isHideBottom = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && bottom == 0)
                bottomDividerView.visibility = if (isHideBottom) View.GONE else View.VISIBLE
                bottomBarView.setHeight(bottom)
                titleTextView.setMarginTop(top)
                view.doOnPreDraw {
                    val bottomDividerHeight = if (isHideBottom) 0 else bottomDividerView.height
                    val bottomPadding = bottomBarView.height + bottomDividerHeight
                    val topPadding = topBarView.height + topDividerView.height
                    nestedScrollView.setPadding(0, topPadding, 0, bottomPadding)
                }
            }
        }
    }

    override fun animateCardToBack() {
        flip(cardFrontImageView, cardBackImageView)
    }

    override fun animateCardToFront() {
        flip(cardBackImageView, cardFrontImageView)
    }

    override fun updateCardImages(back: String, front: String) {
        requestManager
            .load(back)
            .into(cardBackImageView)
        requestManager
            .load(front)
            .into(cardFrontImageView)
    }

    override fun updateCardState(isBackVisible: Boolean) {
        cardBackImageView.scaleX = if (isBackVisible) 1F else 0F
        cardFrontImageView.scaleX = if (isBackVisible) 0F else 1F
    }

    override fun updateCodeImage(image: String) {
        requestManager
            .load(image)
            .into(codeImageView)
    }

    override fun updateInfo1Description(text: String) {
        info1DescriptionTextView.updateText(text)
    }

    override fun updateInfo1Title(text: String) {
        info1TitleTextView.updateText(text)
    }

    override fun updateInfo2Description(text: String) {
        info2DescriptionTextView.updateText(text)
    }

    override fun updateInfo2Title(text: String) {
        info2TitleTextView.updateText(text)
    }

    override fun updateInfo3Description(text: String) {
        info3DescriptionTextView.updateText(text)
    }

    override fun updateInfo3Title(text: String) {
        info3TitleTextView.updateText(text)
    }

    override fun updateInfo4Description(text: String) {
        info4DescriptionTextView.updateText(text)
    }

    override fun updateInfo4Title(text: String) {
        info4TitleTextView.updateText(text)
    }

    override fun updateInfoTitle(text: String) {
        infoTitleTextView.updateText(text)
    }

    override fun updateTitle(text: String) {
        titleTextView.updateText(text)
    }

    private fun flip(view1: View, view2: View) {
        view1
            .animate()
            .setDuration(100L)
            .scaleX(0F)
            .withEndAction {
                view2
                    .animate()
                    .setDuration(100L)
                    .scaleX(1F)
                    .withEndAction { cardViewModel.animationEnded() }
            }
    }

}