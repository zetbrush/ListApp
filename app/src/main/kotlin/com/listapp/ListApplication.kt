package com.listapp

import android.app.Application
import com.listapp.core.di.dispatcherModule
import com.listapp.core.di.retrofitModule
import com.listfetch.impl.di.listFetchModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class ListApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ListApplication)
            modules(
                dispatcherModule +
                        retrofitModule +
                        listFetchModule
            )
        }
    }
}