package com.example.fintrack.modules

import com.example.fintrack.view.ui.datepickers.*
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
    fun provideOperationSaveDatePickerFragment() = OperationSaveDatePickerFragment("OperationSaveDatePickerFragment")

    @Singleton
    @Provides
    fun provideCategoryStartDatePickerFragment() = CategoryStartDatePickerFragment("CategoryStartDatePickerFragment")

    @Singleton
    @Provides
    fun provideCategoryEndDatePickerFragment() = CategoryEndDatePickerFragment("CategoryEndDatePickerFragment")

    @Singleton
    @Provides
    fun provideOperationFilterStartDatePickerFragment() = OperationFilterStartDatePickerFragment("OperationFilterStartDatePickerFragment")

    @Singleton
    @Provides
    fun provideOperationFilterEndDatePickerFragment() = OperationFilterEndDatePickerFragment("OperationFilterEndDatePickerFragment")
}