package com.example.moneytracker.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    @Inject
    lateinit var yearlyOperationsSummaryFragment: YearlyOperationsSummaryFragment

    @Inject
    lateinit var addFragment: AddFragment

    @Inject
    lateinit var listFragment: ListFragment

    @Inject
    lateinit var chartFragment: ChartFragment

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragmentAdd, yearlyOperationsSummaryFragment)
            commit()
        }

        binding.buttonHome.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragmentAdd, yearlyOperationsSummaryFragment)
                commit()
            }
        }

        binding.buttonList.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragmentAdd, listFragment)
                commit()
            }
        }

        binding.buttonAdd.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragmentAdd, addFragment)
                commit()
            }
        }

        binding.buttonChart.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragmentAdd, chartFragment)
                commit()
            }
        }
    }
}