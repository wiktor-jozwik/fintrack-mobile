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

class OperationListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_operation_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val operationListViewModel = OperationListViewModel()

        operationListViewModel.getOperationListObservable().observe(viewLifecycleOwner) {
            rvOperationItems.adapter = OperationAdapter(it)
        }

        rvOperationItems.layoutManager = LinearLayoutManager(activity)
    }
}