package com.example.moneytracker.view.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentAddCategoryBinding
import com.example.moneytracker.service.model.CategoryType
import com.example.moneytracker.viewmodel.AddCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddCategoryFragment: Fragment(R.layout.fragment_add_category) {
    private val addCategoryViewModel: AddCategoryViewModel by viewModels()
    @Inject lateinit var yearlyOperationsSummaryFragment: YearlyOperationsSummaryFragment

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        operationNameFocusListener()

        binding.buttonSave.setOnClickListener {
            submitForm()
        }
    }

    private fun operationNameFocusListener() {
        binding.inputNameText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.inputNameContainer.helperText = validCategoryName()
            }
        }
    }

    private fun validCategoryName(): String? {
        if (binding.inputNameText.text.toString().isEmpty()){
            return "Please provide name of category."
        }
        return null
    }

    private fun submitForm() {
        val validName = binding.inputNameText.text?.isNotEmpty()
        val selectedRadioButtonText =
            listOf(binding.radioButtonIncome, binding.radioButtonOutcome).first {
                it.id == binding.inputCategoryType.checkedRadioButtonId
            }.text

        val categoryType = hashMapOf("Outcome" to CategoryType.OUTCOME, "Income" to CategoryType.INCOME)

        val operationCategoryType = categoryType[selectedRadioButtonText]

        if (validName == true && operationCategoryType != null) {
            validForm(operationCategoryType)
        } else {
            invalidForm()
        }
    }

    private fun validForm(operationCategoryType: CategoryType) {
        viewLifecycleOwner.lifecycleScope.launch {
            addCategoryViewModel.addNewCategory(
                binding.inputNameText.text.toString(),
                operationCategoryType
            ).observe(viewLifecycleOwner) {
                binding.inputNameText.text = null
                switchToSummary()
            }
        }
    }

    private fun switchToSummary() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, yearlyOperationsSummaryFragment)
            commit()
        }
    }

    private fun invalidForm() {
        binding.inputNameContainer.helperText = validCategoryName()

        AlertDialog.Builder(activity)
            .setTitle("Invalid form")
            .setMessage("Please provide all fields.")
            .setPositiveButton("Okay") {_,_ -> {}}
            .show()
    }
}