package com.aks4125.currencyconverter.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aks4125.currencyconverter.data.CurrencyDetail

@Dao
interface RoomDao {
    @Query("SELECT * from $DATABASE_TABLE")
    suspend fun getAllList(): List<CurrencyDetail>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAllRate(rateList: List<CurrencyDetail>)
}