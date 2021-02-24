package com.aks4125.currencyconverter.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aks4125.currencyconverter.BaseTest
import com.aks4125.currencyconverter.api.ApiInterface
import com.aks4125.currencyconverter.data.CurrencyRates
import com.aks4125.currencyconverter.data.CurrencyTypes
import com.aks4125.currencyconverter.data.repository.DataRepository
import com.aks4125.currencyconverter.data.repository.MainRepository
import com.aks4125.currencyconverter.di.configureTestAppComponent
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.test.inject
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest : BaseTest() {


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var mainViewModel: MainViewModel

    @Suppress("unused")
    private val mAPIService: ApiInterface by inject()

    @MockK
    lateinit var dataRepository: DataRepository

    @MockK
    lateinit var mainRepository: MainRepository

    private val dispatcher = TestCoroutineDispatcher()


    @Before
    override fun setUp() {
        super.setUp()
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        //Start Koin with required dependencies
        startKoin { modules(configureTestAppComponent(getMockWebServerUrl())) }
    }

    @After
    override fun tearDown() {
        super.tearDown()
        Dispatchers.resetMain()
    }

    @Test
    fun test_fetch_all_currency_types() {

        mainViewModel = MainViewModel(mainRepository, dataRepository)
        val sampleResponse = getJson("currency_api_responce.json")
        val jsonObj = Gson().fromJson(sampleResponse, CurrencyTypes::class.java)
        //Make sure login use case returns expected response when called
        coEvery { mainRepository.getAllTypes() } returns jsonObj
        mainViewModel.currencyData.observeForever { }

        mainViewModel.refresh()

        assert(mainViewModel.currencyData.value != null)
    }

    @Test
    fun test_fetch_all_currency_rates() {

        mainViewModel = MainViewModel(mainRepository, dataRepository)
        val sampleResponse = getJson("currency_rate_response.json")
        val jsonObj = Gson().fromJson(sampleResponse, CurrencyRates::class.java)
        //Make sure login use case returns expected response when called
        coEvery { mainRepository.getCurrencyRates() } returns jsonObj
        mainViewModel.currencyData.observeForever { }

        mainViewModel.refresh()

        assert(mainViewModel.currencyData.value != null)
    }


}