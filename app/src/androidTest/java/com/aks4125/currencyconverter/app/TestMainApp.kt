package com.aks4125.currencyconverter.app

import com.aks4125.currencyconverter.MyApp
import org.koin.core.module.Module

/**
 * Helps to configure required dependencies for Instru Tests.
 * Method provideDependency can be overrided and new dependencies can be supplied.
 */
class TestMainApp : MyApp() {
    @Suppress("unused")
    fun provideDependency() = listOf<Module>()
}