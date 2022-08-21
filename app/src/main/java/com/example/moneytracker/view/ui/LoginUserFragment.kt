package com.example.moneytracker.view.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentUserLoginBinding
import com.example.moneytracker.service.model.mt.JwtResponse
import com.example.moneytracker.view.ui.utils.isValidEmail
import com.example.moneytracker.view.ui.utils.makeErrorToast
import com.example.moneytracker.view.ui.utils.responseErrorHandler
import com.example.moneytracker.viewmodel.LoginUserViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class LoginUserFragment @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val registerUserFragment: RegisterUserFragment
) : Fragment(R.layout.fragment_user_login) {
    private val loginUserViewModel: LoginUserViewModel by viewModels()

    @Inject
    lateinit var homeFragment: HomeFragment

    private var loginUserLiveData: MutableLiveData<Response<JwtResponse>> = MutableLiveData()

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

        loginUserLiveData.observe(viewLifecycleOwner) {
            try {
                val res = responseErrorHandler(it)
                Log.d("MT", res.toString())
                with(sharedPreferences.edit()) {
                    putString("JWT_AUTH_TOKEN", res.jwtToken)
                    apply()
                }

                switchToHome()
                clearFields()
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message)
            }
        }

        emailTextChangeListener()
        passwordTextChangeListener()

        binding.registerLink.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.mainFrameLayoutFragment, registerUserFragment)
                commit()
            }
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
            loginUserLiveData.value = loginUserViewModel.loginUser(
                binding.inputEmailText.text.toString(),
                binding.inputPasswordText.text.toString(),
            )
        }
    }

    private fun switchToHome() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.mainFrameLayoutFragment, homeFragment)
            commit()
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
            .setPositiveButton("Okay") { _, _ -> {} }
            .show()
    }
}