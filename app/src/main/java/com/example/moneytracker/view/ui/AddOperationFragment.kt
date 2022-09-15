package com.example.moneytracker.view.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentAddOperationBinding
import com.example.moneytracker.service.model.mt.Category
import com.example.moneytracker.service.model.mt.Currency
import com.example.moneytracker.service.model.mt.Operation
import com.example.moneytracker.view.ui.utils.makeErrorToast
import com.example.moneytracker.view.ui.utils.removeSpaces
import com.example.moneytracker.view.ui.utils.responseErrorHandler
import com.example.moneytracker.viewmodel.AddOperationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class AddOperationFragment : Fragment(R.layout.fragment_add_operation) {
    private val addOperationViewModel: AddOperationViewModel by viewModels()

    @Inject
    lateinit var addFragment: AddFragment

    @Inject
    lateinit var operationListFragment: OperationListFragment

    @Inject
    lateinit var datePickerFragment: DatePickerFragment

    private var editCurrencyName: String? = null
    private var editCategoryName: String? = null

    private var saveOperationLiveData: MutableLiveData<Response<Operation>> = MutableLiveData()
    private var currencyLiveData: MutableLiveData<Response<List<Currency>>> = MutableLiveData()
    private var categoryLiveData: MutableLiveData<Response<List<Category>>> = MutableLiveData()

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
        saveOperationLiveData = MutableLiveData()
        currencyLiveData = MutableLiveData()
        categoryLiveData = MutableLiveData()
    }

    override fun onResume() {
        super.onResume()
        fillEditInformation()
        clearHelpers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveOperationLiveData.observe(viewLifecycleOwner) {
            try {
                responseErrorHandler(it)
                if (binding.id.text.isNullOrBlank()) {
                    switchToAdd()
                } else {
                    switchToOperationList()
                }
                clearFields()
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }

        fulfillSpinners()

        setDatePickerListener()

        moneyAmountTextChangeListener()
        operationNameTextChangeListener()

        binding.buttonSave.setOnClickListener {
            submitForm()
        }
    }

    private fun fillEditInformation() {
        if (arguments != null) {
            Log.d("MT", arguments.toString())
            val operationId = arguments?.getString("operationId")
            val operationName = arguments?.getString("operationName")
            val operationDate = arguments?.getString("operationDate")
            val operationMoneyAmount = arguments?.getString("operationMoneyAmount")
            val operationCurrencyName = arguments?.getString("operationCurrencyName")
            val operationCategoryName = arguments?.getString("operationCategoryName")

            binding.id.text = operationId
            binding.textDate.text = operationDate
            binding.inputNameText.setText(operationName)
            binding.inputMoneyAmountText.setText(operationMoneyAmount)
            editCurrencyName = operationCurrencyName
            editCategoryName = operationCategoryName

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
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        textDate.text = date
                    }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }
        }
    }

    private fun fulfillSpinners() {
        viewLifecycleOwner.lifecycleScope.launch {
            currencyLiveData.observe(viewLifecycleOwner) {
                try {
                    val res = responseErrorHandler(it)
                    val currenciesNames = res.map { currency -> currency.name }
                    val currenciesAdapter = ArrayAdapter(
                        activity as Context,
                        android.R.layout.simple_spinner_item,
                        currenciesNames
                    )
                    binding.inputCurrency.adapter = currenciesAdapter
                    selectValueForSpinner(binding.inputCurrency, editCurrencyName);
                    editCurrencyName = null
                } catch (e: Exception) {
                    makeErrorToast(requireContext(), e.message, 200)
                }
            }

            currencyLiveData.value = addOperationViewModel.getUsersCurrencies()

            categoryLiveData.observe(viewLifecycleOwner) {
                try {
                    val res = responseErrorHandler(it)
                    val categoriesNames = res.map { category -> category.name }
                    val categoriesAdapter = ArrayAdapter(
                        activity as Context,
                        android.R.layout.simple_spinner_item,
                        categoriesNames
                    )
                    binding.inputCategory.adapter = categoriesAdapter
                    selectValueForSpinner(binding.inputCategory, editCategoryName);
                    editCategoryName = null
                } catch (e: Exception) {
                    makeErrorToast(requireContext(), e.message, 200)
                }
            }
            categoryLiveData.value = addOperationViewModel.getAllCategories()
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

        if (!id.isNullOrBlank()) {
            viewLifecycleOwner.lifecycleScope.launch {
                saveOperationLiveData.value = addOperationViewModel.editOperation(
                    Integer.parseInt(id.toString()),
                    binding.inputNameText.text.toString(),
                    binding.inputMoneyAmountText.text.toString().toDouble(),
                    date,
                    binding.inputCategory.selectedItem.toString(),
                    binding.inputCurrency.selectedItem.toString()
                )
            }
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                saveOperationLiveData.value = addOperationViewModel.addNewOperation(
                    binding.inputNameText.text.toString(),
                    binding.inputMoneyAmountText.text.toString().toDouble(),
                    date,
                    binding.inputCategory.selectedItem.toString(),
                    binding.inputCurrency.selectedItem.toString()
                )
            }
        }


    }

    private fun switchToAdd() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.homeFrameLayoutFragment, addFragment)
            commit()
        }
    }

    private fun switchToOperationList() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.homeFrameLayoutFragment, operationListFragment)
            commit()
        }
    }

    private fun clearFields() {
        Log.d("MT", "clearFields")
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
}