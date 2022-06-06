package com.example.moneytracker.view.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentOperationListBinding
import com.example.moneytracker.view.adapter.OperationListAdapter
import com.example.moneytracker.viewmodel.OperationListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OperationListFragment : Fragment(R.layout.fragment_operation_list) {
    private val operationListViewModel: OperationListViewModel by viewModels()

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

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deleteLambda = OperationListAdapter.OnClickListener { operationId ->
            deleteOperation(
                operationId,
                binding.recyclerViewOperationItems.adapter as OperationListAdapter
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            operationListViewModel.getAllOperations().observe(viewLifecycleOwner) {

                binding.recyclerViewOperationItems.adapter = OperationListAdapter(
                    it,
                    deleteLambda
                )
            }
            binding.recyclerViewOperationItems.adapter!!.notifyDataSetChanged()
        }

        binding.recyclerViewOperationItems.adapter = OperationListAdapter(
            listOf(),
            deleteLambda
        )
        binding.recyclerViewOperationItems.layoutManager = LinearLayoutManager(context)
    }

    private fun deleteOperation(operationId: Int, adapter: OperationListAdapter) {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog)
            .setCancelable(false)
            .setMessage("Are you sure?")
            .setPositiveButton("Yes") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    operationListViewModel.deleteOperation(operationId)
                    adapter.deleteOperation(operationId)
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}