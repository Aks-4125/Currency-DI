package com.aks4125.currencyconverter.model

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.*
import com.aks4125.currencyconverter.data.CurrencyDetail
import com.aks4125.currencyconverter.data.CurrencyModel
import com.aks4125.currencyconverter.data.repository.DataRepository
import com.aks4125.currencyconverter.data.repository.MainRepository
import com.aks4125.currencyconverter.ui.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.dsl.module
import java.util.*

val mainViewModule = module {
    factory { MainViewModel(get(), get()) }
}

/**
 * Unit test code coverage
 * MainViewModel    100% (4/4)	100% (6/6)
 */
class MainViewModel(
    private val mainRepository: MainRepository,
    private val dataRepository: DataRepository
) : ViewModel(), KoinComponent {

    private val preferences: SharedPreferences by inject()
    private val refresh = MutableLiveData<Boolean>()

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _onMessageError = MutableLiveData<String>()
    val onMessageError: LiveData<String> = _onMessageError


    val currencyData: LiveData<CurrencyModel> = Transformations.switchMap(refresh) {
        liveData {
            val mData = CurrencyModel()
            try {
                setLoadingVisibility(true)
                val lastSave = preferences.getLong("logTime", -1)
                val period = 5 * 60 * 1000
                var logTime = Calendar.getInstance().time.time
                if ((lastSave + period) > logTime) {
                    // If the last time successfully fetch the data was less than 30-minutes ago,
                    // get the data from Room
                    Log.d(TAG, "Reload data from Database")
                    mData.data.addAll(dataRepository.loadRateList())
                } else {
                    // If the last time successfully fetch the data was more than 30-minutes ago,
                    // fetch the data from api
                    Log.d(TAG, "Requesting API")
                    val currencyTypes = mainRepository.getAllTypes()
                    val mRates = mainRepository.getCurrencyRates()
                    for (typeKey in currencyTypes.currencies.keys.toList()) {
                        // full key = source type code + target type code
                        val fullKey = "${mRates.source}${typeKey}"
                        // Before create and add any CurrencyInfo object,
                        // will check if we can find the key in the map of the rates
                        if (mRates.quotes.containsKey(fullKey)) {
                            mData.data.add(
                                CurrencyDetail(
                                    code = typeKey,
                                    name = currencyTypes.currencies[typeKey] ?: "",
                                    rate = mRates.quotes[fullKey] ?: 1.0
                                )
                            )
                        }
                    }
                    dataRepository.updateAllRate(mData.data)

                    // Log the time in SharedPreferences only if list is not empty due to server response
                    if (mData.data.isNotEmpty()) {
                        logTime = Calendar.getInstance().time.time
                        preferences.edit { putLong("logTime", logTime) }
                        Log.d(TAG, "Fetched data from API")
                    }
                }
                setLoadingVisibility(false)
            } catch (exception: Throwable) {
                setLoadingVisibility(false)
                _onMessageError.value = exception.message
                Log.e(TAG, exception.message!!)
            }
            emit(mData)
        }
    }

    private fun setLoadingVisibility(visible: Boolean) {
        _isViewLoading.value = visible
    }

    fun refresh() {
        refresh.value = true
    }

}