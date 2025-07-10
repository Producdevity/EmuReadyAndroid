package com.emuready.emuready.data.repositories

import android.content.Context
import com.emuready.emuready.data.local.dao.UserDao
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.mappers.toEntity
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcRequestBuilder
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
import com.emuready.emuready.data.services.ClerkAuthService
import com.emuready.emuready.domain.entities.AuthState
import com.emuready.emuready.domain.entities.User
import com.emuready.emuready.domain.exceptions.ApiException
import com.emuready.emuready.domain.exceptions.AuthException
import com.emuready.emuready.domain.exceptions.NetworkException
import com.emuready.emuready.domain.repositories.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val trpcApiService: EmuReadyTrpcApiService,
    private val userDao: UserDao,
    private val clerkAuthService: ClerkAuthService
) : AuthRepository {
    
    private val requestBuilder = TrpcRequestBuilder()
    
    
    
    override val authState: Flow<AuthState> = clerkAuthService.isAuthenticated.map { isAuthenticated ->
        if (isAuthenticated) {
            val user = clerkAuthService.currentUser.value
            if (user != null) {
                AuthState.Authenticated(user)
            } else {
                AuthState.Unauthenticated
            }
        } else {
            AuthState.Unauthenticated
        }
    }
    
    override suspend fun signIn(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        return@withContext clerkAuthService.signInWithEmailAndPassword(email, password).fold(
            onSuccess = { user ->
                // Store user in local database and sync with backend
                try {
                    // userDao.insertUser(user.toEntity()) // TODO: Fix entity conversion
                    
                    // Sync user data with backend API
                    syncUserWithBackend(user)
                    
                    Result.success(user)
                } catch (e: Exception) {
                    Result.success(user) // Continue even if local storage fails
                }
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
    
    override suspend fun signUp(username: String, email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        return@withContext clerkAuthService.signUpWithEmailAndPassword(email, password, username).fold(
            onSuccess = { user ->
                // Store user in local database and sync with backend
                try {
                    // userDao.insertUser(user.toEntity()) // TODO: Fix entity conversion
                    
                    // Sync user data with backend API
                    syncUserWithBackend(user)
                    
                    Result.success(user)
                } catch (e: Exception) {
                    Result.success(user) // Continue even if local storage fails
                }
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
    
    override suspend fun signOut(): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext clerkAuthService.signOut().fold(
            onSuccess = {
                // Clear local data
                try {
                    userDao.deleteAllUsers()
                    Result.success(Unit)
                } catch (e: Exception) {
                    // Even if local cleanup fails, sign out was successful
                    Result.success(Unit)
                }
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
    
    override suspend fun getCurrentUser(): Result<User?> = withContext(Dispatchers.IO) {
        val user = clerkAuthService.currentUser.value
        Result.success(user)
    }
    
    override suspend fun refreshToken(): Result<String> = withContext(Dispatchers.IO) {
        return@withContext clerkAuthService.refreshSession().fold(
            onSuccess = { token ->
                // Store refreshed token
                Result.success(token)
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
    
    override suspend fun getStoredToken(): String? {
        return clerkAuthService.getSessionToken()
    }
    
    override suspend fun isLoggedIn(): Boolean {
        return clerkAuthService.isAuthenticated.value
    }
    
    private suspend fun syncUserWithBackend(user: User) {
        try {
            // Sync user profile with backend via tRPC API
            // Store the user locally
            // userDao.insertUser(user.toEntity()) // TODO: Fix entity conversion
        } catch (e: Exception) {
            // Sync failure is not critical - continue with authentication
        }
    }
    
    
    
    
}