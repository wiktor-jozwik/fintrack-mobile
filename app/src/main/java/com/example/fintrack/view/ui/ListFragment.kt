package com.example.fintrack.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.example.fintrack.R
import com.example.fintrack.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonShowOperations.setOnClickListener {
            findNavController(view).navigate(R.id.action_listFragment_to_listOperationFragment)
        }

        binding.buttonShowCategories.setOnClickListener {
            findNavController(view).navigate(R.id.action_listFragment_to_listCategoryFragment)
        }

        binding.buttonShowCurrencies.setOnClickListener {
            findNavController(view).navigate(R.id.action_listFragment_to_listCurrencyFragment)
        }
    }
}