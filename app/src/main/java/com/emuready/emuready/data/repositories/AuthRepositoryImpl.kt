package com.emuready.emuready.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.emuready.emuready.data.local.dao.UserDao
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.mappers.toEntity
import com.emuready.emuready.data.remote.api.AuthApiService
import com.emuready.emuready.data.remote.dto.LoginRequest
import com.emuready.emuready.data.remote.dto.RegisterRequest
import com.emuready.emuready.domain.entities.AuthState
import com.emuready.emuready.domain.entities.User
import com.emuready.emuready.domain.exceptions.ApiException
import com.emuready.emuready.domain.exceptions.AuthException
import com.emuready.emuready.domain.exceptions.NetworkException
import com.emuready.emuready.domain.repositories.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authApiService: AuthApiService,
    private val userDao: UserDao,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {
    
    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    private val USER_ID_KEY = stringPreferencesKey("user_id")
    
    override val authState: Flow<AuthState> = flow {
        try {
            val token = getStoredToken()
            if (token != null) {
                val user = getCurrentUser().getOrNull()
                if (user != null) {
                    emit(AuthState.Authenticated(user))
                } else {
                    emit(AuthState.Unauthenticated)
                }
            } else {
                emit(AuthState.Unauthenticated)
            }
        } catch (e: Exception) {
            emit(AuthState.Unauthenticated)
        }
    }
    
    override suspend fun signIn(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = authApiService.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                val user = authResponse.user.toDomain()
                
                // Store tokens and user
                storeAuthData(authResponse.token, authResponse.refreshToken, user.id)
                userDao.insertUser(user.toEntity())
                
                Result.success(user)
            } else {
                Result.failure(AuthException("Invalid credentials"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error", e))
        }
    }
    
    override suspend fun signUp(username: String, email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = authApiService.register(RegisterRequest(username, email, password))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                val user = authResponse.user.toDomain()
                
                // Store tokens and user
                storeAuthData(authResponse.token, authResponse.refreshToken, user.id)
                userDao.insertUser(user.toEntity())
                
                Result.success(user)
            } else {
                Result.failure(AuthException("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error", e))
        }
    }
    
    override suspend fun signOut(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val token = getStoredToken()
            if (token != null) {
                authApiService.logout("Bearer $token")
            }
            
            // Clear local data
            clearAuthData()
            userDao.deleteAllUsers()
            
            Result.success(Unit)
        } catch (e: Exception) {
            // Still clear local data even if API call fails
            clearAuthData()
            userDao.deleteAllUsers()
            Result.success(Unit)
        }
    }
    
    override suspend fun getCurrentUser(): Result<User?> = withContext(Dispatchers.IO) {
        try {
            val userId = getStoredUserId()
            if (userId != null) {
                val userEntity = userDao.getUserById(userId)
                if (userEntity != null) {
                    Result.success(userEntity.toDomain())
                } else {
                    // Try to fetch from API
                    val token = getStoredToken()
                    if (token != null) {
                        val response = authApiService.getProfile("Bearer $token")
                        if (response.isSuccessful && response.body() != null) {
                            val user = response.body()!!.toDomain()
                            userDao.insertUser(user.toEntity())
                            Result.success(user)
                        } else {
                            Result.success(null)
                        }
                    } else {
                        Result.success(null)
                    }
                }
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun refreshToken(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val refreshToken = getStoredRefreshToken()
            if (refreshToken != null) {
                val response = authApiService.refreshToken("Bearer $refreshToken")
                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    storeAuthData(authResponse.token, authResponse.refreshToken, authResponse.user.id)
                    Result.success(authResponse.token)
                } else {
                    Result.failure(AuthException("Failed to refresh token"))
                }
            } else {
                Result.failure(AuthException("No refresh token available"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error", e))
        }
    }
    
    override suspend fun isLoggedIn(): Boolean {
        return getStoredToken() != null
    }
    
    private suspend fun storeAuthData(token: String, refreshToken: String, userId: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[USER_ID_KEY] = userId
        }
    }
    
    private suspend fun clearAuthData() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
            preferences.remove(USER_ID_KEY)
        }
    }
    
    override suspend fun getStoredToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.first()
    }
    
    private suspend fun getStoredRefreshToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY]
        }.first()
    }
    
    private suspend fun getStoredUserId(): String? {
        return dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }.first()
    }
}