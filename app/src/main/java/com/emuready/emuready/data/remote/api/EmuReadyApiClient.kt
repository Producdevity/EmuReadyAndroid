package com.emuready.emuready.data.remote.api

// import com.clerk.android.Clerk
import com.emuready.emuready.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * EmuReady API Client
 * Configured exactly according to the official API documentation
 * Base URLs:
 * - tRPC Endpoint: https://emuready.com/api/mobile/trpc
 * - REST Endpoints: https://emuready.com/api/mobile/
 */
@Module
@InstallIn(SingletonComponent::class)
object EmuReadyApiClient {
    
    private const val BASE_URL = "https://www.emuready.com/api/mobile/"
    private const val TRPC_BASE_URL = "https://www.emuready.com/api/mobile/trpc/"
    
    /**
     * JSON configuration for kotlinx.serialization
     * Configured to handle the API's exact JSON format
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        allowStructuredMapKeys = true
        allowTrailingComma = true
    }
    
    /**
     * Clerk Authentication Interceptor
     */
    @Provides
    @Singleton
    fun provideClerkAuthInterceptor(): Interceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
        
        // Add required headers as specified in API documentation
        requestBuilder.apply {
            addHeader("Content-Type", "application/json")
            addHeader("x-trpc-source", "android")
            addHeader("x-client-type", "android")
            
            // Clerk JWT token will be added when authentication is integrated
            /*
            try {
                val session = Clerk.shared.session
                session?.getToken()?.let { token ->
                    addHeader("Authorization", "Bearer $token")
                }
            } catch (e: Exception) {
                // Log but don't fail - some endpoints are public
                android.util.Log.w("ClerkAuth", "Failed to get auth token: ${e.message}")
            }
            */
        }
        
        chain.proceed(requestBuilder.build())
    }
    
    /**
     * HTTP Logging Interceptor for debugging
     */
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
    
    /**
     * OkHttp Client with authentication and proper configuration
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
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
            .baseUrl(TRPC_BASE_URL)
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