package com.example.moneytracker.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneytracker.databinding.OperatationBinding
import com.example.moneytracker.service.model.Operation
import java.time.format.DateTimeFormatter


class OperationListAdapter(
        operations: List<Operation>,
        private val onClickListener: OnClickListener
    ) : RecyclerView.Adapter<OperationListAdapter.OperationViewHolder>() {
    private var operationsList: MutableList<Operation> = operations.toMutableList()

    inner class OperationViewHolder(val binding: OperatationBinding) : RecyclerView.ViewHolder(binding.root)
    class OnClickListener(val clickListener: (position: Int) -> Unit) {
        fun onClick(position: Int) {
            clickListener(position)
        }
    }

    internal fun deleteOperation(operationId: Int) {
        val position = operationsList.map {
            it.id
        }.indexOf(operationId)

        if (position != -1) {
            operationsList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OperationViewHolder {
        val binding = OperatationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OperationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val currentOperation = operationsList[position]

        holder.binding.apply {
            id.text = currentOperation.id.toString()
            textName.text = currentOperation.name
            textCategory.text = currentOperation.category.name
            textMoneyAmount.text = "${currentOperation.moneyAmount} ${currentOperation.currency.symbol}"
            textDate.text = currentOperation.date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))
            buttonDelete.setOnClickListener {
                onClickListener.onClick(currentOperation.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return operationsList.size
    }
}