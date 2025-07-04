package com.emuready.emuready.domain.repositories

import androidx.paging.PagingData
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.domain.entities.GameDetail
import com.emuready.emuready.domain.entities.GameSortOption
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getGames(
        search: String? = null,
        genre: String? = null,
        sortBy: GameSortOption = GameSortOption.TITLE
    ): Flow<PagingData<Game>>
    
    suspend fun getGameDetail(gameId: String): Result<GameDetail>
    
    fun getFavoriteGames(): Flow<List<Game>>
    
    fun getRecentGames(limit: Int = 10): Flow<List<Game>>
    
    suspend fun getFeaturedGames(): Result<List<Game>>
    
    suspend fun getRecommendations(userId: String): Result<List<Game>>
    
    suspend fun toggleFavorite(gameId: String): Result<Unit>
    
    suspend fun syncGamesFromRemote(): Result<Unit>
}