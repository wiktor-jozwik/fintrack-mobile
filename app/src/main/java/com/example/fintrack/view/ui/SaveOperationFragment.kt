package com.example.fintrack.view.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import com.example.fintrack.R
import com.example.fintrack.databinding.FragmentSaveOperationBinding
import com.example.fintrack.service.model.mt.Category
import com.example.fintrack.service.model.mt.Currency
import com.example.fintrack.service.model.mt.Operation
import com.example.fintrack.view.ui.datepickers.OperationSaveDatePickerFragment
import com.example.fintrack.view.ui.datepickers.RequestKey
import com.example.fintrack.view.ui.enums.SaveState
import com.example.fintrack.view.ui.utils.makeErrorToast
import com.example.fintrack.view.ui.utils.removeSpaces
import com.example.fintrack.viewmodel.SaveOperationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class SaveOperationFragment : Fragment(R.layout.fragment_save_operation) {
    private val saveOperationViewModel: SaveOperationViewModel by viewModels()

    @Inject
    lateinit var operationSaveDatePickerFragment: OperationSaveDatePickerFragment

    private var editCurrencyName: String? = null
    private var editCategoryName: String? = null

    private var saveOperationLiveData: MutableLiveData<Operation> = MutableLiveData()
    private var currencyLiveData: MutableLiveData<List<Currency>> = MutableLiveData()
    private var categoryLiveData: MutableLiveData<List<Category>> = MutableLiveData()

    private var saveState: SaveState = SaveState.ADD

    private var _binding: FragmentSaveOperationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveOperationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
        saveOperationLiveData = MutableLiveData()
        currencyLiveData = MutableLiveData()
        categoryLiveData = MutableLiveData()
    }

    override fun onResume() {
        super.onResume()
        binding.textTitle.text = "Add operation"
        binding.buttonSave.text = "Add"
        when (saveState) {
            SaveState.ADD -> {
                binding.importOperationsLink.visibility = View.VISIBLE
            }
            SaveState.CLEAR -> {
                binding.importOperationsLink.visibility = View.VISIBLE

                clearFields()
                saveState = SaveState.ADD
            }
            SaveState.EDIT -> {
                binding.importOperationsLink.visibility = View.INVISIBLE

                binding.textTitle.text = "Edit operation"
                binding.buttonSave.text = "Edit"
                clearFields()
                fillEditInformation()
                saveState = SaveState.CLEAR
            }
        }
        clearHelpers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveOperationLiveData.observe(viewLifecycleOwner) {
            findNavController(view).navigate(R.id.action_saveOperationFragment_to_listOperationFragment)
            clearFields()
        }

        fulfillSpinners()

        setDatePickerListener()

        moneyAmountTextChangeListener()
        operationNameTextChangeListener()

        binding.importOperationsLink.setOnClickListener {
            findNavController(view).navigate(R.id.action_saveOperationFragment_to_importOperationsFragment)
        }

        binding.buttonSave.setOnClickListener {
            submitForm()
        }

        val operationId = arguments?.getString("operationId")
        if (!operationId.isNullOrBlank()) {
            saveState = SaveState.EDIT
        }
    }

    private fun fillEditInformation() {
        if (arguments != null) {
            val operationId = arguments?.getString("operationId")
            val operationName = arguments?.getString("operationName")
            val operationDate = arguments?.getString("operationDate")
            val operationMoneyAmount = arguments?.getString("operationMoneyAmount")
            val operationCurrencyName = arguments?.getString("operationCurrencyName")
            val operationCategoryName = arguments?.getString("operationCategoryName")
            val operationCategoryType = arguments?.getString("operationCategoryType")

            binding.id.text = operationId
            binding.textDate.text = operationDate
            binding.inputNameText.setText(operationName)
            binding.inputMoneyAmountText.setText(operationMoneyAmount)
            editCurrencyName = operationCurrencyName
            if (!operationCategoryName.isNullOrBlank() && !operationCategoryType.isNullOrBlank()) {
                editCategoryName = mapCategoryName(operationCategoryName, operationCategoryType)
            }

            arguments = null
        }
    }

    private fun setDatePickerListener() {
        val calendar = Calendar.getInstance()
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.time)

        binding.apply {
            textDate.text = today

            buttonDatePicker.setOnClickListener {
                val supportFragmentManager = requireActivity().supportFragmentManager
                supportFragmentManager.setFragmentResultListener(
                    RequestKey.value,
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == RequestKey.value) {
                        val date = bundle.getString("OperationSaveDatePickerFragment")
                        textDate.text = date
                    }
                }

                operationSaveDatePickerFragment.show(supportFragmentManager, "operationSaveDatePickerFragment")
            }
        }
    }

    private fun fulfillSpinners() {
        currencyLiveData.observe(viewLifecycleOwner) {
            val currenciesNames = it.map { currency -> currency.name }
            val currenciesAdapter = ArrayAdapter(
                activity as Context,
                android.R.layout.simple_spinner_item,
                currenciesNames
            )
            binding.inputCurrency.adapter = currenciesAdapter
            selectValueForSpinner(binding.inputCurrency, editCurrencyName);
            editCurrencyName = null
        }

        categoryLiveData.observe(viewLifecycleOwner) {
            val categoriesNames = it.map { category ->
                mapCategoryName(category.name, category.type.toString())
            }
            val categoriesAdapter = ArrayAdapter(
                activity as Context,
                android.R.layout.simple_spinner_item,
                categoriesNames
            )
            binding.inputCategory.adapter = categoriesAdapter
            selectValueForSpinner(binding.inputCategory, editCategoryName);
            editCategoryName = null
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                currencyLiveData.value = saveOperationViewModel.getUsersCurrencies()
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }

            try {
                categoryLiveData.value = saveOperationViewModel.getAllCategories()
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }
    }

    private fun selectValueForSpinner(spinner: Spinner, value: Any?) {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i) == value) {
                spinner.setSelection(i)
                break
            }
        }
    }

    private fun submitForm() {
        if (
            validateMoneyAmount() == null &&
            validateOperationName() == null &&
            validateCategoryPresence() &&
            validateCurrencyPresence()
        ) {
            validForm()
        } else {
            invalidForm()
        }
    }

    private fun moneyAmountTextChangeListener() {
        binding.inputMoneyAmountText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                binding.inputMoneyAmountContainer.helperText = validateMoneyAmount()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

    }

    private fun validateMoneyAmount(): String? {
        val moneyAmountText = binding.inputMoneyAmountText.text.toString()

        if (moneyAmountText.isEmpty()) {
            return "Please specify amount of money."
        }
        return null
    }

    private fun operationNameTextChangeListener() {
        binding.inputNameText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.inputNameContainer.helperText = validateOperationName()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun validateOperationName(): String? {
        if (binding.inputNameText.text.toString().removeSpaces().isBlank()) {
            return "Please provide name of operation."
        }
        return null
    }

    private fun validateCategoryPresence(): Boolean {
        return binding.inputCategory.selectedItem != null && binding.inputCategory.selectedItem.toString()
            .isNotEmpty()
    }

    private fun validateCurrencyPresence(): Boolean {
        return binding.inputCurrency.selectedItem != null && binding.inputCurrency.selectedItem.toString()
            .isNotEmpty()
    }

    private fun validForm() {
        val id = binding.id.text

        val date = LocalDate.parse(
            "${binding.textDate.text}",
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        )

        val categoryName = binding.inputCategory.selectedItem.toString().substring(6)

        if (!id.isNullOrBlank()) {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    saveOperationLiveData.value = saveOperationViewModel.editOperation(
                        Integer.parseInt(id.toString()),
                        binding.inputNameText.text.toString(),
                        binding.inputMoneyAmountText.text.toString().toDouble(),
                        date,
                        // substring for cut (OUT) or (INC) from category name
                        categoryName,
                        binding.inputCurrency.selectedItem.toString()
                    )
                } catch (e: Exception) {
                    makeErrorToast(requireContext(), e.message, 200)
                }
            }
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    saveOperationLiveData.value = saveOperationViewModel.addNewOperation(
                        binding.inputNameText.text.toString(),
                        binding.inputMoneyAmountText.text.toString().toDouble(),
                        date,
                        categoryName,
                        binding.inputCurrency.selectedItem.toString()
                    )
                } catch (e: Exception) {
                    makeErrorToast(requireContext(), e.message, 200)
                }
            }
        }
    }

    private fun clearFields() {
        binding.id.text = null
        binding.inputNameText.setText("")
        binding.inputMoneyAmountText.setText("")
        clearHelpers()
    }

    private fun clearHelpers() {
        binding.inputNameContainer.helperText = ""
        binding.inputMoneyAmountContainer.helperText = ""
    }

    private fun invalidForm() {
        val nameText = validateOperationName()
        val moneyAmountText = validateMoneyAmount()

        binding.inputNameContainer.helperText = nameText
        binding.inputMoneyAmountContainer.helperText = moneyAmountText

        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog)
            .setTitle("Invalid form")
            .setMessage("Please provide requested fields.")
            .setPositiveButton("Okay") { _, _ -> {} }
            .show()
    }

    private fun mapCategoryName(categoryName: String, categoryType: String): String {
        return "(${
            categoryType.substring(0, 3)
        }) $categoryName"
    }
}