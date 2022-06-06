package com.example.moneytracker.view.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentUserRegisterBinding
import com.example.moneytracker.service.model.User
import com.example.moneytracker.view.ui.utils.isValidEmail
import com.example.moneytracker.view.ui.utils.responseErrorHandler
import com.example.moneytracker.viewmodel.RegisterUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class RegisterUserFragment : Fragment(R.layout.fragment_user_register) {
    private val registerUserViewModel: RegisterUserViewModel by viewModels()

    @Inject
    lateinit var loginUserFragment: LoginUserFragment

    private var registerUserLiveData: MutableLiveData<Response<User>> = MutableLiveData()

    private var _binding: FragmentUserRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
        registerUserLiveData = MutableLiveData()
    }

    override fun onResume() {
        super.onResume()
        clearHelpers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerUserLiveData.observe(viewLifecycleOwner) {
            try {
                responseErrorHandler(it)
                switchToLogin()
                clearFields()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }

        emailTextChangeListener()
        passwordTextChangeListener()

        binding.loginLink.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.mainFrameLayoutFragment, loginUserFragment)
                commit()
            }
        }

        binding.buttonRegister.setOnClickListener {
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

    private fun validForm() {
        viewLifecycleOwner.lifecycleScope.launch {
            registerUserLiveData.value = registerUserViewModel.registerUser(
                binding.inputEmailText.text.toString(),
                binding.inputPasswordText.text.toString(),
                binding.inputPasswordConfirmationText.text.toString()
            )
        }
    }

    private fun switchToLogin() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.mainFrameLayoutFragment, loginUserFragment)
            commit()
        }
    }

    private fun clearFields() {
        binding.inputEmailText.setText("")
        binding.inputPasswordText.setText("")
        binding.inputPasswordConfirmationText.setText("")
        clearHelpers()
    }

    private fun clearHelpers() {
        binding.inputEmailContainer.helperText = ""
        binding.inputPasswordContainer.helperText = ""
        binding.inputPasswordConfirmationContainer.helperText = ""
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

        if (emailText.isBlank()) {
            return "Please provide email."
        } else if (!emailText.isValidEmail()) {
            return "Please provide valid email."
        }
        return null
    }

    private fun passwordTextChangeListener() {
        binding.inputPasswordText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                binding.inputPasswordContainer.helperText = validatePassword()
                binding.inputPasswordConfirmationContainer.helperText = validatePassword()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        binding.inputPasswordConfirmationText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                binding.inputPasswordContainer.helperText = validatePassword()
                binding.inputPasswordConfirmationContainer.helperText = validatePassword()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun validatePassword(): String? {
        val password = binding.inputPasswordText.text.toString()
        val passwordConfirmation = binding.inputPasswordConfirmationText.text.toString()
        if (password.isEmpty() || passwordConfirmation.isEmpty()) {
            return "Please provide password and confirm it."
        } else if (password.count() < 8) {
            return "Password should be at least 8 char's length."
        } else if (password != passwordConfirmation) {
            return "Password's does not match."
        }
        return null
    }

    private fun invalidForm() {
        val emailText = validateEmail()
        val passwordText = validatePassword()

        binding.inputEmailContainer.helperText = emailText
        binding.inputPasswordContainer.helperText = passwordText
        binding.inputPasswordConfirmationContainer.helperText = passwordText

        AlertDialog.Builder(activity)
            .setTitle("Invalid form")
            .setMessage("Please provide requested fields.")
            .setPositiveButton("Okay") { _, _ -> {} }
            .show()
    }
}