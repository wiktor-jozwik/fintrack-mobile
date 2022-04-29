package com.example.moneytracker.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneytracker.R
import com.example.moneytracker.view.adapter.OperationAdapter
import com.example.moneytracker.viewmodel.OperationListViewModel
import kotlinx.android.synthetic.main.fragment_operation_list.*

class OperationListFragment : Fragment(R.layout.fragment_operation_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val operationListViewModel = OperationListViewModel()

        operationListViewModel.getAllOperationsObservable().observe(viewLifecycleOwner) {
            recyclerViewOperationItems.adapter = OperationAdapter(it)
        }

        recyclerViewOperationItems.layoutManager = LinearLayoutManager(activity)
    }
}