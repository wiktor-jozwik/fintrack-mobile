package com.example.moneytracker.view.ui

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
    lateinit var yearlyOperationsSummaryFragment: YearlyOperationsSummaryFragment
    @Inject
    lateinit var addFragment: AddFragment
    @Inject
    lateinit var listFragment: ListFragment
    @Inject
    lateinit var chartFragment: ChartFragment
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, yearlyOperationsSummaryFragment)
            commit()
        }

        binding.buttonMain.setOnClickListener {
            Log.d("MT", "main")

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, yearlyOperationsSummaryFragment)
                commit()
            }
        }

        binding.buttonList.setOnClickListener {
            Log.d("MT", "list")

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, listFragment)
                commit()
            }
        }

        binding.buttonAdd.setOnClickListener {
            Log.d("MT", "add")

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, addFragment)
                commit()
            }
        }

        binding.buttonChart.setOnClickListener {
            Log.d("MT", "chart")

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, chartFragment)
                commit()
            }
        }
    }
}