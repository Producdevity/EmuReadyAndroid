package com.emuready.emuready.data.services

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.emuready.emuready.domain.entities.User
import com.emuready.emuready.domain.exceptions.AuthException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock authentication service that simulates Clerk functionality
 * This will be replaced with actual Clerk integration when the SDK is available
 */
@Singleton
class MockAuthService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: DataStore<Preferences>
) {
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()
    
    private val USER_ID_KEY = stringPreferencesKey("mock_user_id")
    private val USERNAME_KEY = stringPreferencesKey("mock_username")
    private val EMAIL_KEY = stringPreferencesKey("mock_email")
    private val AUTH_TOKEN_KEY = stringPreferencesKey("mock_auth_token")
    
    init {
        // Load stored user on initialization
        loadStoredUser()
    }
    
    private fun loadStoredUser() {
        // This would typically be done with a coroutine, but for simplicity we'll check synchronously in init
    }
    
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            // Mock authentication - in real implementation, this would verify with backend
            if (email.isBlank() || password.isBlank()) {
                return@withContext Result.failure(AuthException("Email and password are required"))
            }
            
            if (password.length < 6) {
                return@withContext Result.failure(AuthException("Password must be at least 6 characters"))
            }
            
            // Create mock user
            val user = User(
                id = "mock_user_${email.hashCode()}",
                username = email.substringBefore("@"),
                email = email,
                avatarUrl = null,
                totalListings = 0,
                totalLikes = 0,
                memberSince = System.currentTimeMillis(),
                isVerified = false,
                lastActive = System.currentTimeMillis()
            )
            
            // Store user data
            storeUserData(user, "mock_token_${System.currentTimeMillis()}")
            
            _currentUser.value = user
            _isAuthenticated.value = true
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(AuthException("Sign in error: ${e.message}", e))
        }
    }
    
    suspend fun signUpWithEmailAndPassword(email: String, password: String, username: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            // Mock sign up validation
            if (email.isBlank() || password.isBlank() || username.isBlank()) {
                return@withContext Result.failure(AuthException("All fields are required"))
            }
            
            if (password.length < 6) {
                return@withContext Result.failure(AuthException("Password must be at least 6 characters"))
            }
            
            if (username.length < 3) {
                return@withContext Result.failure(AuthException("Username must be at least 3 characters"))
            }
            
            // Create mock user
            val user = User(
                id = "mock_user_${email.hashCode()}",
                username = username,
                email = email,
                avatarUrl = null,
                totalListings = 0,
                totalLikes = 0,
                memberSince = System.currentTimeMillis(),
                isVerified = false,
                lastActive = System.currentTimeMillis()
            )
            
            // Store user data
            storeUserData(user, "mock_token_${System.currentTimeMillis()}")
            
            _currentUser.value = user
            _isAuthenticated.value = true
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(AuthException("Sign up error: ${e.message}", e))
        }
    }
    
    suspend fun signOut(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            clearUserData()
            _currentUser.value = null
            _isAuthenticated.value = false
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(AuthException("Sign out error: ${e.message}", e))
        }
    }
    
    fun getSessionToken(): String? {
        // In a real implementation, this would return the actual session token
        return if (_isAuthenticated.value) "mock_token_${_currentUser.value?.id}" else null
    }
    
    suspend fun refreshSession(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val user = _currentUser.value
            if (user != null) {
                val newToken = "mock_token_${System.currentTimeMillis()}"
                storeUserData(user, newToken)
                Result.success(newToken)
            } else {
                Result.failure(AuthException("No active session to refresh"))
            }
        } catch (e: Exception) {
            Result.failure(AuthException("Session refresh error: ${e.message}", e))
        }
    }
    
    suspend fun updateProfile(updates: Map<String, Any>): Result<User> = withContext(Dispatchers.IO) {
        try {
            val currentUser = _currentUser.value
            if (currentUser != null) {
                // Update user with new data
                val updatedUser = currentUser.copy(
                    username = updates["username"] as? String ?: currentUser.username,
                    email = updates["email"] as? String ?: currentUser.email,
                    avatarUrl = updates["avatarUrl"] as? String ?: currentUser.avatarUrl
                )
                
                storeUserData(updatedUser, getSessionToken() ?: "")
                _currentUser.value = updatedUser
                
                Result.success(updatedUser)
            } else {
                Result.failure(AuthException("No authenticated user to update"))
            }
        } catch (e: Exception) {
            Result.failure(AuthException("Profile update error: ${e.message}", e))
        }
    }
    
    suspend fun loadStoredUserAsync(): User? = withContext(Dispatchers.IO) {
        try {
            val prefs = dataStore.data.first()
            val userId = prefs[USER_ID_KEY]
            val username = prefs[USERNAME_KEY]
            val email = prefs[EMAIL_KEY]
            val token = prefs[AUTH_TOKEN_KEY]
            
            if (userId != null && username != null && email != null && token != null) {
                val user = User(
                    id = userId,
                    username = username,
                    email = email,
                    avatarUrl = null,
                    totalListings = 0,
                    totalLikes = 0,
                    memberSince = System.currentTimeMillis(),
                    isVerified = false,
                    lastActive = System.currentTimeMillis()
                )
                
                _currentUser.value = user
                _isAuthenticated.value = true
                
                user
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    private suspend fun storeUserData(user: User, token: String) {
        dataStore.edit { prefs ->
            prefs[USER_ID_KEY] = user.id
            prefs[USERNAME_KEY] = user.username
            prefs[EMAIL_KEY] = user.email
            prefs[AUTH_TOKEN_KEY] = token
        }
    }
    
    private suspend fun clearUserData() {
        dataStore.edit { prefs ->
            prefs.remove(USER_ID_KEY)
            prefs.remove(USERNAME_KEY)
            prefs.remove(EMAIL_KEY)
            prefs.remove(AUTH_TOKEN_KEY)
        }
    }
}