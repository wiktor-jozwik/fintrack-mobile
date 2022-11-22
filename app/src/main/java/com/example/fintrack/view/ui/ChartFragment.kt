package com.example.fintrack.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.example.fintrack.R
import com.example.fintrack.databinding.FragmentChartBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChartFragment : Fragment(R.layout.fragment_chart) {
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonShowPeriodOperationsChart.setOnClickListener {
            findNavController(view).navigate(R.id.action_chartFragment_to_chartPeriodOperationsFragment)
        }

        binding.buttonShowCategoriesSplitChart.setOnClickListener {
            findNavController(view).navigate(R.id.action_chartFragment_to_chartCategoriesSplitFragment)

        }

//
//        binding.buttonShowGoldChart.setOnClickListener {
//            parentFragmentManager.beginTransaction().apply {
//                replace(R.id.homeFrameLayoutFragment, chartGoldFragment)
//                commit()
//            }
//        }

        binding.buttonShowCurrencyChart.setOnClickListener {
            findNavController(view).navigate(R.id.action_chartFragment_to_chartCurrencyFragment)
        }
    }
}
