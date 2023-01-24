package com.example.fintrack.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fintrack.databinding.CurrencyBinding
import com.example.fintrack.service.model.ft.Currency


class CurrencyListAdapter(
    currencies: List<Currency>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<CurrencyListAdapter.CurrencyViewHolder>() {
    private var userCurrenciesList: MutableList<Currency> = currencies.toMutableList()

    inner class CurrencyViewHolder(val binding: CurrencyBinding) :
        RecyclerView.ViewHolder(binding.root)

    class OnClickListener(val clickListener: (position: Int) -> Unit) {
        fun onClick(position: Int) {
            clickListener(position)
        }
    }

    internal fun deleteUserCurrency(userCurrencyId: Int) {
        val position = userCurrenciesList.map {
            it.id
        }.indexOf(userCurrencyId)

        if (position != -1) {
            userCurrenciesList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CurrencyViewHolder {
        val binding = CurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currentUserCurrency = userCurrenciesList[position]

        holder.binding.apply {
            id.text = currentUserCurrency.id.toString()
            textName.text = currentUserCurrency.name
            textSymbol.text = currentUserCurrency.symbol
            buttonDelete.setOnClickListener {
                onClickListener.onClick(currentUserCurrency.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return userCurrenciesList.size
    }
}