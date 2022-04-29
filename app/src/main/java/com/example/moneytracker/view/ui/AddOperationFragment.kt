package com.example.moneytracker.view.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneytracker.R
import com.example.moneytracker.view.adapter.OperationAdapter
import com.example.moneytracker.viewmodel.AddOperationViewModel
import com.example.moneytracker.viewmodel.OperationListViewModel
import kotlinx.android.synthetic.main.fragment_add_operation.*
import kotlinx.android.synthetic.main.fragment_operation_list.*

class AddOperationFragment: Fragment(R.layout.fragment_add_operation) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addOperationViewModel = AddOperationViewModel()

        val currenciesNames = addOperationViewModel.getAllCurrencies().map { it.name }
        val categoriesNames = addOperationViewModel.getAllCategories().map { it.name }

        val currenciesAdapter = ArrayAdapter(activity as Context, android.R.layout.simple_spinner_item, currenciesNames)
        val categoriesAdapter = ArrayAdapter(activity as Context, android.R.layout.simple_spinner_item, categoriesNames)

        inputCurrency.adapter = currenciesAdapter
        inputCategory.adapter = categoriesAdapter

        val operationListFragment = OperationListFragment()

        buttonSave.setOnClickListener {
            addOperationViewModel.addNewOperation(
                inputName.text.toString(),
                inputMoneyAmount.text.toString().toDouble(),
                inputCategory.selectedItem.toString(),
                inputCurrency.selectedItem.toString()
            )

            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, operationListFragment)
                commit()
            }
        }
    }
}