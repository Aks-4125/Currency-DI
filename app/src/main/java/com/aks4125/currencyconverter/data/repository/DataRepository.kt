package com.aks4125.currencyconverter.data.repository

import com.aks4125.currencyconverter.data.CurrencyDetail
import com.aks4125.currencyconverter.data.room.CurrencyDatabase
import com.aks4125.currencyconverter.data.room.RoomDao
import org.koin.dsl.module

val dataRepositoryModule = module {
    factory { DataRepository(get()) }
    factory { get<CurrencyDatabase>().currencyDao() }
}

/**
 * Unit test code coverage
 * DataRepository	100% (1/1)	100% (3/3)	100% (3/3)
 */
class DataRepository(private val mDao: RoomDao) {
    suspend fun loadRateList(): List<CurrencyDetail> = mDao.getAllList()
    suspend fun updateAllRate(saveList: List<CurrencyDetail>) = mDao.updateAllRate(saveList)
}