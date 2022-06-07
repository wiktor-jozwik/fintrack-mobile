package com.example.moneytracker.view.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentCategoryListBinding
import com.example.moneytracker.service.model.mt.Category
import com.example.moneytracker.view.adapter.CategoryListAdapter
import com.example.moneytracker.view.ui.utils.responseErrorHandler
import com.example.moneytracker.viewmodel.CategoryListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response

@AndroidEntryPoint
class CategoryListFragment : Fragment(R.layout.fragment_category_list) {
    private val categoryListViewModel: CategoryListViewModel by viewModels()

    private var listCategoryLiveData: MutableLiveData<Response<List<Category>>> = MutableLiveData()
    private var deleteCategoryLiveData: MutableLiveData<Response<Category>> = MutableLiveData()

    private var _binding: FragmentCategoryListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryListBinding.inflate(inflater, container, false)
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

        val deleteLambda = CategoryListAdapter.OnClickListener { categoryId ->
            deleteCategory(
                categoryId,
            )
        }

        listCategoryLiveData.observe(viewLifecycleOwner) {
            try {
                val res = responseErrorHandler(it)
                binding.recyclerViewCategoryItems.adapter = CategoryListAdapter(
                    res,
                    deleteLambda
                )
                binding.recyclerViewCategoryItems.adapter!!.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }

        deleteCategoryLiveData.observe(viewLifecycleOwner) {
            try {
                val res = responseErrorHandler(it)
                val adapter = binding.recyclerViewCategoryItems.adapter as CategoryListAdapter
                adapter.deleteCategory(res.id)
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            listCategoryLiveData.value = categoryListViewModel.getAllCategories()
        }

        binding.recyclerViewCategoryItems.adapter = CategoryListAdapter(
            listOf(),
            deleteLambda
        )
        binding.recyclerViewCategoryItems.layoutManager = LinearLayoutManager(activity)
    }

    private fun deleteCategory(categoryId: Int) {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog)
            .setCancelable(false)
            .setMessage("Are you sure?")
            .setPositiveButton("Yes") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    deleteCategoryLiveData.value = categoryListViewModel.deleteCategory(categoryId)
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}