package com.example.fintrack.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.fintrack.R
import com.example.fintrack.databinding.FragmentUserProfileBinding
import com.example.fintrack.service.model.ft.Expenses
import com.example.fintrack.service.model.ft.UserProfileData
import com.example.fintrack.view.ui.utils.makeErrorToast
import com.example.fintrack.viewmodel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {
    private val userProfileViewModel: UserProfileViewModel by viewModels()

    private var userProfileLiveData: MutableLiveData<UserProfileData> = MutableLiveData()
    private var expensesLiveData: MutableLiveData<Expenses> = MutableLiveData()

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
        userProfileLiveData = MutableLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userProfileLiveData.observe(viewLifecycleOwner) {
            val (email, firstName, lastName, phoneNumber) = it.user

            binding.textFirstNameData.text = firstName
            binding.textFirstNameProgressBar.visibility = View.INVISIBLE

            binding.textLastNameData.text = lastName
            binding.textLastNameProgressBar.visibility = View.INVISIBLE

            binding.textPhoneNumberData.text = phoneNumber
            binding.textPhoneNumberProgressBar.visibility = View.INVISIBLE

            binding.textEmailData.text = email
            binding.textEmailProgressBar.visibility = View.INVISIBLE

            binding.textDefaultCurrencyData.text = it.defaultCurrency.name
            binding.textDefaultCurrencyProgressBar.visibility = View.INVISIBLE
        }

        expensesLiveData.observe(viewLifecycleOwner) {
            binding.textTotalIncomeData.text = it.incomes.toString()
            binding.textTotalIncomeProgressBar.visibility = View.INVISIBLE

            binding.textTotalOutcomeData.text = it.outcomes.toString()
            binding.textTotalOutcomeProgressBar.visibility = View.INVISIBLE
        }

        fetchUserProfileData()
        fetchOperations()
    }

    private fun fetchUserProfileData() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                userProfileLiveData.value = userProfileViewModel.getProfileData()
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }
    }

    private fun fetchOperations() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                expensesLiveData.value = userProfileViewModel.getTotalExpenses()
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }
    }
}