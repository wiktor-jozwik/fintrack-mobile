package com.example.moneytracker.view.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentResendEmailConfirmationBinding
import com.example.moneytracker.service.model.mt.StringResponse
import com.example.moneytracker.view.ui.utils.isValidEmail
import com.example.moneytracker.view.ui.utils.makeErrorToast
import com.example.moneytracker.view.ui.utils.makePositiveToast
import com.example.moneytracker.viewmodel.ResendEmailConfirmationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ResendEmailConfirmationFragment(
) : Fragment(R.layout.fragment_resend_email_confirmation) {
    private val resendEmailConfirmationViewModel: ResendEmailConfirmationViewModel by viewModels()

    @Inject
    lateinit var loginFragment: UserLoginFragment

    private var resendEmailConfirmationLiveData: MutableLiveData<StringResponse> = MutableLiveData()

    private var _binding: FragmentResendEmailConfirmationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResendEmailConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        resendEmailConfirmationLiveData = MutableLiveData()
    }

    override fun onResume() {
        super.onResume()
        clearHelpers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resendEmailConfirmationLiveData.observe(viewLifecycleOwner) {
            makePositiveToast(requireContext(), it.response, 200)
            switchToLogin()
            clearFields()
        }

        emailTextChangeListener()

        binding.buttonResend.setOnClickListener {
            submitForm()
        }
    }

    private fun submitForm() {
        if (validateEmail() == null) {
            validForm()
        } else {
            invalidForm()
        }
    }

    private fun emailTextChangeListener() {
        binding.inputEmailText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                binding.inputEmailContainer.helperText = validateEmail()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun validateEmail(): String? {
        val emailText = binding.inputEmailText.text.toString()

        if (emailText.isEmpty() || !emailText.isValidEmail()) {
            return "Please provide valid email."
        }
        return null
    }

    private fun validForm() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                resendEmailConfirmationLiveData.value =
                    resendEmailConfirmationViewModel.resendEmail(
                        binding.inputEmailText.text.toString(),
                    )
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }
    }

    private fun switchToLogin() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.mainFrameLayoutFragment, loginFragment)
            commit()
        }
    }

    private fun clearFields() {
        binding.inputEmailText.setText("")
        clearHelpers()
    }

    private fun clearHelpers() {
        binding.inputEmailContainer.helperText = ""
    }

    private fun invalidForm() {
        val emailText = validateEmail()

        binding.inputEmailContainer.helperText = emailText

        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog)
            .setTitle("Invalid form")
            .setMessage("Please provide requested fields.")
            .setPositiveButton("Okay") { _, _ -> {} }
            .show()
    }
}