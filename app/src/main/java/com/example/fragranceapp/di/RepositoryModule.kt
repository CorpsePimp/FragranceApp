package com.example.fragranceapp.di

import com.example.fragranceapp.data.repository.AuthRepositoryImpl
import com.example.fragranceapp.data.repository.FragranceRepositoryImpl
import com.example.fragranceapp.domain.repository.AuthRepository
import com.example.fragranceapp.domain.repository.FragranceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindFragranceRepository(
        fragranceRepositoryImpl: FragranceRepositoryImpl
    ): FragranceRepository
    
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}