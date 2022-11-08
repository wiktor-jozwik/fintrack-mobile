package com.example.moneytracker.view.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentListCategoryBinding
import com.example.moneytracker.service.model.mt.Category
import com.example.moneytracker.view.adapter.CategoryListAdapter
import com.example.moneytracker.view.ui.utils.makeErrorToast
import com.example.moneytracker.viewmodel.ListCategoryViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListCategoryFragment : Fragment(R.layout.fragment_list_category) {
    private val listCategoryViewModel: ListCategoryViewModel by viewModels()

    private var listCategoryLiveData: MutableLiveData<List<Category>> = MutableLiveData()
    private var deleteCategoryLiveData: MutableLiveData<Category> = MutableLiveData()

    private var _binding: FragmentListCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        listCategoryLiveData = MutableLiveData()
        deleteCategoryLiveData = MutableLiveData()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bubbleAdd.setOnClickListener {
            findNavController(view).navigate(R.id.action_listCategoryFragment_to_saveCategoryFragment)
        }

        val deleteLambda = CategoryListAdapter.DeleteOnClickListener { categoryId ->
            deleteCategory(
                categoryId,
            )
        }

        val editLambda = CategoryListAdapter.EditOnClickListener { category ->
            editCategory(
                view,
                category,
            )
        }


        listCategoryLiveData.observe(viewLifecycleOwner) {
            binding.recyclerViewCategoryItems.adapter = CategoryListAdapter(
                it,
                deleteLambda,
                editLambda
            )
            binding.recyclerViewCategoryItems.adapter!!.notifyDataSetChanged()
        }

        deleteCategoryLiveData.observe(viewLifecycleOwner) {
            val adapter = binding.recyclerViewCategoryItems.adapter as CategoryListAdapter
            adapter.deleteCategory(it.id)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                listCategoryLiveData.value = listCategoryViewModel.getAllCategories()
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }

        binding.recyclerViewCategoryItems.adapter = CategoryListAdapter(
            listOf(),
            deleteLambda,
            editLambda
        )
        binding.recyclerViewCategoryItems.layoutManager = LinearLayoutManager(activity)
    }

    private fun deleteCategory(categoryId: Int) {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog)
            .setCancelable(false)
            .setMessage("Are you sure you want to delete?")
            .setPositiveButton("Yes") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        deleteCategoryLiveData.value =
                            listCategoryViewModel.deleteCategory(categoryId)
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

    private fun editCategory(view: View, category: Category) {
        val bundle = Bundle()
        bundle.putString("categoryId", category.id.toString())
        bundle.putString("categoryName", category.name)
        bundle.putString("categoryType", category.type.toString())
        bundle.putString("isInternal", category.isInternal.toString())


        findNavController(view).navigate(
            R.id.action_listCategoryFragment_to_saveCategoryFragment,
            bundle
        )
    }
}