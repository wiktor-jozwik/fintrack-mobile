package com.example.moneytracker.view.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentCategoryListBinding
import com.example.moneytracker.view.adapter.CategoryListAdapter
import com.example.moneytracker.viewmodel.CategoryListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryListFragment : Fragment(R.layout.fragment_category_list) {
    private val categoryListViewModel: CategoryListViewModel by viewModels()

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
        _binding = null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            categoryListViewModel.getAllCategories().observe(viewLifecycleOwner) {
                binding.recyclerViewCategoryItems.adapter = CategoryListAdapter(
                    it,
                    CategoryListAdapter.OnClickListener { categoryId ->
                        deleteCategory(
                            categoryId,
                            binding.recyclerViewCategoryItems.adapter as CategoryListAdapter
                        )
                    }
                )
            }
        }

        binding.recyclerViewCategoryItems.layoutManager = LinearLayoutManager(activity)
    }

    private fun deleteCategory(categoryId: Int, adapter: CategoryListAdapter) {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("Are you sure?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    categoryListViewModel.deleteOperation(categoryId)
                    adapter.deleteCategory(categoryId)
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }
}