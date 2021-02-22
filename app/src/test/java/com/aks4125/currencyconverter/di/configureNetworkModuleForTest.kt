package com.aks4125.currencyconverter.di

import com.aks4125.currencyconverter.api.ApiInterface
import com.google.gson.GsonBuilder
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Network module test configuration with mockserver url.
 */
fun configureNetworkModuleForTest(baseApi: String) = module {
    single(override = true) {
        Retrofit.Builder()
            .baseUrl(baseApi)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }
    factory { get<Retrofit>().create(ApiInterface::class.java) }
}