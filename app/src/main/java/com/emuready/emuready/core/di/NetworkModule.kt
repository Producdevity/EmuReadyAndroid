package com.emuready.emuready.core.di

import com.emuready.emuready.BuildConfig
import com.emuready.emuready.data.network.AuthInterceptor
import com.emuready.emuready.data.remote.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    // Network configuration is now handled by EmuReadyApiModule
    // Old API services removed since we use tRPC API exclusively
    
}