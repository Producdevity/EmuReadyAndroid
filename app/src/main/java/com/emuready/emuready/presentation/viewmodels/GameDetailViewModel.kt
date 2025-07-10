package com.emuready.emuready.presentation.viewmodels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.domain.entities.GameDetail
import com.emuready.emuready.domain.entities.Listing
import com.emuready.emuready.domain.entities.PcListing
import com.emuready.emuready.domain.usecases.GetGameDetailUseCase
import com.emuready.emuready.domain.usecases.GetMobileListingsForGameUseCase
import com.emuready.emuready.domain.usecases.GetPcListingsForGameUseCase
import com.emuready.emuready.domain.usecases.ToggleFavoriteGameUseCase
import com.emuready.emuready.data.services.EdenLaunchService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GameDetailUiState(
    val isLoading: Boolean = false,
    val game: Game? = null,
    val gameDetail: GameDetail? = null,
    val mobileListings: Flow<PagingData<Listing>>? = null,
    val pcListings: Flow<PagingData<PcListing>>? = null,
    val isFavorite: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class GameDetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getGameDetailUseCase: GetGameDetailUseCase,
    private val toggleFavoriteGameUseCase: ToggleFavoriteGameUseCase,
    private val getMobileListingsForGameUseCase: GetMobileListingsForGameUseCase,
    private val getPcListingsForGameUseCase: GetPcListingsForGameUseCase,
    private val edenLaunchService: EdenLaunchService
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
                    _uiState.value = _uiState.value.copy(
                        game = gameDetail.game,
                        gameDetail = gameDetail,
                        mobileListings = getMobileListingsForGameUseCase(gameId).cachedIn(viewModelScope),
                        pcListings = getPcListingsForGameUseCase(gameId).cachedIn(viewModelScope),
                        isFavorite = gameDetail.game.isFavorite,
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
    
    fun toggleFavorite() {
        val currentGame = _uiState.value.game ?: return
        viewModelScope.launch {
            toggleFavoriteGameUseCase(currentGame.id).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isFavorite = !_uiState.value.isFavorite
                    )
                },
                onFailure = { error ->
                    // Show error briefly but don't update UI state
                    // Could add snackbar notification here
                }
            )
        }
    }
    
    fun launchInEden() {
        val currentGame = _uiState.value.game ?: return
        viewModelScope.launch {
            _uiState.value.mobileListings?.firstOrNull()?.let { pagingData ->
                // val listings = pagingData.asSnapshot() // TODO: Fix PagingData snapshot
                val listings = emptyList<com.emuready.emuready.domain.entities.Listing>()
                val bestListing = listings
                    .filter { it.type == com.emuready.emuready.domain.entities.ListingType.MOBILE_DEVICE }
                    .maxByOrNull { it.overallRating }

                if (bestListing != null) {
                    // Placeholder for actual emulator config construction
                    val dummyEmulatorConfig = "[Renderer]\nuse_vsync=0\n"
                    edenLaunchService.launchGame(currentGame.id, dummyEmulatorConfig).fold(
                        onSuccess = { /* Launched successfully */ },
                        onFailure = { error ->
                            _uiState.value = _uiState.value.copy(
                                error = error.message ?: "Failed to launch Eden emulator."
                            )
                        }
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "No compatible mobile listing found for this game."
                    )
                }
            } ?: run {
                _uiState.value = _uiState.value.copy(
                    error = "No mobile listings available to launch."
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}