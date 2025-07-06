package com.emuready.emuready.data.remote.api.auth

import android.content.Context
import com.clerk.android.Clerk
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Clerk Authentication Interceptor
 * Based on the EmuReady API documentation
 * 
 * This interceptor adds the necessary headers for tRPC communication:
 * - Authorization: Bearer <clerk_token>
 * - Content-Type: application/json
 * - x-trpc-source: mobile
 * - x-client-type: android
 */
@Singleton
class ClerkAuthInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Get Clerk token (this will need to be implemented when Clerk SDK is added)
        val token = getClerkToken()
        
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("x-trpc-source", "mobile")
            .addHeader("x-client-type", "android")
        
        // Add Authorization header if token is available
        token?.let { bearerToken ->
            requestBuilder.addHeader("Authorization", "Bearer $bearerToken")
        }
        
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
    
    /**
     * Get Clerk token from the Clerk SDK
     */
    private fun getClerkToken(): String? {
        return try {
            Clerk.shared.session?.getToken()
        } catch (e: Exception) {
            // Return null if Clerk is not initialized or session is invalid
            null
        }
    }
    
    /**
     * Check if user is authenticated
     */
    fun isAuthenticated(): Boolean {
        return try {
            Clerk.shared.session != null && getClerkToken() != null
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Sign out user (clear Clerk session)
     */
    suspend fun signOut() {
        try {
            Clerk.shared.signOut()
        } catch (e: Exception) {
            // Handle sign out error
        }
    }
}