package com.aks4125.currencyconverter.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {

    single(override = true) { getApiInterface(get()) }
    single(override = true) { getRetrofit(get()) }
    single(override = true) { getOkHttpClient(get()) }
    single(override = true) { getHttpLoginInterceptor() }
}

fun baseUrl(): String {
    return "http://api.currencylayer.com/"
}

fun getApiInterface(retrofit: Retrofit): ApiInterface {
    return retrofit.create(ApiInterface::class.java)
}

fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}

fun getOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

}

fun getHttpLoginInterceptor(): HttpLoggingInterceptor {
    val httpLoginInterceptor = HttpLoggingInterceptor()
    httpLoginInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return httpLoginInterceptor
}
