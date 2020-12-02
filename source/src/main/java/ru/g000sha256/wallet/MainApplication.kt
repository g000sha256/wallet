package ru.g000sha256.wallet

import android.app.Application
import ru.g000sha256.wallet.di.ApplicationComponent
import ru.g000sha256.wallet.di.DaggerApplicationComponent

lateinit var applicationComponent: ApplicationComponent

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent
            .builder()
            .mainApplication(this)
            .build()
    }

}