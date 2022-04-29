package com.example.moneytracker.view.ui

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.moneytracker.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_summary.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val yearlyOperationsSummaryFragment = YearlyOperationsSummaryFragment()
        val operationListFragment = OperationListFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, yearlyOperationsSummaryFragment)
            commit()
        }

        buttonListOperations.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, operationListFragment)
                commit()
            }
        }

        buttonShowYearlySummary.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, yearlyOperationsSummaryFragment)
                commit()
            }
        }
    }
}