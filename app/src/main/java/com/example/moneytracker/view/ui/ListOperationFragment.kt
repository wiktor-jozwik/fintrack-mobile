package com.example.moneytracker.view.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentListOperationBinding
import com.example.moneytracker.service.model.mt.Operation
import com.example.moneytracker.utils.formatToIsoDateWithDashes
import com.example.moneytracker.view.adapter.OperationListAdapter
import com.example.moneytracker.view.ui.utils.makeErrorToast
import com.example.moneytracker.viewmodel.ListOperationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListOperationFragment : Fragment(R.layout.fragment_list_operation) {
    private val listOperationViewModel: ListOperationViewModel by viewModels()

    private var listOperationLiveData: MutableLiveData<List<Operation>> = MutableLiveData()
    private var deleteOperationLiveData: MutableLiveData<Operation> = MutableLiveData()

    private var _binding: FragmentListOperationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListOperationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
        listOperationLiveData = MutableLiveData()
        deleteOperationLiveData = MutableLiveData()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonPlus.setOnClickListener {
            findNavController(view).navigate(R.id.action_listOperationFragment_to_saveOperationFragment)
        }

        val deleteLambda = OperationListAdapter.DeleteOnClickListener { operationId ->
            deleteOperation(operationId)
        }

        val editLambda = OperationListAdapter.EditOnClickListener { operation ->
            editOperation(
                view,
                operation,
            )
        }

        listOperationLiveData.observe(viewLifecycleOwner) {
            binding.recyclerViewOperationItems.adapter = OperationListAdapter(
                it.sortedByDescending { operation -> operation.date },
                deleteLambda,
                editLambda
            )
            binding.recyclerViewOperationItems.adapter!!.notifyDataSetChanged()
        }

        deleteOperationLiveData.observe(viewLifecycleOwner) {
            val adapter = binding.recyclerViewOperationItems.adapter as OperationListAdapter
            adapter.deleteOperation(it.id)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                listOperationLiveData.value = listOperationViewModel.getAllOperations()
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }

        binding.recyclerViewOperationItems.adapter = OperationListAdapter(
            listOf(),
            deleteLambda,
            editLambda
        )
        binding.recyclerViewOperationItems.layoutManager = LinearLayoutManager(context)
    }

    private fun deleteOperation(operationId: Int) {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog)
            .setCancelable(false)
            .setMessage("Are you sure you want to delete?")
            .setPositiveButton("Yes") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        deleteOperationLiveData.value =
                            listOperationViewModel.deleteOperation(operationId)
                    } catch (e: Exception) {
                        makeErrorToast(requireContext(), e.message, 200)
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun editOperation(view: View, operation: Operation) {
        val bundle = Bundle()
        bundle.putString("operationId", operation.id.toString())
        bundle.putString("operationName", operation.name)
        bundle.putString("operationCategoryName", operation.category.name)
        bundle.putString("operationCategoryType", operation.category.type.toString())
        bundle.putString("operationCurrencyName", operation.currency.name)
        bundle.putString("operationDate", operation.date.formatToIsoDateWithDashes())
        bundle.putString("operationMoneyAmount", operation.moneyAmount.toString())

        findNavController(view).navigate(
            R.id.action_listOperationFragment_to_saveOperationFragment,
            bundle
        )
    }
}