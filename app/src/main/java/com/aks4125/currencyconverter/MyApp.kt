package com.aks4125.currencyconverter

import android.app.Application
import com.aks4125.currencyconverter.api.retrofitModule
import com.aks4125.currencyconverter.data.preferenceModule
import com.aks4125.currencyconverter.data.repository.dataRepositoryModule
import com.aks4125.currencyconverter.data.repository.mainRepositoryModule
import com.aks4125.currencyconverter.data.room.roomDbModule
import com.aks4125.currencyconverter.model.mainViewModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                provideModules()
            )

        }
    }

    open fun provideModules() = listOf(
        retrofitModule, //network
        roomDbModule, //database module
        mainRepositoryModule, //main repository
        dataRepositoryModule, //database repository
        preferenceModule, //preference
        mainViewModule // view model
    )

}