package com.emuready.emuready.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.domain.usecases.GetGameDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GameDetailUiState(
    val isLoading: Boolean = false,
    val game: Game? = null,
    val error: String? = null
)

@HiltViewModel
class GameDetailViewModel @Inject constructor(
    private val getGameDetailUseCase: GetGameDetailUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(GameDetailUiState())
    val uiState: StateFlow<GameDetailUiState> = _uiState.asStateFlow()
    
    fun loadGameDetails(gameId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            getGameDetailUseCase(gameId).fold(
                onSuccess = { gameDetail ->
                    // Convert GameDetail to Game for UI state
                    val game = Game(
                        id = gameDetail.id,
                        title = gameDetail.title,
                        titleId = gameDetail.titleId,
                        coverImageUrl = gameDetail.coverImageUrl,
                        developer = gameDetail.developer,
                        publisher = gameDetail.publisher,
                        releaseDate = gameDetail.releaseDate,
                        genres = gameDetail.genres,
                        averageCompatibility = gameDetail.averageRating,
                        totalListings = gameDetail.listings.size,
                        lastUpdated = gameDetail.lastUpdated.toEpochMilli(),
                        isFavorite = false // Default value, would be fetched from user preferences
                    )
                    _uiState.value = _uiState.value.copy(
                        game = game,
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
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}