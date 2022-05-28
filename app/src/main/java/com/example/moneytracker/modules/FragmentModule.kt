package com.example.moneytracker.modules

import com.example.moneytracker.view.ui.*
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
    fun provideYearlyOperationsSummaryFragment() = YearlyOperationsSummaryFragment()

    @Singleton
    @Provides
    fun provideAddOperationFragment() = AddOperationFragment()

    @Singleton
    @Provides
    fun provideAddCategoryFragment() = AddCategoryFragment()

    @Singleton
    @Provides
    fun provideOperationListFragment() = OperationListFragment()

    @Singleton
    @Provides
    fun provideOperationCategoryListFragment() = CategoryListFragment()

    @Singleton
    @Provides
    fun provideChartFragment() = ChartFragment()

    @Singleton
    @Provides
    fun provideDatePickerFragment() = DatePickerFragment()

    @Singleton
    @Provides
    fun provideTimePickerFragment() = TimePickerFragment()
}