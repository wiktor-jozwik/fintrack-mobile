package com.example.moneytracker.modules

import com.example.moneytracker.viewmodel.utils.CurrencyCalculator
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