package com.example.moneytracker.view.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentAddOperationBinding
import com.example.moneytracker.viewmodel.AddOperationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddOperationFragment: Fragment(R.layout.fragment_add_operation) {
    private val addOperationViewModel: AddOperationViewModel by viewModels()
    @Inject lateinit var yearlyOperationsSummaryFragment: YearlyOperationsSummaryFragment

    private var _binding: FragmentAddOperationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddOperationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moneyAmountFocusListener()
        operationNameFocusListener()

        fulfillSpinners()

        binding.buttonSave.setOnClickListener {
            submitForm()
        }
    }

    private fun fulfillSpinners() {
        viewLifecycleOwner.lifecycleScope.launch {
            addOperationViewModel.getAllCurrencies().observe(viewLifecycleOwner) {
                val currenciesNames = it.map { currency -> currency.name }

                val currenciesAdapter = ArrayAdapter(
                    activity as Context,
                    android.R.layout.simple_spinner_item,
                    currenciesNames
                )
                binding.inputCurrency.adapter = currenciesAdapter
            }

            addOperationViewModel.getAllCategories().observe(viewLifecycleOwner) {
                val categoriesNames = it.map { category -> category.name }

                val categoriesAdapter = ArrayAdapter(
                    activity as Context,
                    android.R.layout.simple_spinner_item,
                    categoriesNames
                )
                binding.inputCategory.adapter = categoriesAdapter
            }
        }
    }


    private fun moneyAmountFocusListener() {
        binding.inputMoneyAmountText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.inputMoneyAmountContainer.helperText = validMoneyAmount()
            }
        }
    }

    private fun validMoneyAmount(): String? {
        val moneyAmountText = binding.inputMoneyAmountText.text.toString()

        if (moneyAmountText.isEmpty()){
            return "Please specify amount of money."
        }
        return null
    }

    private fun operationNameFocusListener() {
        binding.inputNameText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.inputNameContainer.helperText = validOperationName()
            }
        }
    }

    private fun validOperationName(): String? {
        if (binding.inputNameText.text.toString().isEmpty()){
            return "Please provide name of operation."
        }
        return null
    }

    private fun submitForm() {
        val validMoneyAmount = binding.inputMoneyAmountText.text?.isNotEmpty()
        val validName = binding.inputNameText.text?.isNotEmpty()

        if (validMoneyAmount == true && validName == true) {
            validForm()
        } else {
            invalidForm()
        }
    }

    private fun validForm() {
        viewLifecycleOwner.lifecycleScope.launch {
            addOperationViewModel.addNewOperation(
                binding.inputNameText.text.toString(),
                binding.inputMoneyAmountText.text.toString().toDouble(),
                binding.inputCategory.selectedItem.toString(),
                binding.inputCurrency.selectedItem.toString()
            ).observe(viewLifecycleOwner) {
                binding.inputNameText.text = null
                binding.inputMoneyAmountText.text = null
                switchToYearlySummary()
            }
        }
    }

    private fun switchToYearlySummary() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, yearlyOperationsSummaryFragment)
            commit()
        }
    }

    private fun invalidForm() {
        binding.inputNameContainer.helperText = validOperationName()
        binding.inputMoneyAmountContainer.helperText = validMoneyAmount()

        AlertDialog.Builder(activity)
            .setTitle("Invalid form")
            .setMessage("Please provide all fields.")
            .setPositiveButton("Okay") {_,_ -> {}}
            .show()
    }
}