package com.emuready.emuready.data.remote.api

import com.emuready.emuready.data.remote.api.auth.ClerkAuthInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * EmuReady API Client
 * Configured according to the official API documentation
 * Base URLs:
 * - tRPC Endpoint: https://emuready.com/api/mobile/trpc
 * - REST Endpoints: https://emuready.com/api/mobile/
 */
@Module
@InstallIn(SingletonComponent::class)
object EmuReadyApiClient {
    
    private const val BASE_URL = "https://emuready.com/api/mobile/"
    
    /**
     * JSON configuration for kotlinx.serialization
     * Configured to handle the API's JSON format
     */
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        allowStructuredMapKeys = true
    }
    
    /**
     * HTTP Logging Interceptor for debugging
     */
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    
    /**
     * OkHttp Client with authentication and logging
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: ClerkAuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    /**
     * Retrofit instance for tRPC endpoints
     */
    @Provides
    @Singleton
    @Named("trpc")
    fun provideTrpcRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("${BASE_URL}trpc/")
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }
    
    /**
     * Retrofit instance for REST endpoints
     */
    @Provides
    @Singleton
    @Named("rest")
    fun provideRestRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }
    
    /**
     * tRPC API Service
     */
    @Provides
    @Singleton
    fun provideTrpcApiService(
        @Named("trpc") retrofit: Retrofit
    ): EmuReadyTrpcApiService {
        return retrofit.create(EmuReadyTrpcApiService::class.java)
    }
    
    /**
     * REST API Service
     */
    @Provides
    @Singleton
    fun provideRestApiService(
        @Named("rest") retrofit: Retrofit
    ): EmuReadyRestApiService {
        return retrofit.create(EmuReadyRestApiService::class.java)
    }
}