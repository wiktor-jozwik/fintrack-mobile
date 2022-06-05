package com.example.moneytracker.view.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentAddCategoryBinding
import com.example.moneytracker.service.model.CategoryType
import com.example.moneytracker.view.ui.utils.removeSpaces
import com.example.moneytracker.viewmodel.AddCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddCategoryFragment : Fragment(R.layout.fragment_add_category) {
    private val addCategoryViewModel: AddCategoryViewModel by viewModels()

    @Inject
    lateinit var yearlyOperationsSummaryFragment: YearlyOperationsSummaryFragment

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
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryNameTextChangeListener()

        binding.buttonSave.setOnClickListener {
            submitForm()
        }
    }

    private fun submitForm() {
        val selectedRadioButtonText =
            listOf(binding.radioButtonIncome, binding.radioButtonOutcome).first {
                it.id == binding.inputCategoryType.checkedRadioButtonId
            }.text

        val categoryType =
            hashMapOf("Outcome" to CategoryType.OUTCOME, "Income" to CategoryType.INCOME)

        val operationCategoryType = categoryType[selectedRadioButtonText]

        if (validateCategoryName() == null && operationCategoryType != null) {
            validForm(operationCategoryType)
        } else {
            invalidForm()
        }
    }

    private fun validForm(operationCategoryType: CategoryType) {
        viewLifecycleOwner.lifecycleScope.launch {
            addCategoryViewModel.addNewCategory(
                binding.inputNameText.text.toString().removeSpaces(),
                operationCategoryType
            ).observe(viewLifecycleOwner) {

                binding.inputNameText.setText("")
                binding.inputNameContainer.helperText = ""

                switchToSummary()
            }
        }
    }

    private fun switchToSummary() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.homeFrameLayoutFragment, yearlyOperationsSummaryFragment)
            commit()
        }
    }


    private fun categoryNameTextChangeListener() {
        binding.inputNameText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.inputNameContainer.helperText = validateCategoryName()
            }
        })
    }

    private fun validateCategoryName(): String? {
        if (binding.inputNameText.text.toString().removeSpaces().isBlank()) {
            return "Please provide name of category."
        }
        return null
    }

    private fun invalidForm() {
        val categoryNameText = validateCategoryName()

        binding.inputNameContainer.helperText = categoryNameText

        AlertDialog.Builder(activity)
            .setTitle("Invalid form")
            .setMessage("Please provide requested fields.")
            .setPositiveButton("Okay") { _, _ -> {} }
            .show()
    }
}