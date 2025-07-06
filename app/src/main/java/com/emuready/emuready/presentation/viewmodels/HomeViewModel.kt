package com.emuready.emuready.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.domain.usecases.AppStats
import com.emuready.emuready.domain.usecases.GetFeaturedGamesUseCase
import com.emuready.emuready.domain.usecases.GetRecentGamesUseCase
import com.emuready.emuready.domain.usecases.GetStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val featuredGames: List<Game> = emptyList(),
    val recentGames: List<Game> = emptyList(),
    val stats: AppStats? = null,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFeaturedGamesUseCase: GetFeaturedGamesUseCase,
    private val getRecentGamesUseCase: GetRecentGamesUseCase,
    private val getStatsUseCase: GetStatsUseCase
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
            
            // Load stats from API
            getStatsUseCase().fold(
                onSuccess = { stats ->
                    _uiState.value = _uiState.value.copy(stats = stats)
                },
                onFailure = { /* Ignore stats error for now */ }
            )
            
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