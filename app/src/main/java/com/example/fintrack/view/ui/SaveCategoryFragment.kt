package com.example.fintrack.view.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import com.example.fintrack.R
import com.example.fintrack.databinding.FragmentSaveCategoryBinding
import com.example.fintrack.service.model.mt.Category
import com.example.fintrack.service.model.mt.CategoryType
import com.example.fintrack.view.ui.enums.SaveState
import com.example.fintrack.view.ui.utils.makeErrorToast
import com.example.fintrack.view.ui.utils.removeSpaces
import com.example.fintrack.viewmodel.SaveCategoryViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SaveCategoryFragment : Fragment(R.layout.fragment_save_category) {
    private val saveCategoryViewModel: SaveCategoryViewModel by viewModels()

    private var saveCategoryLiveData: MutableLiveData<Category> = MutableLiveData()

    private var saveState: SaveState = SaveState.ADD

    private var _binding: FragmentSaveCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        saveCategoryLiveData = MutableLiveData()
    }

    override fun onResume() {
        super.onResume()
        binding.textTitle.text = "Add category"
        binding.buttonSave.text = "Add"
        when (saveState) {
            SaveState.ADD -> {
            }
            SaveState.CLEAR -> {
                clearFields()
                saveState = SaveState.ADD
            }
            SaveState.EDIT -> {
                binding.textTitle.text = "Edit category"
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

        saveCategoryLiveData.observe(viewLifecycleOwner) {
            clearFields()
            findNavController(view).navigate(R.id.action_saveCategoryFragment_to_listCategoryFragment)
        }

        categoryNameTextChangeListener()

        binding.buttonSave.setOnClickListener {
            submitForm()
        }

        val categoryId = arguments?.getString("categoryId")
        if (!categoryId.isNullOrBlank()) {
            saveState = SaveState.EDIT
        }
    }

    private fun fillEditInformation() {
        val categoryType = arguments?.getString("categoryType")
        val categoryId = arguments?.getString("categoryId")
        val categoryName = arguments?.getString("categoryName")
        val isInternal = arguments?.getString("isInternal")

        if (arguments != null && !categoryType.isNullOrBlank()) {
            binding.inputNameText.setText(categoryName)
            binding.id.text = categoryId
            if (isInternal.toBoolean()) {
                binding.inputInternal.isChecked = true
            }
            when (enumValueOf<CategoryType>(categoryType)) {
                CategoryType.INCOME -> binding.radioButtonIncome.isChecked = true
                CategoryType.OUTCOME -> binding.radioButtonOutcome.isChecked = true
            }
            arguments = null
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
        val isInternal = binding.inputInternal.isChecked

        if (validateCategoryName() == null && operationCategoryType != null) {
            validForm(operationCategoryType, isInternal)
        } else {
            invalidForm()
        }
    }

    private fun validForm(operationCategoryType: CategoryType, isInternal: Boolean) {
        val id = binding.id.text

        if (!id.isNullOrBlank()) {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    saveCategoryLiveData.value = saveCategoryViewModel.editCategory(
                        Integer.parseInt(id.toString()),
                        binding.inputNameText.text.toString().removeSpaces(),
                        operationCategoryType,
                        isInternal
                    )
                } catch (e: Exception) {
                    makeErrorToast(requireContext(), e.message, 200)
                }
            }
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    saveCategoryLiveData.value = saveCategoryViewModel.addNewCategory(
                        binding.inputNameText.text.toString().removeSpaces(),
                        operationCategoryType,
                        isInternal
                    )
                } catch (e: Exception) {
                    makeErrorToast(requireContext(), e.message, 200)
                }
            }
        }
    }

    private fun clearFields() {
        binding.inputNameText.setText("")
        binding.radioButtonOutcome.isChecked = true
        binding.radioButtonIncome.isChecked = false
        binding.inputInternal.isChecked = false
        clearHelpers()
    }

    private fun clearHelpers() {
        binding.inputNameContainer.helperText = ""
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

        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog)
            .setTitle("Invalid form")
            .setMessage("Please provide requested fields.")
            .setPositiveButton("Okay") { _, _ -> {} }
            .show()
    }
}