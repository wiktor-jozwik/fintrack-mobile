package com.example.moneytracker.modules

import android.content.SharedPreferences
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
    fun provideUserLoginFragment(
        sharedPreferences: SharedPreferences,
        userRegisterFragment: UserRegisterFragment,
    ) =
        UserLoginFragment(sharedPreferences, userRegisterFragment)

    @Singleton
    @Provides
    fun provideUserRegisterFragment() = UserRegisterFragment()

    @Singleton
    @Provides
    fun provideUserProfileFragment() = UserProfileFragment()

    @Singleton
    @Provides
    fun provideHomeFragment(sharedPreferences: SharedPreferences) = HomeFragment(sharedPreferences)

    @Singleton
    @Provides
    fun provideYearlyOperationsSummaryFragment() = YearlyOperationsSummaryFragment()

    @Singleton
    @Provides
    fun provideSaveOperationFragment() = SaveOperationFragment()

    @Singleton
    @Provides
    fun provideSaveCategoryFragment() = SaveCategoryFragment()

    @Singleton
    @Provides
    fun provideSaveCurrencyFragment() = SaveCurrencyFragment()

    @Singleton
    @Provides
    fun provideListOperationFragment() = ListOperationFragment()

    @Singleton
    @Provides
    fun provideListCategoryFragment() = ListCategoryFragment()

    @Singleton
    @Provides
    fun provideListCurrencyFragment() = ListCurrencyFragment()

    @Singleton
    @Provides
    fun provideChartFragment() = ChartFragment()

    @Singleton
    @Provides
    fun provideChartPeriodOperationsFragment() = ChartPeriodOperationsFragment()

    @Singleton
    @Provides
    fun provideChartCategoriesSplitFragment() = ChartCategoriesSplitFragment()

    @Singleton
    @Provides
    fun provideChartGoldFragment() = ChartGoldFragment()

    @Singleton
    @Provides
    fun provideChartCurrencyFragment() = ChartCurrencyFragment()

    @Singleton
    @Provides
    fun provideListFragment() = ListFragment()

    @Singleton
    @Provides
    fun provideDatePickerFragment() = DatePickerFragment()

    @Singleton
    @Provides
    fun provideCategoryStartDatePickerFragment() = CategoryStartDatePickerFragment()

    @Singleton
    @Provides
    fun provideCategoryEndDatePickerFragment() = CategoryEndDatePickerFragment()

    @Singleton
    @Provides
    fun provideResendEmailConfirmationFragment() = ResendEmailConfirmationFragment()

    @Singleton
    @Provides
    fun provideImportOperationsFragment() = ImportOperationsFragment()
}