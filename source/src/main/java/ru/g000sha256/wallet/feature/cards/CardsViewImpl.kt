package ru.g000sha256.wallet.feature.cards

import android.app.Dialog
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.RequestManager
import kotlin.math.max
import kotlin.math.min
import ru.g000sha256.wallet.R
import ru.g000sha256.wallet.extension.doOnPreDraw
import ru.g000sha256.wallet.extension.dp
import ru.g000sha256.wallet.extension.dpF
import ru.g000sha256.wallet.extension.hideKeyboard
import ru.g000sha256.wallet.extension.setHeight
import ru.g000sha256.wallet.extension.setOnInsetsChangedCallback
import ru.g000sha256.wallet.extension.updateText
import ru.g000sha256.wallet.feature.cards.adapter.CardsAdapter
import ru.g000sha256.wallet.feature.cards.adapter.CardsItem

class CardsViewImpl(private val cardsViewModel: CardsViewModel, requestManager: RequestManager, view: View) : CardsView {

    private val editText = view.findViewById<EditText>(R.id.edit_text)
    private val clearImageButton = view.findViewById<ImageButton>(R.id.clear_image_button)
    private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
    private val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
    private val errorView = view.findViewById<View>(R.id.error_view)

    private val cardsAdapter: CardsAdapter
    private val context = view.context
    private val resources = view.resources

    private var dialog: Dialog? = null
    private var toast: Toast? = null

    init {
        val settingsImageButton = view.findViewById<ImageButton>(R.id.settings_image_button)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val titleTextView = view.findViewById<TextView>(R.id.title_text_view)
        val bottomBarView = view.findViewById<View>(R.id.bottom_bar_view)
        val bottomDividerView = view.findViewById<View>(R.id.bottom_divider_view)
        val statusBarView = view.findViewById<View>(R.id.status_bar_view)
        val topBarView = view.findViewById<View>(R.id.top_bar_view)
        val topDividerView = view.findViewById<View>(R.id.top_divider_view)
        clearImageButton.alpha = 0F
        clearImageButton.setOnClickListener { cardsViewModel.clickOnClear() }
        val spanCount = calculateSpanCount()
        val carouselCardWidth = calculateCarouselCardWidth(spanCount)
        val listCardWidth = ViewGroup.LayoutParams.MATCH_PARENT
        cardsAdapter = CardsAdapter(cardsViewModel, carouselCardWidth, listCardWidth, requestManager)
        recyclerView.adapter = cardsAdapter
        val layoutManager = GridLayoutManager(context, spanCount)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {
                val viewType = cardsAdapter.getItemViewType(position)
                when (viewType) {
                    CardsAdapter.ViewType.CARD -> return 1
                    CardsAdapter.ViewType.CAROUSEL -> return spanCount
                    CardsAdapter.ViewType.HEADER -> return spanCount
                    else -> throw IllegalArgumentException()
                }
            }

        }
        recyclerView.layoutManager = layoutManager
        val onScrollListener = object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                var scroll = recyclerView.computeVerticalScrollOffset()
                scroll = max(0, scroll)
                scroll = min(16.dp, scroll)
                topDividerView.alpha = scroll / 16.dpF
            }

        }
        onScrollListener.onScrolled(recyclerView, 0, 0)
        recyclerView.addOnScrollListener(onScrollListener)
        val textWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                cardsViewModel.updateSearch(s)
            }

            override fun afterTextChanged(s: Editable) {}

        }
        editText.addTextChangedListener(textWatcher)
        settingsImageButton.setOnClickListener { cardsViewModel.clickOnSettings() }
        swipeRefreshLayout.setColorSchemeResources(R.color.lime)
        swipeRefreshLayout.setOnRefreshListener { cardsViewModel.refresh() }
        topBarView.setOnClickListener { recyclerView.smoothScrollToPosition(0) }
        errorView
            .findViewById<View>(R.id.button)
            .setOnClickListener { cardsViewModel.clickOnRetry() }
        view.setOnInsetsChangedCallback { bottom, top ->
            val isHideBottom = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && bottom == 0)
            bottomDividerView.visibility = if (isHideBottom) View.GONE else View.VISIBLE
            bottomBarView.setHeight(bottom)
            statusBarView.setHeight(top)
            if (bottom > 100.dp) {
                settingsImageButton.visibility = View.GONE
                titleTextView.visibility = View.GONE
            } else {
                settingsImageButton.visibility = View.VISIBLE
                titleTextView.visibility = View.VISIBLE
                hideKeyboard()
            }
            view.doOnPreDraw {
                val padding = 4.dp
                val bottomDividerHeight = if (isHideBottom) 0 else bottomDividerView.height
                val bottomPadding = bottomBarView.height + bottomDividerHeight + padding
                val topPadding = topBarView.height + topDividerView.height + padding
                recyclerView.setPadding(padding, topPadding, padding, bottomPadding)
                swipeRefreshLayout.setProgressViewOffset(true, topPadding, topPadding + 32.dp)
            }
        }
    }

    override fun hideKeyboard() {
        editText.hideKeyboard()
        editText.clearFocus()
    }

    override fun showContent(withAnimation: Boolean, diffResult: DiffUtil.DiffResult, cardsItems: List<CardsItem>) {
        editText.isEnabled = true
        swipeRefreshLayout.isEnabled = true
        cardsAdapter.setCardsItems(cardsItems)
        diffResult.dispatchUpdatesTo(cardsAdapter)
        errorView.animate(false, withAnimation)
        progressBar.animate(false, withAnimation)
        swipeRefreshLayout.animate(true, withAnimation)
    }

    override fun showError(withAnimation: Boolean) {
        editText.isEnabled = false
        swipeRefreshLayout.isEnabled = false
        errorView.animate(true, withAnimation)
        progressBar.animate(false, withAnimation)
        swipeRefreshLayout.animate(false, withAnimation)
    }

    override fun showProgress(withAnimation: Boolean) {
        editText.isEnabled = false
        swipeRefreshLayout.isEnabled = false
        errorView.animate(false, withAnimation)
        progressBar.animate(true, withAnimation)
        swipeRefreshLayout.animate(false, withAnimation)
    }

    override fun showSettings(cardsSort: CardsSort, showCategories: Boolean) {
        if (dialog != null) return
        dialog = CardsDialog(context, cardsSort, showCategories)
            .apply {
                setOnCancelListener {
                    cardsViewModel.dialogCanceled()
                    dismissDialog()
                }
                setSortCallback { cardsViewModel.selectSort(it) }
                setShowCategoriesCallback { cardsViewModel.selectShowCategories(it) }
                show()
            }
    }

    override fun showToast(messageId: Int) {
        cancelToast()
        toast = Toast(context)
            .apply {
                val textView = LayoutInflater
                    .from(context)
                    .inflate(R.layout.toast, null) as TextView
                textView.setText(messageId)
                duration = Toast.LENGTH_SHORT
                view = textView
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
    }

    override fun updateSearch(text: CharSequence?) {
        editText.updateText(text)
    }

    override fun visibleClear(isVisible: Boolean) {
        if (clearImageButton.tag == isVisible) return
        clearImageButton.tag = isVisible
        clearImageButton.isClickable = isVisible
        val alpha = if (isVisible) 1F else 0F
        if (isVisible) clearImageButton.visibility = View.VISIBLE
        clearImageButton
            .animate()
            .setDuration(100L)
            .alpha(alpha)
            .withEndAction { if (!isVisible) clearImageButton.visibility = View.GONE }
    }

    override fun visibleRefresh(isVisible: Boolean) {
        swipeRefreshLayout.isRefreshing = isVisible
    }

    fun destroy() {
        cancelToast()
        dismissDialog()
    }

    private fun calculateCarouselCardWidth(spanCount: Int): Int {
        return (resources.displayMetrics.widthPixels - 8.dp - 16.dp) / spanCount
    }

    private fun calculateSpanCount(): Int {
        val spanCount = (resources.displayMetrics.widthPixels - 8.dp) / 208.dp
        return max(2, spanCount)
    }

    private fun cancelToast() {
        toast?.cancel()
        toast = null
    }

    private fun dismissDialog() {
        dialog?.dismiss()
        dialog = null
    }

    private fun View.animate(isVisible: Boolean, withAnimation: Boolean) {
        val view = this
        val alpha = if (isVisible) 1F else 0F
        if (withAnimation) {
            view.visibility = View.VISIBLE
            val delay = if (isVisible) 100L else 0L
            view
                .animate()
                .setDuration(200L)
                .setStartDelay(delay)
                .alpha(alpha)
                .withEndAction { if (!isVisible) view.visibility = View.GONE }
        } else {
            view.alpha = alpha
            view.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }

}