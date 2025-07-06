package com.emuready.emuready.data.remote.api

import com.emuready.emuready.data.remote.dto.MobileUser
import retrofit2.Response
import retrofit2.http.*

/**
 * EmuReady REST API Service
 * For endpoints that are not tRPC-based (like auth validation)
 * Based on the official API documentation
 */
interface EmuReadyRestApiService {
    
    /**
     * REST Endpoint: /api/mobile/auth
     * Method: GET
     * Purpose: Validates user session and returns user data
     * Headers: Authorization: Bearer <clerk_token>
     */
    @GET("auth")
    suspend fun validateAuth(): Response<AuthValidationResponse>
}

@kotlinx.serialization.Serializable
data class AuthValidationResponse(
    val user: MobileUser
)