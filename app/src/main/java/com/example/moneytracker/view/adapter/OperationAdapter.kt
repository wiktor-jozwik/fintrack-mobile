package com.example.moneytracker.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneytracker.R
import com.example.moneytracker.databinding.OperatationBinding
import com.example.moneytracker.service.model.Operation
import java.time.format.DateTimeFormatter

class OperationAdapter(
        private val operations: List<Operation>
    ) : RecyclerView.Adapter<OperationAdapter.OperationViewHolder>() {
    private lateinit var binding: OperatationBinding

    inner class OperationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OperationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = OperatationBinding.inflate(inflater)

        return OperationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.operatation,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val curOperation = operations[position]
        holder.itemView.apply {
            binding.textName.text = curOperation.name
            binding.textCategory.text = curOperation.category.name
            binding.textMoneyAmount.text = "${curOperation.moneyAmount} ${curOperation.currency.symbol}"
            binding.textDate.text = curOperation.date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))
        }
    }

    override fun getItemCount(): Int {
        return operations.size
    }
}