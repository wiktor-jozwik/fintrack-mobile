package com.example.moneytracker.view.ui

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentUserLoginBinding
import com.example.moneytracker.view.ui.utils.isValidEmail
import com.example.moneytracker.viewmodel.LoginUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginUserFragment @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : Fragment(R.layout.fragment_user_login) {
    private val loginUserViewModel: LoginUserViewModel by viewModels()

    @Inject
    lateinit var homeFragment: HomeFragment
    @Inject
    lateinit var welcomeFragment: WelcomeFragment

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
        _binding = null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailTextChangeListener()
        passwordTextChangeListener()

        binding.buttonGoBack.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.mainFrameLayoutFragment, welcomeFragment)
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
            loginUserViewModel.loginUser(
                binding.inputEmailText.text.toString(),
                binding.inputPasswordText.text.toString(),
            ).observe(viewLifecycleOwner) {
                with(sharedPreferences.edit()) {
                    putString("JWT_AUTH_TOKEN", it.jwt)
                    apply()
                }

                binding.inputEmailText.setText("")
                binding.inputEmailContainer.helperText = ""
                binding.inputPasswordText.setText("")
                binding.inputPasswordContainer.helperText = ""

                switchToHome()
            }
        }
    }

    private fun switchToHome() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.mainFrameLayoutFragment, homeFragment)
            commit()
        }
    }

    private fun invalidForm() {
        val emailText = validateEmail()
        val passwordText = validatePassword()

        binding.inputEmailContainer.helperText = emailText
        binding.inputPasswordContainer.helperText = passwordText

        AlertDialog.Builder(activity)
            .setTitle("Invalid form")
            .setMessage("Please provide requested fields.")
            .setPositiveButton("Okay") { _, _ -> {} }
            .show()
    }
}