package com.example.moneytracker.modules

import com.example.moneytracker.service.repository.internal.CurrencyRepository
import com.example.moneytracker.service.repository.internal.CategoryRepository
import com.example.moneytracker.service.repository.internal.OperationRepository
import com.example.moneytracker.service.repository.internal.UserRepository
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
    fun provideUserRepository() = UserRepository()

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