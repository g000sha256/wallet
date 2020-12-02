package ru.g000sha256.wallet.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import javax.inject.Inject
import javax.inject.Provider

class ViewModelProviderFactory<VM : ViewModel> @Inject constructor(
    private val viewModelProvider: Provider<VM>
) : ViewModelProvider.Factory {

    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        return viewModelProvider.get() as VM
    }

    inline fun <reified VM : ViewModel> get(viewModelStore: ViewModelStore): VM {
        return get(viewModelStore, VM::class.java)
    }

    fun <VM : ViewModel> get(viewModelStore: ViewModelStore, clazz: Class<VM>): VM {
        val viewModelProvider = ViewModelProvider(viewModelStore, this)
        return viewModelProvider[clazz]
    }

}