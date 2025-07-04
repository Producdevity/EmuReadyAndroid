package com.emuready.emuready.data.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

@Singleton
class AuthInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Skip authentication for auth endpoints
        if (originalRequest.url.pathSegments.contains("auth")) {
            return chain.proceed(originalRequest)
        }
        
        // Add authentication token to other requests
        val token = runBlocking {
            try {
                val tokenKey = stringPreferencesKey("auth_token")
                context.dataStore.data.map { preferences ->
                    preferences[tokenKey]
                }.first()
            } catch (e: Exception) {
                null
            }
        }
        
        val requestBuilder = originalRequest.newBuilder()
        
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        
        return chain.proceed(requestBuilder.build())
    }
}