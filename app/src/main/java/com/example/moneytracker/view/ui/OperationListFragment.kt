package com.example.moneytracker.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentAddOperationBinding
import com.example.moneytracker.databinding.FragmentOperationListBinding
import com.example.moneytracker.view.adapter.OperationAdapter
import com.example.moneytracker.viewmodel.AddOperationViewModel
import com.example.moneytracker.viewmodel.OperationListViewModel

class OperationListFragment : Fragment(R.layout.fragment_operation_list) {
    private var _binding: FragmentOperationListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOperationListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val operationListViewModel = OperationListViewModel()

        operationListViewModel.getAllOperations().observe(viewLifecycleOwner) {
            binding.recyclerViewOperationItems.adapter = OperationAdapter(it)
        }

        binding.recyclerViewOperationItems.layoutManager = LinearLayoutManager(activity)
    }
}