package com.example.moneytracker.modules

import com.example.moneytracker.service.repository.CurrencyRepository
import com.example.moneytracker.service.repository.OperationCategoryRepository
import com.example.moneytracker.service.repository.OperationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideOperationRepository() = OperationRepository()
    @Singleton
    @Provides
    fun provideOperationCategoryRepository() = OperationCategoryRepository()
    @Singleton
    @Provides
    fun provideCurrencyRepository() = CurrencyRepository()
}