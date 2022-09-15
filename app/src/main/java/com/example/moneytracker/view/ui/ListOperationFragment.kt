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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentListOperationBinding
import com.example.moneytracker.service.model.mt.Operation
import com.example.moneytracker.utils.formatToIsoDateWithDashes
import com.example.moneytracker.view.adapter.OperationListAdapter
import com.example.moneytracker.view.ui.utils.makeErrorToast
import com.example.moneytracker.view.ui.utils.responseErrorHandler
import com.example.moneytracker.viewmodel.ListOperationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class ListOperationFragment : Fragment(R.layout.fragment_list_operation) {
    private val listOperationViewModel: ListOperationViewModel by viewModels()

    @Inject
    lateinit var saveOperationFragment: SaveOperationFragment

    private var listOperationLiveData: MutableLiveData<Response<List<Operation>>> =
        MutableLiveData()
    private var deleteOperationLiveData: MutableLiveData<Response<Operation>> = MutableLiveData()


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

        val deleteLambda = OperationListAdapter.DeleteOnClickListener { operationId ->
            deleteOperation(
                operationId,
                binding.recyclerViewOperationItems.adapter as OperationListAdapter
            )
        }

        val editLambda = OperationListAdapter.EditOnClickListener { operation ->
            editOperation(
                operation,
            )
        }

        listOperationLiveData.observe(viewLifecycleOwner) {
            try {
                val res = responseErrorHandler(it)
                binding.recyclerViewOperationItems.adapter = OperationListAdapter(
                    res.sortedByDescending { operation -> operation.date },
                    deleteLambda,
                    editLambda
                )
                binding.recyclerViewOperationItems.adapter!!.notifyDataSetChanged()
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }

        deleteOperationLiveData.observe(viewLifecycleOwner) {
            try {
                val res = responseErrorHandler(it)
                val adapter = binding.recyclerViewOperationItems.adapter as OperationListAdapter
                adapter.deleteOperation(res.id)
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            listOperationLiveData.value = listOperationViewModel.getAllOperations()
        }

        binding.recyclerViewOperationItems.adapter = OperationListAdapter(
            listOf(),
            deleteLambda,
            editLambda
        )
        binding.recyclerViewOperationItems.layoutManager = LinearLayoutManager(context)
    }

    private fun deleteOperation(operationId: Int, adapter: OperationListAdapter) {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog)
            .setCancelable(false)
            .setMessage("Are you sure?")
            .setPositiveButton("Yes") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    deleteOperationLiveData.value =
                        listOperationViewModel.deleteOperation(operationId)
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun editOperation(operation: Operation) {
        val bundle = Bundle()
        bundle.putString("operationId", operation.id.toString())
        bundle.putString("operationName", operation.name)
        bundle.putString("operationCategoryName", operation.category.name)
        bundle.putString("operationCurrencyName", operation.currency.name)
        bundle.putString("operationDate", operation.date.formatToIsoDateWithDashes())
        bundle.putString("operationMoneyAmount", operation.moneyAmount.toString())

        parentFragmentManager.beginTransaction().apply {
            saveOperationFragment.arguments = bundle
            replace(R.id.homeFrameLayoutFragment, saveOperationFragment)
            commit()
        }
    }
}