package com.example.moneytracker.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentSaveBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SaveFragment : Fragment(R.layout.fragment_save) {
    @Inject
    lateinit var saveOperationFragment: SaveOperationFragment

    @Inject
    lateinit var saveCategoryFragment: SaveCategoryFragment

    @Inject
    lateinit var saveCurrencyFragment: SaveCurrencyFragment

    private var _binding: FragmentSaveBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonShowAddOperation.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.homeFrameLayoutFragment, saveOperationFragment)
                commit()
            }
        }

        binding.buttonShowAddCategory.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.homeFrameLayoutFragment, saveCategoryFragment)
                commit()
            }
        }

        binding.buttonShowAddCurrency.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.homeFrameLayoutFragment, saveCurrencyFragment)
                commit()
            }
        }
    }
}