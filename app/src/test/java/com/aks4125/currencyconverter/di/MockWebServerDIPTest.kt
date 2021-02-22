package com.aks4125.currencyconverter.model

import okhttp3.mockwebserver.MockWebServer
import org.koin.dsl.module

/**
 * Creates Mockwebserver instance for testing
 */
val MockWebServerDIPTest = module {

    factory {
        MockWebServer()
    }

}