package com.example.moneytracker.view.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : Fragment(R.layout.fragment_home) {
    @Inject
    lateinit var yearlyOperationsSummaryFragment: YearlyOperationsSummaryFragment

    @Inject
    lateinit var addFragment: AddFragment

    @Inject
    lateinit var listFragment: ListFragment

    @Inject
    lateinit var chartFragment: ChartFragment

    @Inject
    lateinit var loginUserFragment: LoginUserFragment

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

//        logoutUserLiveData.observe(viewLifecycleOwner) {
//            try {
//                responseErrorHandler(it)
//                with(sharedPreferences.edit()) {
//                    putString("JWT_AUTH_TOKEN", "")
//                    apply()
//                }
//                switchToLogin()
//            } catch (e: Exception) {
//                makeErrorToast(requireContext(), e.message, 200)
//            }
//        }

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

        binding.buttonAdd.setOnClickListener {
            setButtonsWhite()
            binding.buttonAdd.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.primary
                )
            )
            childFragmentManager.beginTransaction().apply {
                replace(R.id.homeFrameLayoutFragment, addFragment)
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
//        with(sharedPreferences.edit()) {
//            putString("JWT_AUTH_TOKEN", "")
//            apply()
//        }
        binding.buttonLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog)
                .setCancelable(false)
                .setMessage("Are you sure to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        logout()
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
            putString("JWT_AUTH_TOKEN", "")
            apply()
        }
        switchToLogin()
    }

    private fun switchToLogin() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.mainFrameLayoutFragment, loginUserFragment)
            commit()
        }
    }

    private fun setButtonsWhite() {
        val color = ContextCompat.getColor(requireContext(), R.color.white)
        binding.buttonHome.setColorFilter(color)
        binding.buttonList.setColorFilter(color)
        binding.buttonAdd.setColorFilter(color)
        binding.buttonChart.setColorFilter(color)
    }
}