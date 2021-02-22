package com.aks4125.currencyconverter

import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import java.io.BufferedReader
import java.io.Reader

abstract class BaseUITest : KoinTest {
    /**
     * For MockWebServer instance
     */
    private lateinit var mMockServerInstance: MockWebServer

    @Before
    open fun setUp() {
        startMockServer()
    }

    /**
     * Helps to read input file returns the respective data in mocked call
     */
    fun mockNetworkResponseWithFileContent(fileName: String, responseCode: Int) =
        mMockServerInstance.enqueue(

            MockResponse()
                .setResponseCode(responseCode)
                .setBody(getJson(fileName))
        )

    /**
     * Reads input file and converts to json
     */
    fun getJson(path: String): String {
        var content: String = ""
        val testContext = InstrumentationRegistry.getInstrumentation().targetContext
        val inputStream = testContext.assets.open(path)
        val reader = BufferedReader(inputStream.reader() as Reader?)
        reader.use { reader ->
            content = reader.readText()
        }
        return content
    }


    /**
     * Start Mockwebserver
     */
    private fun startMockServer() {
        mMockServerInstance = MockWebServer()
        mMockServerInstance.start()

    }

    /**
     * Set Mockwebserver url
     */
    fun getMockWebServerUrl() = mMockServerInstance.url("/").toString()

    /**
     * Stop Mockwebserver
     */
    private fun stopMockServer() {
        mMockServerInstance.shutdown()

    }

    @After
    open fun tearDown() {
        //Stop Mock server
        stopMockServer()
        //Stop Koin as well
        stopKoin()
    }

    /**
     * Custom matcher for recyclerview testing.
     */
    fun recyclerItemAtPosition(
        position: Int,
        @NonNull itemMatcher: Matcher<View>,
        targetViewId: Int
    ): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: return false
                val targetView = viewHolder.itemView.findViewById<View>(targetViewId)
                val text = (targetView as TextView).text
                return itemMatcher.matches(targetView)
            }
        }
    }
}