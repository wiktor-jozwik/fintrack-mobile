package com.example.moneytracker.modules

import com.example.moneytracker.view.ui.CategoryEndDatePickerFragment
import com.example.moneytracker.view.ui.CategoryStartDatePickerFragment
import com.example.moneytracker.view.ui.DatePickerFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FragmentModule {
    @Singleton
    @Provides
    fun provideDatePickerFragment() = DatePickerFragment()

    @Singleton
    @Provides
    fun provideCategoryStartDatePickerFragment() = CategoryStartDatePickerFragment()

    @Singleton
    @Provides
    fun provideCategoryEndDatePickerFragment() = CategoryEndDatePickerFragment()
}