package com.example.moneytracker.modules

import com.example.moneytracker.service.repository.CurrencyRepository
import com.example.moneytracker.service.repository.CategoryRepository
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
    fun provideOperationCategoryRepository() = CategoryRepository()
    @Singleton
    @Provides
    fun provideCurrencyRepository() = CurrencyRepository()
}