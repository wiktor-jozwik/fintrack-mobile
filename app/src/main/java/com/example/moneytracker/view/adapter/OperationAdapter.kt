package com.example.moneytracker.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneytracker.databinding.OperatationBinding
import com.example.moneytracker.service.model.Operation
import java.time.format.DateTimeFormatter

class OperationAdapter(
        private val operations: List<Operation>
    ) : RecyclerView.Adapter<OperationAdapter.OperationViewHolder>() {
    inner class OperationViewHolder(val binding: OperatationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OperationViewHolder {
        val binding = OperatationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OperationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val curOperation = operations[position]

        holder.binding.apply {
            textName.text = curOperation.name
            textCategory.text = curOperation.category.name
            textMoneyAmount.text = "${curOperation.moneyAmount} ${curOperation.currency.symbol}"
            textDate.text = curOperation.date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))
        }
    }

    override fun getItemCount(): Int {
        return operations.size
    }
}