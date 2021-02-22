package com.aks4125.currencyconverter.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.aks4125.currencyconverter.data.room.DATABASE_TABLE
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val preferenceModule = module {
    single(override = true) { setupSharedPreferences(androidApplication()) }
}

private const val PREFERENCE_KEY = "${DATABASE_TABLE}_preferences"

private fun setupSharedPreferences(app: Application): SharedPreferences = app.getSharedPreferences(
    PREFERENCE_KEY, Context.MODE_PRIVATE)