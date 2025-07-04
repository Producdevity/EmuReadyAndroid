package com.emuready.emuready.domain.repositories

import com.emuready.emuready.domain.entities.AuthState
import com.emuready.emuready.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<AuthState>
    
    suspend fun signIn(email: String, password: String): Result<User>
    
    suspend fun signUp(username: String, email: String, password: String): Result<User>
    
    suspend fun signOut(): Result<Unit>
    
    suspend fun getCurrentUser(): Result<User?>
    
    suspend fun refreshToken(): Result<String>
    
    suspend fun isLoggedIn(): Boolean
    
    suspend fun getStoredToken(): String?
}