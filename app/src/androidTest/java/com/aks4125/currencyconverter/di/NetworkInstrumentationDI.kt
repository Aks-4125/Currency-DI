package com.aks4125.currencyconverter.di

import com.aks4125.currencyconverter.api.ApiInterface
import com.google.gson.GsonBuilder
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit Koin DI component for Instrumentation Testing
 */
@Suppress("unused")
fun configureNetworkForInstrumentationTest(baseTestApi: String) = module {

    single(override = true) {
        Retrofit.Builder()
            .baseUrl(baseTestApi)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }
    factory { get<Retrofit>().create(ApiInterface::class.java) }
}

