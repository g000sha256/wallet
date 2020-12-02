package ru.g000sha256.wallet.feature.cards

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.CheckBox
import android.widget.RadioButton
import ru.g000sha256.wallet.R

class CardsDialog(context: Context, cardsSort: CardsSort, showCategories: Boolean) : Dialog(context) {

    private var sortCallback: (CardsSort) -> Unit = {}
    private var showCategoriesCallback: (Boolean) -> Unit = {}

    init {
        val window = window!!
        window.setBackgroundDrawableResource(R.color.transparent)
        window.setDimAmount(0.8F)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater
            .from(context)
            .inflate(R.layout.feature_cards_dialog, null)
        setContentView(view)
        view
            .findViewById<View>(R.id.image_button)
            .setOnClickListener { cancel() }
        val showCategoriesCheckBox = view.findViewById<CheckBox>(R.id.show_categories_check_box)
        val sortByAddDateRadioButton = view.findViewById<RadioButton>(R.id.sort_by_add_date_radio_button)
        val sortByLastUsedRadioButton = view.findViewById<RadioButton>(R.id.sort_by_last_used_radio_button)
        val sortByNameRadioButton = view.findViewById<RadioButton>(R.id.sort_by_name_radio_button)
        val sortByPopularRadioButton = view.findViewById<RadioButton>(R.id.sort_by_popular_radio_button)
        showCategoriesCheckBox.isChecked = showCategories
        sortByAddDateRadioButton.isChecked = cardsSort == CardsSort.ADD_DATE
        sortByLastUsedRadioButton.isChecked = cardsSort == CardsSort.LAST_USED
        sortByNameRadioButton.isChecked = cardsSort == CardsSort.NAME
        sortByPopularRadioButton.isChecked = cardsSort == CardsSort.POPULAR
        showCategoriesCheckBox.setOnCheckedChangeListener { _, isChecked -> showCategoriesCallback(isChecked) }
        sortByAddDateRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) return@setOnCheckedChangeListener
            sortCallback(CardsSort.ADD_DATE)
            sortByLastUsedRadioButton.isChecked = false
            sortByNameRadioButton.isChecked = false
            sortByPopularRadioButton.isChecked = false
        }
        sortByLastUsedRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) return@setOnCheckedChangeListener
            sortCallback(CardsSort.LAST_USED)
            sortByAddDateRadioButton.isChecked = false
            sortByNameRadioButton.isChecked = false
            sortByPopularRadioButton.isChecked = false
        }
        sortByNameRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) return@setOnCheckedChangeListener
            sortCallback(CardsSort.NAME)
            sortByAddDateRadioButton.isChecked = false
            sortByLastUsedRadioButton.isChecked = false
            sortByPopularRadioButton.isChecked = false
        }
        sortByPopularRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) return@setOnCheckedChangeListener
            sortCallback(CardsSort.POPULAR)
            sortByAddDateRadioButton.isChecked = false
            sortByLastUsedRadioButton.isChecked = false
            sortByNameRadioButton.isChecked = false
        }
    }

    fun setShowCategoriesCallback(callback: (Boolean) -> Unit) {
        showCategoriesCallback = callback
    }

    fun setSortCallback(callback: (CardsSort) -> Unit) {
        sortCallback = callback
    }

}