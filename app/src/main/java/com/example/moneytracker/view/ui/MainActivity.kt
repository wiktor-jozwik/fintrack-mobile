package com.example.moneytracker.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moneytracker.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val yearlyOperationsSummaryFragment = YearlyOperationsSummaryFragment()
        val operationListFragment = OperationListFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, yearlyOperationsSummaryFragment)
            commit()
        }

        btnOperationList.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, operationListFragment)
                commit()
            }
        }

        btnYearlySummary.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, yearlyOperationsSummaryFragment)
                commit()
            }
        }
    }
}