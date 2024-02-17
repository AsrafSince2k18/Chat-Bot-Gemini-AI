package com.example.chatbot.root.di

import com.example.chatbot.root.data.repositoryImpl.GeminiRepositoryImpl
import com.example.chatbot.root.domain.repository.GeminiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleDatabase {

    @Provides
    @Singleton
    fun provideDatabase(): GeminiRepository{
        return GeminiRepositoryImpl()
    }

}