package com.example.moneytracker.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {
    @Inject
    lateinit var listOperationFragment: ListOperationFragment

    @Inject
    lateinit var listCategoryFragment: ListCategoryFragment

    @Inject
    lateinit var listCurrencyFragment: ListCurrencyFragment

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
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.homeFrameLayoutFragment, listOperationFragment)
                commit()
            }
        }

        binding.buttonShowCategories.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.homeFrameLayoutFragment, listCategoryFragment)
                commit()
            }
        }

        binding.buttonShowCurrencies.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.homeFrameLayoutFragment, listCurrencyFragment)
                commit()
            }
        }
    }
}