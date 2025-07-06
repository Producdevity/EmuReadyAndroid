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
    
    // OkHttpClient and Json providers removed to avoid conflicts with EmuReadyApiClient
    
    @Provides
    @Singleton
    fun provideGameApiService(@Named("rest") retrofit: Retrofit): GameApiService {
        return retrofit.create(GameApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideListingApiService(@Named("rest") retrofit: Retrofit): ListingApiService {
        return retrofit.create(ListingApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideAuthApiService(@Named("rest") retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
}