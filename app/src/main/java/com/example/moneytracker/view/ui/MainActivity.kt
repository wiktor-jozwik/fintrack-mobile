package com.example.moneytracker.view.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.moneytracker.R
import com.example.moneytracker.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var welcomeFragment: WelcomeFragment

    @Inject
    lateinit var homeFragment: HomeFragment

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val token = sharedPreferences.getString(getString(R.string.jwt_auth_token), "")

        if (token != null && token.isNotEmpty()) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, homeFragment)
                commit()
            }
        } else {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, welcomeFragment)
                commit()
            }
        }
    }
}