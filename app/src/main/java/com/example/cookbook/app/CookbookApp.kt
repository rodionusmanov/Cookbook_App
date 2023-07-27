package com.example.cookbook.app

import android.app.Application
import com.example.cookbook.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CookbookApp:Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CookbookApp)
            modules(appModule)
        }
    }
}