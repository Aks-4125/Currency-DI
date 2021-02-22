package com.aks4125.currencyconverter.data.repository

import com.aks4125.currencyconverter.api.ApiInterface
import org.koin.dsl.module

val mainRepositoryModule = module {
    factory { MainRepository(get()) }
}

/**
 * Unit test code coverage
 * MainRepository	100% (1/1)	100% (3/3)	100% (3/3)
 */
class MainRepository(private val mApi: ApiInterface) {
    suspend fun getAllTypes() = mApi.getCurrencyTypes()
    suspend fun getCurrencyRates() = mApi.getCurrencyRates()
}
