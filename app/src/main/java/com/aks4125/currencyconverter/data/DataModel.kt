package com.aks4125.currencyconverter.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aks4125.currencyconverter.data.room.DATABASE_TABLE
import com.google.gson.annotations.SerializedName


data  class CurrencyTypes (
    @SerializedName("currencies") val currencies: Map<String, String>
)

data class CurrencyRates(
    @SerializedName("source") val source: String,
    @SerializedName("quotes") val quotes: Map<String, Double>
)


@Entity(tableName = DATABASE_TABLE)
data class CurrencyDetail(
    @PrimaryKey
    @ColumnInfo(name = "code")
    var code: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "rate")
    var rate: Double = 1.0
)


data class CurrencyModel(
    var data: MutableList<CurrencyDetail> = mutableListOf()
) {
    fun getCurrencyList(): MutableList<String> {
        val rtKeys: MutableList<String> = mutableListOf()
        for (info in data) {
            val nameKey = "[${info.code}] ${info.name}"
            rtKeys.add(nameKey)
        }
        return rtKeys
    }
}