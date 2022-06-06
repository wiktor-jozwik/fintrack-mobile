package com.example.moneytracker.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentSummaryBinding
import com.example.moneytracker.viewmodel.YearlyOperationsSummaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class YearlyOperationsSummaryFragment : Fragment() {
    private val yearlyOperationsSummaryViewModel: YearlyOperationsSummaryViewModel by viewModels()

    @Inject
    lateinit var addOperationFragment: AddOperationFragment

    @Inject
    lateinit var addCategoryFragment: AddCategoryFragment

    private var yearlyOperationsLiveData: MutableLiveData<Triple<Double, Double, Double>> =
        MutableLiveData()

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
        yearlyOperationsLiveData = MutableLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        yearlyOperationsLiveData.observe(viewLifecycleOwner) {
            val (totalIncome, totalOutcome, balance) = it
            binding.textIncomesValue.text = "$totalIncome PLN"
            binding.textIncomesValue.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.main_green
                )
            )
            binding.textOutcomesValue.text = "$totalOutcome PLN"
            binding.textOutcomesValue.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.main_red
                )
            )
            binding.textBalanceValue.text = "$balance PLN"
            if (balance > 0) {
                binding.textBalanceValue.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.main_green
                    )
                )
            } else if (balance < 0) {
                binding.textBalanceValue.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.main_red
                    )
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            yearlyOperationsLiveData.value =
                yearlyOperationsSummaryViewModel.calculateYearlyIncomeAndOutcome()
        }
    }
}