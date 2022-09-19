package com.example.moneytracker.view.ui

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
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentSaveCategoryBinding
import com.example.moneytracker.service.model.mt.Category
import com.example.moneytracker.service.model.mt.CategoryType
import com.example.moneytracker.view.ui.utils.makeErrorToast
import com.example.moneytracker.view.ui.utils.removeSpaces
import com.example.moneytracker.viewmodel.SaveCategoryViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SaveCategoryFragment : Fragment(R.layout.fragment_save_category) {
    private val saveCategoryViewModel: SaveCategoryViewModel by viewModels()

    @Inject
    lateinit var listCategoryFragment: ListCategoryFragment

    private var saveCategoryLiveData: MutableLiveData<Category> = MutableLiveData()

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
        fillEditInformation()
        clearHelpers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveCategoryLiveData.observe(viewLifecycleOwner) {
            switchToCategoryList()
            clearFields()
        }

        categoryNameTextChangeListener()

        binding.buttonSave.setOnClickListener {
            submitForm()
        }
    }

    private fun fillEditInformation() {
        val categoryType = arguments?.getString("categoryType")

        if (arguments != null && !categoryType.isNullOrBlank()) {
            val categoryId = arguments?.getString("categoryId")
            val categoryName = arguments?.getString("categoryName")

            binding.inputNameText.setText(categoryName)
            binding.id.text = categoryId
            when (enumValueOf<CategoryType>(categoryType)) {
                CategoryType.INCOME -> binding.radioButtonIncome.isChecked = true
                CategoryType.OUTCOME -> binding.radioButtonOutcome.isChecked = true
            }
        }
        arguments = null
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
        val id = binding.id.text
        if (!id.isNullOrBlank()) {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    saveCategoryLiveData.value = saveCategoryViewModel.editCategory(
                        Integer.parseInt(id.toString()),
                        binding.inputNameText.text.toString().removeSpaces(),
                        operationCategoryType
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
                        operationCategoryType
                    )
                } catch (e: Exception) {
                    makeErrorToast(requireContext(), e.message, 200)
                }
            }
        }
    }

    private fun switchToCategoryList() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.homeFrameLayoutFragment, listCategoryFragment)
            commit()
        }
    }

    private fun clearFields() {
        binding.inputNameText.setText("")
        binding.id.text = null
        binding.radioButtonOutcome.isChecked = true
        binding.radioButtonIncome.isChecked = false
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