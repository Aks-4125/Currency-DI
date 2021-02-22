package com.aks4125.currencyconverter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.aks4125.currencyconverter.R
import com.aks4125.currencyconverter.data.CurrencyDetail
import com.aks4125.currencyconverter.databinding.ListItemCurrencyBinding
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat

class CurrencyAdapter() : RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder>() {
    private val dec = DecimalFormat("#,###.##")
    private var mList: ArrayList<CurrencyDetail> = ArrayList()
    private var inputPrice: Double = 1.0
    private var selectedRate: Double = 1.0

    var items: MutableList<CurrencyDetail> = mList.toMutableList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item: ListItemCurrencyBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item_currency, parent, false)
        return CurrencyHolder(item)
    }

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        holder.bind(items[position])
    }


    inner class CurrencyHolder(private val binding: ListItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CurrencyDetail) {
            binding.currencyModel = item
            binding.amount = " ${dec.format(item.rate * inputPrice / selectedRate)}"

            binding.card.setOnClickListener {
                Snackbar.make(binding.root, item.name, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateSelectedRate(newRate: Double) {
        this.selectedRate = newRate
        notifyDataSetChanged()
    }

    fun updateInputPrice(newPrice: Double) {
        this.inputPrice = newPrice
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

}