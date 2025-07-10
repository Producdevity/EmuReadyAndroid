package com.emuready.emuready.data.services

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.emuready.emuready.domain.entities.User
import com.emuready.emuready.domain.exceptions.AuthException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Authentication service that will integrate with Clerk when available
 * Currently uses mock implementation for development
 */
@Singleton
class ClerkAuthService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: DataStore<Preferences>
) {
    
    // Delegate to mock service for now
    private val mockAuthService = MockAuthService(context, dataStore)
    
    val currentUser: StateFlow<User?> = mockAuthService.currentUser
    val isAuthenticated: StateFlow<Boolean> = mockAuthService.isAuthenticated
    
    init {
        // Load any stored user session
        MainScope().launch {
            mockAuthService.loadStoredUserAsync()
        }
    }
    
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> {
        return mockAuthService.signInWithEmailAndPassword(email, password)
    }
    
    suspend fun signUpWithEmailAndPassword(email: String, password: String, username: String): Result<User> {
        return mockAuthService.signUpWithEmailAndPassword(email, password, username)
    }
    
    suspend fun signOut(): Result<Unit> {
        return mockAuthService.signOut()
    }
    
    fun getSessionToken(): String? {
        return mockAuthService.getSessionToken()
    }
    
    suspend fun refreshSession(): Result<String> {
        return mockAuthService.refreshSession()
    }
    
    suspend fun updateProfile(updates: Map<String, Any>): Result<User> {
        return mockAuthService.updateProfile(updates)
    }
}