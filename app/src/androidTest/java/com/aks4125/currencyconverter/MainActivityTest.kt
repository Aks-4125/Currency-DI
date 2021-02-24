package com.aks4125.currencyconverter

import android.os.SystemClock
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.aks4125.currencyconverter.di.MockWebServerInstrumentationTest
import com.aks4125.currencyconverter.ui.main.CurrencyAdapter
import com.aks4125.currencyconverter.ui.main.MainActivity
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.containsString
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.test.inject
import java.net.HttpURLConnection


@RunWith(AndroidJUnit4::class)
class MainActivityTest : BaseUITest() {
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    //Inject MockWebServer created with koin so that View will have corresponding dependencies while executing.
    @Suppress("unused")
    val mMockWebServer: MockWebServer by inject()

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java, true, false)
//    @get:Rule
//    @JvmField
//    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Before
    fun start() {
        super.setUp()
        loadKoinModules(MockWebServerInstrumentationTest)
    }


    @Test
    fun test_recyclerview_elements_for_expected_response() {
        mActivityTestRule.launchActivity(null)
        Assert.assertNotNull(mActivityTestRule.activity)

        mockNetworkResponseWithFileContent("currency_rate_response.json", HttpURLConnection.HTTP_OK)


        //Wait for MockWebServer to get back with response
        val recyclerView = withId(R.id.rvList)

        // wait for mock WebServer response
        SystemClock.sleep(1000)

        onView(withId(R.id.currency_menu)).check(matches(withSpinnerText(containsString("USD"))))

        onView(recyclerView)
            .check(matches(recyclerItemAtPosition(0, withText("AED"), R.id.txtCurrencyCode)))

        //Check if item at 2nd position is having 2nd element in json
        onView(recyclerView)
            .check(matches(recyclerItemAtPosition(1, withText("AFN"), R.id.txtCurrencyCode)))

        //Scroll to last index in json
        onView(recyclerView).perform(
            RecyclerViewActions.scrollToPosition<CurrencyAdapter.CurrencyHolder>(167)
        )

        //Check if item at last position is having last element in json
        onView(recyclerView)
            .check(matches(recyclerItemAtPosition(167, withText("ZWL"), R.id.txtCurrencyCode)))

        SystemClock.sleep(1000)

        mActivityTestRule.finishActivity()

    }


}
