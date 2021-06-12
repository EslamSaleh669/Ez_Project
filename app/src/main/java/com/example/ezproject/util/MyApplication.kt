package com.example.ezproject.util

import android.app.Application
import android.content.Context
import com.example.ezproject.BuildConfig
import com.example.ezproject.di.*
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import com.yariksoffice.lingver.Lingver
import timber.log.Timber

class MyApplication() : Application() {

    var appComponent: AppComponent? = null
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule(baseContext))
            .storageModule(StorageModule(baseContext))
            .viewModelFactoryModule(ViewModelFactoryModule()).build()

        Lingver.init(
            this,
            getSharedPreferences(
                Constants.SHARED_NAME,
                Context.MODE_PRIVATE
            ).getString(Constants.LANG_KEY, "en") ?: "en"
        )
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        RxPaparazzo.register(this);
    }
}