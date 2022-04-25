package com.example.moneytracker.view.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.moneytracker.R
import com.example.moneytracker.service.model.Operation
import kotlinx.android.synthetic.main.operatation.view.*
import java.time.format.DateTimeFormatter

class OperationAdapter(
        private val operations: List<Operation>
    ) : RecyclerView.Adapter<OperationAdapter.OperationViewHolder>() {
    class OperationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OperationViewHolder {
        return OperationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.operatation,
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val curOperation = operations[position]
        holder.itemView.apply {
            tvName.text = curOperation.name
            tvCategory.text = curOperation.category.name
            tvMoneyAmount.text = curOperation.moneyAmount.toString()
            tvDate.text = curOperation.date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))
        }
    }

    override fun getItemCount(): Int {
        return operations.size
    }


}