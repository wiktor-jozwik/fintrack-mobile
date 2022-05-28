package com.example.moneytracker.view.ui

import android.os.Bundle
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
    lateinit var operationListFragment: OperationListFragment
    @Inject
    lateinit var categoryListFragment: CategoryListFragment
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

        binding.buttonListOperations.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, operationListFragment)
                commit()
            }
        }

        binding.buttonListCategories.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, categoryListFragment)
                commit()
            }
        }

        binding.buttonShowYearlySummary.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, yearlyOperationsSummaryFragment)
                commit()
            }
        }


        binding.buttonShowChart.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, chartFragment)
                commit()
            }
        }
    }
}