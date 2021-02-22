package com.aks4125.currencyconverter.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aks4125.currencyconverter.BaseTest
import com.aks4125.currencyconverter.data.CurrencyDetail
import com.aks4125.currencyconverter.data.CurrencyModel
import com.aks4125.currencyconverter.data.CurrencyRates
import com.aks4125.currencyconverter.data.room.RoomDao
import com.aks4125.currencyconverter.di.configureTestAppComponent
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.test.inject
import org.mockito.Mockito

@RunWith(JUnit4::class)
class DataRepositoryTest : BaseTest() {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    //Inject api service created with koin
    val dao: RoomDao by inject()

    @MockK
    lateinit var db: DataRepository

    @Before
    fun start() {
        super.setUp()

        startKoin {
            androidContext(Mockito.mock(Context::class.java))
            modules(configureTestAppComponent(getMockWebServerUrl()))
        }
    }

    @Test
    fun test_update_fetch_rateList() = runBlocking {
        val sampleResponse = getJson("currency_rate_response.json")
        val gson = Gson()
        val jsonObj = gson.fromJson(sampleResponse, CurrencyRates::class.java)
        db = DataRepository(spyk())

        val mList: ArrayList<CurrencyDetail> = ArrayList()
        for (typeKey in jsonObj.quotes.keys.toList()) {
            mList.add(
                CurrencyDetail(
                    code = typeKey,
                    name = typeKey,
                    rate = jsonObj.quotes[typeKey] ?: 1.0
                )
            )
        }
        val model = CurrencyModel(mList)
        assertNotNull(model.getCurrencyList())

        assertEquals(model.getCurrencyList().size, 168)

        // update list
        coEvery { db.updateAllRate(mList) } returns Unit
        assertNotNull(db.updateAllRate(mList))

        //fetch list
        coEvery { db.loadRateList() } returns mList
        assertNotNull(db.loadRateList())
        assertEquals(jsonObj.quotes.size, 168)
        assertEquals(db.loadRateList().size, 168)

        assertEquals(db.loadRateList()[0].code, "USDAED")
        assertEquals(db.loadRateList()[0].rate, 3.673104, Double.POSITIVE_INFINITY)

    }

}