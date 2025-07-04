package com.emuready.emuready.data.remote.api

import com.emuready.emuready.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("auth/refresh")
    suspend fun refreshToken(@Header("Authorization") refreshToken: String): Response<AuthResponse>
    
    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>
    
    @GET("auth/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<UserDto>
}