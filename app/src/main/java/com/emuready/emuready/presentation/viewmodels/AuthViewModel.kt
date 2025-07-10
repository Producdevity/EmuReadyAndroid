package com.emuready.emuready.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emuready.emuready.domain.entities.AuthState
import com.emuready.emuready.domain.entities.User
import com.emuready.emuready.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    data class AuthUiState(
        val isLoading: Boolean = false,
        val isAuthenticated: Boolean = false,
        val user: User? = null,
        val error: String? = null
    )
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    init {
        observeAuthState()
    }
    
    private fun observeAuthState() {
        authRepository.authState
            .onEach { authState ->
                when (authState) {
                    is AuthState.Authenticated -> {
                        _uiState.value = _uiState.value.copy(
                            isAuthenticated = true,
                            user = authState.user,
                            isLoading = false,
                            error = null
                        )
                    }
                    is AuthState.Unauthenticated -> {
                        _uiState.value = _uiState.value.copy(
                            isAuthenticated = false,
                            user = null,
                            isLoading = false
                        )
                    }
                    is AuthState.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }
    
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            authRepository.signIn(email, password).fold(
                onSuccess = { user ->
                    // Success is handled by authState flow
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Sign in failed"
                    )
                }
            )
        }
    }
    
    fun signUp(username: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            authRepository.signUp(username, email, password).fold(
                onSuccess = { user ->
                    // Success is handled by authState flow
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Sign up failed"
                    )
                }
            )
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            authRepository.signOut().fold(
                onSuccess = {
                    // Success is handled by authState flow
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Sign out failed"
                    )
                }
            )
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    // updateProfile function will be added when repository interface is extended
    
    fun refreshToken() {
        viewModelScope.launch {
            authRepository.refreshToken().fold(
                onSuccess = { token ->
                    // Token refreshed successfully
                },
                onFailure = { error ->
                    // Handle refresh failure - might need to re-authenticate
                    _uiState.value = _uiState.value.copy(
                        error = "Session expired. Please sign in again."
                    )
                }
            )
        }
    }
}