package com.example.fintrack.view.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fintrack.R
import com.example.fintrack.databinding.FragmentListOperationBinding
import com.example.fintrack.service.model.mt.CategoryType
import com.example.fintrack.service.model.mt.Operation
import com.example.fintrack.service.model.mt.OperationSearchFilters
import com.example.fintrack.utils.formatToIsoDateWithDashes
import com.example.fintrack.view.adapter.OperationListAdapter
import com.example.fintrack.view.ui.datepickers.OperationFilterEndDatePickerFragment
import com.example.fintrack.view.ui.datepickers.OperationFilterStartDatePickerFragment
import com.example.fintrack.view.ui.datepickers.RequestKey
import com.example.fintrack.view.ui.utils.isValidDate
import com.example.fintrack.view.ui.utils.makeErrorToast
import com.example.fintrack.viewmodel.ListOperationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@AndroidEntryPoint
class ListOperationFragment : Fragment(R.layout.fragment_list_operation) {
    private val listOperationViewModel: ListOperationViewModel by viewModels()
    private val operators = listOf("", ">", "<", "=", ">=", "<=")

    @Inject
    lateinit var operationFilterStartDatePickerFragment: OperationFilterStartDatePickerFragment

    @Inject
    lateinit var operationFilterEndDatePickerFragment: OperationFilterEndDatePickerFragment

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView(view)

        setDeleteOperationLiveDataObserver()

        setFilterBubbleListener()

        setFilterButtonListener()

        setClearButtonListener()

        setAddBubbleListener(view)

        getOperations(null)

        fulfillOperatorSpinner()

        setDatePickersListener()
    }

    private fun setRecyclerView(view: View) {
        val deleteLambda = OperationListAdapter.DeleteOnClickListener { operationId ->
            deleteOperation(operationId)
        }

        val editLambda = OperationListAdapter.EditOnClickListener { operation ->
            editOperation(
                view,
                operation,
            )
        }

        setListOperationLiveDataObserver(deleteLambda, editLambda)

        binding.recyclerViewOperationItems.adapter = OperationListAdapter(
            listOf(),
            deleteLambda,
            editLambda
        )
        binding.recyclerViewOperationItems.layoutManager = LinearLayoutManager(context)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListOperationLiveDataObserver(
        deleteLambda: OperationListAdapter.DeleteOnClickListener,
        editLambda: OperationListAdapter.EditOnClickListener
    ) {
        listOperationLiveData.observe(viewLifecycleOwner) {
            binding.recyclerViewOperationItems.adapter = OperationListAdapter(
                it.sortedByDescending { operation -> operation.date },
                deleteLambda,
                editLambda
            )
            binding.recyclerViewOperationItems.adapter!!.notifyDataSetChanged()
        }
    }

    private fun setDeleteOperationLiveDataObserver() {
        deleteOperationLiveData.observe(viewLifecycleOwner) {
            val adapter = binding.recyclerViewOperationItems.adapter as OperationListAdapter
            adapter.deleteOperation(it.id)
        }
    }

    private fun setFilterBubbleListener() {
        binding.bubbleFilter.setOnClickListener {
            toggleFilterLayout()
        }
    }

    private fun toggleFilterLayout() {
        val visibility: Int =
            if (binding.constraintLayoutOperationFilter.visibility == View.GONE) {
                View.VISIBLE
            } else {
                View.GONE
            }

        binding.constraintLayoutOperationFilter.visibility = visibility
    }

    private fun setFilterButtonListener() {
        binding.buttonFilter.setOnClickListener {
            var categoryType: CategoryType? = null

            if (binding.radioButtonIncome.isChecked) {
                categoryType = CategoryType.INCOME
            } else if (binding.radioButtonOutcome.isChecked) {
                categoryType = CategoryType.OUTCOME
            }

            var operator: String? = binding.filterOperator.selectedItem.toString()

            if (operator == "") {
                operator = null
            }

            var moneyAmount: Double? = null
            val moneyAmountString: String = binding.filterMoneyAmountText.text.toString()
            if (moneyAmountString.isNotBlank()) {
                moneyAmount = moneyAmountString.toDouble()
            }

            var startDate: LocalDate? = null
            val startDateString = binding.buttonDatePickerStart.text.toString()
            var endDate: LocalDate? = null
            val endDateString = binding.buttonDatePickerEnd.text.toString()


            if (isValidDate(startDateString)) {
                startDate = LocalDate.parse(startDateString)
            }
            if (isValidDate(endDateString)) {
                endDate = LocalDate.parse(endDateString)
            }

            getOperations(
                OperationSearchFilters(
                    startDate,
                    endDate,
                    categoryType,
                    binding.filterSearchNameText.text.toString(),
                    binding.filterIncludeInternal.isChecked,
                    operator,
                    moneyAmount
                )
            )

            toggleFilterLayout()
        }
    }

    private fun setClearButtonListener() {
        binding.buttonClearFilter.setOnClickListener {
            binding.apply {
                filterIncludeInternal.isChecked = false
                buttonDatePickerStart.text = "Start date"
                buttonDatePickerEnd.text = "End date"
                filterCategoryType.clearCheck()
                filterSearchNameText.setText("")
                filterMoneyAmountText.setText("")
                filterOperator.setSelection(operators.indexOf(""))
            }
            getOperations(null)
            toggleFilterLayout()
        }
    }

    private fun setAddBubbleListener(view: View) {
        binding.bubbleAdd.setOnClickListener {
            findNavController(view).navigate(R.id.action_listOperationFragment_to_saveOperationFragment)
        }
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

    private fun getOperations(searchFilters: OperationSearchFilters?) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                listOperationLiveData.value = listOperationViewModel.getAllOperations(
                    searchFilters
                )
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }
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

    private fun fulfillOperatorSpinner() {
        val operatorsAdapter =
            ArrayAdapter(activity as Context, android.R.layout.simple_spinner_item, operators)
        binding.filterOperator.adapter = operatorsAdapter
    }


    private fun setDatePickersListener() {
        binding.apply {
            buttonDatePickerStart.setOnClickListener {
                val supportFragmentManager = requireActivity().supportFragmentManager
                supportFragmentManager.setFragmentResultListener(
                    RequestKey.value,
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == RequestKey.value) {
                        val date = bundle.getString("OperationFilterStartDatePickerFragment")
                        buttonDatePickerStart.text = date.toString()
                    }
                }

                operationFilterStartDatePickerFragment.show(
                    supportFragmentManager,
                    "operationFilterStartDatePickerFragment"
                )
            }

            buttonDatePickerEnd.setOnClickListener {
                val supportFragmentManager = requireActivity().supportFragmentManager
                supportFragmentManager.setFragmentResultListener(
                    RequestKey.value,
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == RequestKey.value) {
                        val date = bundle.getString("OperationFilterEndDatePickerFragment")
                        buttonDatePickerEnd.text = date.toString()
                    }
                }

                operationFilterEndDatePickerFragment.show(
                    supportFragmentManager,
                    "operationFilterEndDatePickerFragment"
                )
            }
        }
    }
}