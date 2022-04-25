package com.example.moneytracker.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneytracker.R
import com.example.moneytracker.view.adapter.OperationAdapter
import com.example.moneytracker.viewmodel.OperationListViewModel
import kotlinx.android.synthetic.main.activity_operation_list.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operation_list)

        val operationListViewModel = OperationListViewModel()

        operationListViewModel.getOperationListObservable().observe(this) {
            rvOperationItems.adapter = OperationAdapter(it)
        }

        rvOperationItems.layoutManager = LinearLayoutManager(this)
    }
}