package com.emuready.emuready.domain.repositories

import androidx.paging.PagingData
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.domain.entities.GameDetail
import com.emuready.emuready.domain.entities.GameSortOption
import com.emuready.emuready.domain.entities.Cpu
import com.emuready.emuready.domain.entities.Gpu
import com.emuready.emuready.presentation.viewmodels.SortOption
import com.emuready.emuready.presentation.viewmodels.FilterType
import com.emuready.emuready.presentation.ui.screens.FilterOption
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getGames(
        search: String? = null,
        sortBy: SortOption = SortOption.POPULARITY,
        systemIds: Set<String> = emptySet(),
        deviceIds: Set<String> = emptySet(),
        emulatorIds: Set<String> = emptySet(),
        performanceIds: Set<String> = emptySet()
    ): Flow<PagingData<Game>>
    
    suspend fun getGameDetail(gameId: String): Result<GameDetail>
    
    fun getFavoriteGames(): Flow<List<Game>>
    
    fun getRecentGames(limit: Int = 10): Flow<List<Game>>
    
    suspend fun getFeaturedGames(): Result<List<Game>>
    
    suspend fun getRecommendations(userId: String): Result<List<Game>>
    
    suspend fun toggleFavorite(gameId: String): Result<Unit>
    
    suspend fun syncGamesFromRemote(): Result<Unit>
    
    suspend fun getAvailableFilters(): Map<FilterType, List<FilterOption>>
    
    suspend fun searchGames(query: String): Result<List<Game>>

    suspend fun getCpus(search: String? = null, brandId: String? = null, limit: Int? = null): Result<List<Cpu>>

    suspend fun getGpus(search: String? = null, brandId: String? = null, limit: Int? = null): Result<List<Gpu>>

    fun getGamesFromFilteredListings(
        search: String? = null,
        sortBy: SortOption = SortOption.POPULARITY,
        systemIds: Set<String> = emptySet(),
        deviceIds: Set<String> = emptySet(),
        emulatorIds: Set<String> = emptySet(),
        performanceIds: Set<String> = emptySet()
    ): Flow<PagingData<Game>>
}