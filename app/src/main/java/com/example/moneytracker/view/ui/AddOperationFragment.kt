package com.example.moneytracker.view.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
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
    lateinit var datePickerFragment: DatePickerFragment

    private var addOperationLiveData: MutableLiveData<Response<Operation>> = MutableLiveData()
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
        addOperationLiveData = MutableLiveData()
        currencyLiveData = MutableLiveData()
        categoryLiveData = MutableLiveData()
    }

    override fun onResume() {
        super.onResume()
        clearHelpers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addOperationLiveData.observe(viewLifecycleOwner) {
            try {
                responseErrorHandler(it)
                addFragment()
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
                } catch (e: Exception) {
                    makeErrorToast(requireContext(), e.message, 200)
                }
            }
            categoryLiveData.value = addOperationViewModel.getAllCategories()
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
        val date = LocalDate.parse(
            "${binding.textDate.text}",
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        )

        viewLifecycleOwner.lifecycleScope.launch {
            addOperationLiveData.value = addOperationViewModel.addNewOperation(
                binding.inputNameText.text.toString(),
                binding.inputMoneyAmountText.text.toString().toDouble(),
                date,
                binding.inputCategory.selectedItem.toString(),
                binding.inputCurrency.selectedItem.toString()
            )
        }
    }

    private fun addFragment() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.homeFrameLayoutFragment, addFragment)
            commit()
        }
    }

    private fun clearFields() {
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