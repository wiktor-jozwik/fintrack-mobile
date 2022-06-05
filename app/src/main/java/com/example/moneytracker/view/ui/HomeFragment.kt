package com.example.moneytracker.view.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentHomeBinding
import com.example.moneytracker.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : Fragment(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var yearlyOperationsSummaryFragment: YearlyOperationsSummaryFragment

    @Inject
    lateinit var addFragment: AddFragment

    @Inject
    lateinit var listFragment: ListFragment

    @Inject
    lateinit var chartFragment: ChartFragment

    @Inject
    lateinit var welcomeFragment: WelcomeFragment

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

        childFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, yearlyOperationsSummaryFragment)
            commit()
        }

        binding.buttonHome.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, yearlyOperationsSummaryFragment)
                commit()
            }
        }

        binding.buttonList.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, listFragment)
                commit()
            }
        }

        binding.buttonAdd.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, addFragment)
                commit()
            }
        }

        binding.buttonChart.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, chartFragment)
                commit()
            }
        }

        binding.buttonLogout.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                homeViewModel.logoutUser().observe(viewLifecycleOwner) {
                    if (it.success) {
                        with(sharedPreferences.edit()) {
                            putString(getString(R.string.jwt_auth_token), "")
                            apply()
                        }
                    }
                }

                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.frameLayoutFragment, welcomeFragment)
                    commit()
                }
            }
        }
    }
}