package com.example.moneytracker.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.moneytracker.R
import com.example.moneytracker.databinding.OperationBinding
import com.example.moneytracker.service.model.mt.CategoryType
import com.example.moneytracker.service.model.mt.Operation
import com.example.moneytracker.view.ui.utils.cutText
import java.time.format.DateTimeFormatter


class OperationListAdapter(
    operations: List<Operation>,
    private val deleteOnClickListener: OperationListAdapter.DeleteOnClickListener,
    private val editOnClickListener: OperationListAdapter.EditOnClickListener
) : RecyclerView.Adapter<OperationListAdapter.OperationViewHolder>() {
    private var operationsList: MutableList<Operation> = operations.toMutableList()

    inner class OperationViewHolder(val binding: OperationBinding) :
        RecyclerView.ViewHolder(binding.root)

    class DeleteOnClickListener(val clickListener: (position: Int) -> Unit) {
        fun onClick(position: Int) {
            clickListener(position)
        }
    }

    class EditOnClickListener(val clickListener: (operation: Operation) -> Unit) {
        fun onClick(operation: Operation) {
            clickListener(operation)
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
        val binding = OperationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OperationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val currentOperation = operationsList[position]

        val maxTextLength = 25

        holder.binding.apply {
            id.text = currentOperation.id.toString()
            textName.text = currentOperation.name.cutText(maxTextLength)
            textCategory.text = currentOperation.category.name.cutText(maxTextLength)
            textDate.text =
                currentOperation.date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            buttonDelete.setOnClickListener {
                deleteOnClickListener.onClick(currentOperation.id)
            }
            buttonEdit.setOnClickListener {
                editOnClickListener.onClick(currentOperation)
            }

            when (currentOperation.category.type) {
                CategoryType.INCOME -> {
                    textMoneyAmount.setTextColor(
                        ContextCompat.getColor(
                            textMoneyAmount.context,
                            R.color.main_green
                        )
                    )
                    textMoneyAmount.text =
                        "+${currentOperation.moneyAmount} ${currentOperation.currency.symbol}"
                }
                CategoryType.OUTCOME -> {
                    textMoneyAmount.setTextColor(
                        ContextCompat.getColor(
                            textMoneyAmount.context,
                            R.color.main_red
                        )
                    )
                    textMoneyAmount.text =
                        "-${currentOperation.moneyAmount} ${currentOperation.currency.symbol}"
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return operationsList.size
    }
}