package com.emuready.emuready.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emuready.emuready.domain.entities.AuthState
import com.emuready.emuready.domain.entities.User
import com.emuready.emuready.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val isAuthenticated: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
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
    
    fun signOut() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            authRepository.signOut().fold(
                onSuccess = {
                    // Auth state will be updated via flow
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message,
                        isLoading = false
                    )
                }
            )
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}