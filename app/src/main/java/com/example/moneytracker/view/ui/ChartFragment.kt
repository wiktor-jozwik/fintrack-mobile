package com.example.moneytracker.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentChartBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ChartFragment : Fragment(R.layout.fragment_chart) {
    @Inject
    lateinit var chartPeriodOperationsFragment: ChartPeriodOperationsFragment

    @Inject
    lateinit var chartCategoriesSplitFragment: ChartCategoriesSplitFragment

    @Inject
    lateinit var chartGoldFragment: ChartGoldFragment

    @Inject
    lateinit var chartCurrencyFragment: ChartCurrencyFragment

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
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.homeFrameLayoutFragment, chartPeriodOperationsFragment)
                commit()
            }
        }

        binding.buttonShowCategoriesSplitChart.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.homeFrameLayoutFragment, chartCategoriesSplitFragment)
                commit()
            }
        }


        binding.buttonShowGoldChart.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.homeFrameLayoutFragment, chartGoldFragment)
                commit()
            }
        }

        binding.buttonShowCurrencyChart.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.homeFrameLayoutFragment, chartCurrencyFragment)
                commit()
            }
        }
    }
}
