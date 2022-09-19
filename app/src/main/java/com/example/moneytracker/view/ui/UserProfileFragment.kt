package com.example.moneytracker.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentUserProfileBinding
import com.example.moneytracker.service.model.mt.Expenses
import com.example.moneytracker.service.model.mt.UserProfileData
import com.example.moneytracker.view.ui.utils.makeErrorToast
import com.example.moneytracker.view.ui.utils.responseErrorHandler
import com.example.moneytracker.viewmodel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response

@AndroidEntryPoint
class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {
    private val userProfileViewModel: UserProfileViewModel by viewModels()

    private var userProfileLiveData: MutableLiveData<Response<UserProfileData>> = MutableLiveData()
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
            try {
                val userProfileData = responseErrorHandler(it)
                var (email, firstName, lastName, phoneNumber) = userProfileData.user

                email = getValidText(email)
                firstName = getValidText(firstName)
                lastName = getValidText(lastName)
                phoneNumber = getValidText(phoneNumber)
                email = getValidText(email)

                binding.textFirstNameData.text = firstName
                binding.textFirstNameProgressBar.visibility = View.INVISIBLE

                binding.textLastNameData.text = lastName
                binding.textLastNameProgressBar.visibility = View.INVISIBLE

                binding.textPhoneNumberData.text = phoneNumber
                binding.textPhoneNumberProgressBar.visibility = View.INVISIBLE

                binding.textEmailData.text = email
                binding.textEmailProgressBar.visibility = View.INVISIBLE

                binding.textDefaultCurrencyData.text = userProfileData.defaultCurrency.name
                binding.textDefaultCurrencyProgressBar.visibility = View.INVISIBLE
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
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

    private fun getValidText(text: String?): String {
        val maxCharacterForText = 15

        if (text.isNullOrBlank()) {
            return "-"
        }

        return if (text.length > maxCharacterForText) {
            text.substring(0, maxCharacterForText) + ".."
        } else {
            text.substring(0, maxCharacterForText.coerceAtMost(text.length))
        }
    }
}