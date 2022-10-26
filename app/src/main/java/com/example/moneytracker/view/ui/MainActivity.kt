package com.example.moneytracker.view.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.moneytracker.R
import com.example.moneytracker.databinding.ActivityMainBinding
import com.example.moneytracker.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedDestinationId = item.itemId
            if (selectedDestinationId == R.id.userLogoutNav) {
                handleNavigationLogoutSelected(navController)
                return@setOnItemSelectedListener true
            }

            val stackCount: Int = navHostFragment.childFragmentManager.backStackEntryCount

            if (stackCount > 1) {
                navController.popBackStack(selectedDestinationId, true)
            }

            navController.navigate(selectedDestinationId)

            return@setOnItemSelectedListener true
        }
    }

    private fun handleNavigationLogoutSelected(navController: NavController) {
        MaterialAlertDialogBuilder(this, R.style.AlertDialog)
            .setCancelable(false)
            .setMessage("Are you sure to logout?")
            .setPositiveButton("Yes") { _, _ ->
                lifecycleScope.launch {
                    try {
                        mainViewModel.logout()
                    } catch (e: Exception) {
                    } finally {
                        eraseTokens()
                        navController.navigate(R.id.userLoginFragment)
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun eraseTokens() {
        with(sharedPreferences.edit()) {
            putString("JWT_ACCESS_TOKEN", "")
            putString("JWT_REFRESH_TOKEN", "")
            apply()
        }
    }
}