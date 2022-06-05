package com.example.moneytracker.view.ui

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentUserLoginBinding
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

        binding.buttonGoBack.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, welcomeFragment)
                commit()
            }
        }

        binding.buttonLogin.setOnClickListener {
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
            loginUserViewModel.loginUser(
                binding.inputEmailText.text.toString(),
                binding.inputPasswordText.text.toString(),
            ).observe(viewLifecycleOwner) {
                with(sharedPreferences.edit()) {
                    putString(getString(R.string.jwt_auth_token), it.jwt)
                    apply()
                }
                switchToHome()
            }
        }
    }

    private fun switchToHome() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, homeFragment)
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