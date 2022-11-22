package com.example.fintrack.modules

import com.example.fintrack.viewmodel.utils.CurrencyCalculator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {
    @Singleton
    @Provides
    fun provideCurrencyCalculator() = CurrencyCalculator()
}