package com.emuready.emuready.data.remote.api.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

// DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

/**
 * Authentication Interceptor for EmuReady API
 * 
 * This interceptor adds the necessary headers for tRPC communication:
 * - Authorization: Bearer <auth_token>
 * - Content-Type: application/json
 * - x-trpc-source: mobile
 * - x-client-type: android
 */
@Singleton
class ClerkAuthInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor {
    
    companion object {
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
    }
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Get authentication token from DataStore
        val token = getAuthToken()
        
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("x-trpc-source", "mobile")
            .addHeader("x-client-type", "android")
        
        // Check if this is a protected endpoint that requires authentication
        val url = originalRequest.url.toString()
        val isProtectedEndpoint = isProtectedEndpoint(url)
        
        // Add Authorization header if token is available or if endpoint requires it
        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        } else if (isProtectedEndpoint) {
            // For protected endpoints without token, we'll still proceed but the server will handle the auth error
            // This allows the app to show proper auth prompts instead of network errors
        }
        
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
    
    /**
     * Check if the endpoint requires authentication
     */
    private fun isProtectedEndpoint(url: String): Boolean {
        val protectedPaths = listOf(
            "auth.getSession",
            "auth.updateProfile", 
            "auth.deleteAccount",
            "listings.createListing",
            "listings.updateListing",
            "listings.deleteListing",
            "listings.voteListing",
            "listings.getUserVote",
            "notifications.",
            "preferences.",
            "trust.getMyTrustInfo",
            "developers.",
            "listingReports.create"
        )
        
        return protectedPaths.any { path -> url.contains(path) }
    }
    
    /**
     * Get authentication token from DataStore
     */
    private fun getAuthToken(): String? {
        return try {
            runBlocking {
                context.dataStore.data.map { preferences ->
                    preferences[AUTH_TOKEN_KEY]
                }.first()
            }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Check if user is authenticated
     */
    fun isAuthenticated(): Boolean {
        return getAuthToken() != null
    }
    
    /**
     * Save authentication token
     */
    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
        }
    }
    
    /**
     * Clear authentication token (sign out)
     */
    suspend fun signOut() {
        context.dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN_KEY)
        }
    }
}