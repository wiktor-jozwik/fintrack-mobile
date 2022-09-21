package com.example.moneytracker.view.ui

import android.content.SharedPreferences
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
import com.example.moneytracker.databinding.FragmentHomeBinding
import com.example.moneytracker.service.model.mt.LogoutResponse
import com.example.moneytracker.viewmodel.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
    lateinit var listFragment: ListFragment

    @Inject
    lateinit var chartFragment: ChartFragment

    @Inject
    lateinit var userProfileFragment: UserProfileFragment

    @Inject
    lateinit var userLoginFragment: UserLoginFragment

    private var logoutUserLiveData: MutableLiveData<LogoutResponse> = MutableLiveData()

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
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logoutUserLiveData.observe(viewLifecycleOwner) {
            logout()
        }

        childFragmentManager.beginTransaction().apply {
            setButtonsWhite()
            binding.buttonHome.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.primary
                )
            )
            replace(R.id.homeFrameLayoutFragment, yearlyOperationsSummaryFragment)
            commit()
        }

        binding.buttonHome.setOnClickListener {
            setButtonsWhite()
            binding.buttonHome.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.primary
                )
            )
            childFragmentManager.beginTransaction().apply {
                replace(R.id.homeFrameLayoutFragment, yearlyOperationsSummaryFragment)
                commit()
            }
        }

        binding.buttonList.setOnClickListener {
            setButtonsWhite()
            binding.buttonList.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.primary
                )
            )
            childFragmentManager.beginTransaction().apply {
                replace(R.id.homeFrameLayoutFragment, listFragment)
                commit()
            }
        }

        binding.buttonChart.setOnClickListener {
            setButtonsWhite()
            binding.buttonChart.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.primary
                )
            )
            childFragmentManager.beginTransaction().apply {
                replace(R.id.homeFrameLayoutFragment, chartFragment)
                commit()
            }
        }

        binding.buttonProfile.setOnClickListener {
            setButtonsWhite()
            binding.buttonProfile.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.primary
                )
            )
            childFragmentManager.beginTransaction().apply {
                replace(R.id.homeFrameLayoutFragment, userProfileFragment)
                commit()
            }
        }

        binding.buttonLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog)
                .setCancelable(false)
                .setMessage("Are you sure to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            logoutUserLiveData.value = homeViewModel.logout()
                        } catch (e: Exception) {
                            switchToLogin()
                        }
                    }
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun logout() {
        with(sharedPreferences.edit()) {
            putString("JWT_ACCESS_TOKEN", "")
            putString("JWT_REFRESH_TOKEN", "")
            apply()
        }
        switchToLogin()
    }

    private fun switchToLogin() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.mainFrameLayoutFragment, userLoginFragment)
            commit()
        }
    }

    private fun setButtonsWhite() {
        val color = ContextCompat.getColor(requireContext(), R.color.white)
        binding.buttonHome.setColorFilter(color)
        binding.buttonList.setColorFilter(color)
        binding.buttonChart.setColorFilter(color)
        binding.buttonProfile.setColorFilter(color)
    }
}