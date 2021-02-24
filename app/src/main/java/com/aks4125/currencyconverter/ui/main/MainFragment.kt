package com.aks4125.currencyconverter.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.aks4125.currencyconverter.R
import com.aks4125.currencyconverter.data.CurrencyModel
import com.aks4125.currencyconverter.databinding.FragmentMainBinding
import com.aks4125.currencyconverter.model.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

const val TAG = "CurrencyConverter"

class MainFragment : Fragment(), LifecycleObserver {
    companion object {
        const val TAG: String = "CurrencyConverter"
    }

    private val scope = MainScope()
    private val mainViewModel: MainViewModel by viewModel()
    private val requestCycleInMillis: Long = 30 * 60 * 1000 // 30 Minutes

    private var mCurrencyList: MutableList<String> = mutableListOf()
    private var targetPriceText: Double = 1.0
    private var selectedRate: Double = 1.0
    private var updateCurrency: Boolean = false
    private var selectIndex: Int = 0
    private var job: Job? = null
    private var mAdapter: CurrencyAdapter? = null

    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container, false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    /**
     * start fetching updates every 30 mins
     */
    private fun startUpdates() {
        stopUpdates()
        job = scope.launch {
            while (true) {
                mainViewModel.refresh()
                delay(requestCycleInMillis)
            }
        }
    }

    /**
     * stop updates for requesting API
     */
    private fun stopUpdates() {
        job?.cancel()
        job = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * Register Input observers, recyclerview adapter, spinner adapter
         */
        setupUI()
        setupObservers()
        startUpdates()
    }

    private fun setupObservers() {
        mainViewModel.currencyData.observe(viewLifecycleOwner, currencyObserver)
        binding.viewModel = mainViewModel
    }

    private fun setupUI() {
        binding.edtPrice.doAfterTextChanged {
            val mVal = binding.edtPrice.text?.toString() ?: "1"
            val mAmount: Double = if (mVal.isEmpty()) 1.0
            else mVal.toDoubleOrNull() ?: 1.0
            targetPriceText = mAmount
            mAdapter?.updateInputPrice(mAmount)
        }

        mLayoutManager = GridLayoutManager(
            requireContext(),
            if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
        )
        binding.rvList.layoutManager = mLayoutManager
        mAdapter = CurrencyAdapter()
        // avoid flickering while updating UI
        mAdapter?.setHasStableIds(true)
        binding.rvList.adapter = mAdapter


        spinnerAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                mCurrencyList
            )
        binding.currencyMenu.adapter = spinnerAdapter

    }

    private val currencyObserver = Observer<CurrencyModel> {
        context?.let { _ ->
            mCurrencyList.clear()
            mCurrencyList.addAll(it.getCurrencyList())
            spinnerAdapter.notifyDataSetChanged()
            binding.currencyMenu.setSelection(
                if (updateCurrency) selectIndex else spinnerAdapter.getPosition(
                    getString(R.string.defaultSpinnerText)
                )
            ) //USD as default currency

            binding.currencyMenu.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // not required
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        pos: Int,
                        id: Long
                    ) {
                        /**
                         * keep track of selectedIndex & rate as selected currency info to keep it same upon refresh
                         */
                        selectIndex = binding.currencyMenu.selectedItemPosition
                        selectedRate = if (it.data.size > 0) it.data[selectIndex].rate else 0.0
                        updateCurrency = true
                        mAdapter?.updateSelectedRate(it.data[pos].rate)
                    }
                }
            Log.d(TAG, "data received -> $it")

            /**
             * update list without disturbing scrolled position
             */
            mAdapter?.items = it.data
        }
    }

    /**
     * 3 column for landscape for better UX
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        var spanSize = 2
        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            spanSize = 3

        mLayoutManager.spanCount = spanSize
    }
}