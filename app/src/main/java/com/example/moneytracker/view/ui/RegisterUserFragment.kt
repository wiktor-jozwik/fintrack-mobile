package com.example.moneytracker.view.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentUserRegisterBinding
import com.example.moneytracker.viewmodel.RegisterUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterUserFragment: Fragment(R.layout.fragment_user_register) {
    private val registerUserViewModel: RegisterUserViewModel by viewModels()

    @Inject
    lateinit var loginUserFragment: LoginUserFragment
    @Inject
    lateinit var welcomeFragment: WelcomeFragment

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGoBack.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, welcomeFragment)
                commit()
            }
        }

        binding.buttonRegister.setOnClickListener {
            submitForm()
        }
    }

    private fun submitForm() {
//        val validMoneyAmount = binding.inputMoneyAmountText.text?.isNotEmpty()
//        val validName = binding.inputNameText.text?.isNotEmpty()
        val validEmail = true
        val validPassword = true // pass == passConf

        if (validEmail == true && validPassword == true) {
            validForm()
        } else {
            invalidForm()
        }
    }

    private fun validForm() {
        viewLifecycleOwner.lifecycleScope.launch {
            registerUserViewModel.registerUser(
                binding.inputEmailText.text.toString(),
                binding.inputPasswordText.text.toString(),
                binding.inputPasswordConfirmationText.text.toString()
            ).observe(viewLifecycleOwner) {
                switchToLogin()
            }
        }
    }

    private fun switchToLogin() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, loginUserFragment)
            commit()
        }
    }

    private fun invalidForm() {
//        binding.inputNameContainer.helperText = validOperationName()
//        binding.inputMoneyAmountContainer.helperText = validMoneyAmount()

        AlertDialog.Builder(activity)
            .setTitle("Invalid form")
            .setMessage("Please provide all fields.")
            .setPositiveButton("Okay") { _, _ -> {} }
            .show()
    }
}