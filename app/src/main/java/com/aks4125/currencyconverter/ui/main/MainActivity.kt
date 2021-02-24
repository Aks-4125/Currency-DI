package com.aks4125.currencyconverter.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aks4125.currencyconverter.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         *   Navigation component will handle single fragment navigation
         */


        /**
         * covered:
         *          * Unit Test + UI Test
         *          * SonarQube code quality (A)
         *          * Offline compatibility
         *          * Retry/Empty API support
         *          * Code Analyzer
         *          * Jacoco code coverage configuration
         *
         * main libraries used:
         *          * Coroutines
         *          * Retrofit
         *          * MockWebServer
         *          * Koin
         *
         *
         * Note: Add your API key to gradle.properties > API_ACCESS_KEY
         * Debug APK added to root dir of the repository to test
         */


    }
}