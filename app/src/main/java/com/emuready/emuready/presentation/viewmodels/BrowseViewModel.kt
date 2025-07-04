package com.emuready.emuready.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.domain.usecases.GetGamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BrowseUiState(
    val isLoading: Boolean = false,
    val games: List<Game> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null
)

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val getGamesUseCase: GetGamesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(BrowseUiState())
    val uiState: StateFlow<BrowseUiState> = _uiState.asStateFlow()
    
    fun searchGames(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                searchQuery = query,
                error = null
            )
            
            try {
                getGamesUseCase(search = query).collect { pagingData ->
                    // For simplicity, we'll need to convert PagingData to List
                    // In a real app, you'd use LazyPagingItems
                    _uiState.value = _uiState.value.copy(
                        games = emptyList(), // Placeholder - would need proper PagingData handling
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}