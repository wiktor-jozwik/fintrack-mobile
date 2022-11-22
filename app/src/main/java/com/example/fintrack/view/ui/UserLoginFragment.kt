package com.example.fintrack.view.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import com.example.fintrack.R
import com.example.fintrack.databinding.FragmentUserLoginBinding
import com.example.fintrack.service.model.ft.JwtResponse
import com.example.fintrack.view.ui.utils.isValidEmail
import com.example.fintrack.view.ui.utils.makeErrorToast
import com.example.fintrack.viewmodel.UserLoginViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserLoginFragment : Fragment(R.layout.fragment_user_login) {
    private val userLoginViewModel: UserLoginViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var loginUserLiveData: MutableLiveData<JwtResponse> = MutableLiveData()

    private var _binding: FragmentUserLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        loginUserLiveData = MutableLiveData()
    }

    override fun onResume() {
        super.onResume()
        clearHelpers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.GONE

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog)
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes") { _, _ -> run { activity?.finish() } }
                        .setNegativeButton("No") { dialog, _ -> run { dialog.dismiss() } }
                        .show()
                }
            })

        loginUserLiveData.observe(viewLifecycleOwner) {
            with(sharedPreferences.edit()) {
                putString("JWT_ACCESS_TOKEN", it.jwtAccessToken)
                putString("JWT_REFRESH_TOKEN", it.jwtRefreshToken)
                apply()
            }

            findNavController(view).navigate(R.id.action_userLoginFragment_to_yearlyOperationsSummaryFragment)
            clearFields()
        }

        emailTextChangeListener()
        passwordTextChangeListener()

        binding.registerLink.setOnClickListener {
            findNavController(view).navigate(R.id.action_userLoginFragment_to_userRegisterFragment)
        }

        binding.resendEmailLink.setOnClickListener {
            findNavController(view).navigate(R.id.action_userLoginFragment_to_resendEmailConfirmationFragment)
        }

        binding.buttonLogin.setOnClickListener {
            submitForm()
        }
    }

    private fun submitForm() {
        if (validateEmail() == null && validatePassword() == null) {
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

    private fun passwordTextChangeListener() {
        binding.inputPasswordText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                binding.inputPasswordContainer.helperText = validatePassword()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun validatePassword(): String? {
        val password = binding.inputPasswordText.text.toString()
        if (password.isEmpty()) {
            return "Please provide password."
        }
        return null
    }

    private fun validForm() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                loginUserLiveData.value = userLoginViewModel.loginUser(
                    binding.inputEmailText.text.toString(),
                    binding.inputPasswordText.text.toString(),
                )
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }
    }

    private fun clearFields() {
        binding.inputEmailText.setText("")
        binding.inputPasswordText.setText("")
        clearHelpers()
    }

    private fun clearHelpers() {
        binding.inputEmailContainer.helperText = ""
        binding.inputPasswordContainer.helperText = ""
    }

    private fun invalidForm() {
        val emailText = validateEmail()
        val passwordText = validatePassword()

        binding.inputEmailContainer.helperText = emailText
        binding.inputPasswordContainer.helperText = passwordText

        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog)
            .setTitle("Invalid form")
            .setMessage("Please provide requested fields.")
            .setPositiveButton("Okay") { _, _ -> run {} }
            .show()
    }
}