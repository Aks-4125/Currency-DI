package com.aks4125.currencyconverter.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aks4125.currencyconverter.BaseTest
import com.aks4125.currencyconverter.api.ApiInterface
import com.aks4125.currencyconverter.di.configureTestAppComponent
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.test.inject
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class MainRepositoryTest : BaseTest() {
    //Target
    private lateinit var mRepo: MainRepository

    //Inject api service created with Koin
    private val mAPIService: ApiInterface by inject()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start() {
        super.setUp()

        startKoin { modules(configureTestAppComponent(getMockWebServerUrl())) }
    }


    @Test
    fun test_fetch_all_currency_codes_with_fullName() = runBlocking {

        mockNetworkResponseWithFileContent("currency_api_responce.json", HttpURLConnection.HTTP_OK)
        mRepo = MainRepository(mAPIService)

        val dataReceived = mRepo.getAllTypes()

        assertNotNull(dataReceived)
        assertEquals(dataReceived.currencies.size, 168)
        assertEquals(dataReceived.currencies["INR"], "Indian Rupee")
        assertEquals(dataReceived.currencies["USD"], "United States Dollar")
    }

    @Test
    fun test_fetch_all_currency_rates() = runBlocking {

        mockNetworkResponseWithFileContent("currency_rate_response.json", HttpURLConnection.HTTP_OK)
        mRepo = MainRepository(mAPIService)

        val dataReceived = mRepo.getCurrencyRates()

        assertNotNull(dataReceived)
        assertEquals(dataReceived.quotes.size, 168)
        assertEquals(dataReceived.source, "USD")
        assertEquals(dataReceived.quotes["USDINR"], 72.55135)

    }

}