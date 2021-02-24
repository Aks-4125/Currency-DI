package com.aks4125.currencyconverter.di

import com.aks4125.currencyconverter.data.repository.dataRepositoryModule
import com.aks4125.currencyconverter.data.repository.mainRepositoryModule
import com.aks4125.currencyconverter.data.room.roomDbModule

/**
 * Main Koin DI component.
 * Helps to configure
 */
fun configureTestAppComponent(baseApi: String) = listOf(
    MockWebServerDIPTest,
    configureNetworkModuleForTest(baseApi),
    mainRepositoryModule,
    dataRepositoryModule,
    roomDbModule
)

