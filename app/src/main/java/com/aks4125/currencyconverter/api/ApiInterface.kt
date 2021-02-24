package com.aks4125.currencyconverter.api

import com.aks4125.currencyconverter.BuildConfig
import com.aks4125.currencyconverter.data.CurrencyRates
import com.aks4125.currencyconverter.data.CurrencyTypes
import retrofit2.http.GET

interface ApiInterface {
    @GET("/api/list?access_key=${BuildConfig.API_ACCESS_KEY}&format=1")
    suspend fun getCurrencyTypes(): CurrencyTypes

    @GET("/api/live?access_key=${BuildConfig.API_ACCESS_KEY}&format=1")
    suspend fun getCurrencyRates(): CurrencyRates
}