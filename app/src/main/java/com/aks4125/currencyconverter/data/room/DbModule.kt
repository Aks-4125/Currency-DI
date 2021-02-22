package com.aks4125.currencyconverter.data.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aks4125.currencyconverter.data.CurrencyDetail
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

const val DATABASE_TABLE = "currency_table"

val roomDbModule = module {
    single(override = true) {
        Room.databaseBuilder(androidContext(), CurrencyDatabase::class.java, "CurrencyTable.db")
            .build()
    }
}

@Database(entities = [CurrencyDetail::class], version = 1)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): RoomDao
}