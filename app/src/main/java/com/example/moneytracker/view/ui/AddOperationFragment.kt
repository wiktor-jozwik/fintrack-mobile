package com.example.moneytracker.view.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.moneytracker.R
import com.example.moneytracker.databinding.ActivityMainBinding
import com.example.moneytracker.databinding.FragmentAddOperationBinding
import com.example.moneytracker.viewmodel.AddOperationViewModel

class AddOperationFragment: Fragment(R.layout.fragment_add_operation) {
    private var _binding: FragmentAddOperationBinding? = null
    private val binding get() = _binding!!

    private lateinit var addOperationViewModel: AddOperationViewModel

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
        addOperationViewModel = AddOperationViewModel()

        moneyAmountFocusListener()
        operationNameFocusListener()

        fulfillSpinners()

        binding.buttonSave.setOnClickListener {
            submitForm()
        }
    }

    private fun fulfillSpinners() {
        val currencies = addOperationViewModel.getAllCurrencies()
        val categories = addOperationViewModel.getAllCategories()

        currencies.observe(viewLifecycleOwner) {
            val currenciesNames = it.map { currency -> currency.name }

            val currenciesAdapter = ArrayAdapter(
                activity as Context,
                android.R.layout.simple_spinner_item,
                currenciesNames
            )
            binding.inputCurrency.adapter = currenciesAdapter
        }

        categories.observe(viewLifecycleOwner) {
            val categoriesNames = it.map { category -> category.name }

            val categoriesAdapter = ArrayAdapter(
                activity as Context,
                android.R.layout.simple_spinner_item,
                categoriesNames
            )
            binding.inputCategory.adapter = categoriesAdapter
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
        val nameText = binding.inputNameText.text.toString()

        if (nameText.isEmpty()){
            return "Please provide name of operation."
        }
        return null
    }

    private fun submitForm() {
        val validMoneyAmount = binding.inputMoneyAmountContainer.helperText == null
        val validName = binding.inputNameContainer.helperText == null

        if (validMoneyAmount && validName) {
            validForm()
        } else {
            invalidForm()
        }
    }

    private fun validForm() {
        val operation = addOperationViewModel.addNewOperation(
            binding.inputNameText.text.toString(),
            binding.inputMoneyAmountText.text.toString().toDouble(),
            binding.inputCategory.selectedItem.toString(),
            binding.inputCurrency.selectedItem.toString()
        )

        operation.observe(viewLifecycleOwner) {
            Log.d("MT", operation.toString())

            switchToOperationList()
        }
    }

    private fun switchToOperationList() {
        val operationListFragment = OperationListFragment()

        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, operationListFragment)
            commit()
        }
    }

    private fun invalidForm() {
//        var message = ""
//        if(binding.inputNameContainer.helperText)
        AlertDialog.Builder(activity)
            .setTitle("Invalid form")
            .setMessage("Please provide all fields.")
            .setPositiveButton("Okay") {_,_ -> {}}
            .show()
    }
}