package com.example.moneytracker.view.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentSummaryBinding
import com.example.moneytracker.viewmodel.YearlyOperationsSummaryViewModel

class YearlyOperationsSummaryFragment: Fragment() {
    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val yearlyOperationsSummaryViewModel = YearlyOperationsSummaryViewModel()

        yearlyOperationsSummaryViewModel.calculateYearlyIncomeAndOutcome().observe(viewLifecycleOwner) {
            val (totalIncome, totalOutcome, balance) = it
            Log.d("MT", it.toString())
            binding.textIncomesValue.text = "$totalIncome zł"
            binding.textOutcomesValue.text = "$totalOutcome zł"
            binding.textBalanceValue.text = "$balance zł"
        }

        val addOperationFragment = AddOperationFragment()

        binding.buttonAddNew.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, addOperationFragment)
                commit()
            }
        }
    }
}