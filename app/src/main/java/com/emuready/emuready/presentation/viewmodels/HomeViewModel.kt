package com.emuready.emuready.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.domain.usecases.GetFeaturedGamesUseCase
import com.emuready.emuready.domain.usecases.GetRecentGamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val featuredGames: List<Game> = emptyList(),
    val recentGames: List<Game> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFeaturedGamesUseCase: GetFeaturedGamesUseCase,
    private val getRecentGamesUseCase: GetRecentGamesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadData()
    }
    
    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Load recent games from local database
            getRecentGamesUseCase().collect { recentGames ->
                _uiState.value = _uiState.value.copy(recentGames = recentGames)
            }
            
            // Try to load featured games from API
            getFeaturedGamesUseCase().fold(
                onSuccess = { featuredGames ->
                    _uiState.value = _uiState.value.copy(
                        featuredGames = featuredGames,
                        isLoading = false
                    )
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
    
    fun retry() {
        loadData()
    }
}