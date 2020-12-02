package ru.g000sha256.wallet.view_model

import androidx.lifecycle.ViewModel
import ru.g000sha256.wallet.common.Jobs

abstract class BaseViewModel<Router, View> : ViewModel() {

    protected val jobs = Jobs()

    protected var router: Router? = null
    protected var view: View? = null

    override fun onCleared() {
        jobs.clear()
    }

    open fun attach(router: Router, view: View) {
        this.router = router
        this.view = view
    }

    fun detach() {
        this.router = null
        this.view = null
    }

}