package com.emuready.emuready.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.emuready.emuready.data.local.dao.GameDao
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.mappers.toEntity
import com.emuready.emuready.data.remote.api.GameApiService
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.domain.entities.GameDetail
import com.emuready.emuready.domain.entities.GameSortOption
import com.emuready.emuready.domain.exceptions.ApiException
import com.emuready.emuready.domain.exceptions.NetworkException
import com.emuready.emuready.domain.repositories.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl @Inject constructor(
    private val gameApiService: GameApiService,
    private val gameDao: GameDao
) : GameRepository {
    
    override fun getGames(
        search: String?,
        genre: String?,
        sortBy: GameSortOption
    ): Flow<PagingData<Game>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { 
                GamePagingSource(gameApiService, gameDao, search, genre, sortBy.name.lowercase()) 
            }
        ).flow
    }
    
    override suspend fun getGameDetail(gameId: String): Result<GameDetail> = withContext(Dispatchers.IO) {
        try {
            val response = gameApiService.getGameDetail(gameId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(ApiException("Failed to fetch game details"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error", e))
        }
    }
    
    override fun getFavoriteGames(): Flow<List<Game>> {
        return gameDao.getFavoriteGames().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getRecentGames(limit: Int): Flow<List<Game>> {
        return gameDao.getRecentGames(limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getFeaturedGames(): Result<List<Game>> = withContext(Dispatchers.IO) {
        try {
            val response = gameApiService.getFeaturedGames()
            if (response.isSuccessful && response.body() != null) {
                val games = response.body()!!.map { it.toDomain() }
                // Cache featured games
                gameDao.insertGames(games.map { it.toEntity() })
                Result.success(games)
            } else {
                Result.failure(ApiException("Failed to fetch featured games"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error", e))
        }
    }
    
    override suspend fun getRecommendations(userId: String): Result<List<Game>> = withContext(Dispatchers.IO) {
        try {
            val response = gameApiService.getRecommendations(userId)
            if (response.isSuccessful && response.body() != null) {
                val games = response.body()!!.map { it.toDomain() }
                Result.success(games)
            } else {
                Result.failure(ApiException("Failed to fetch recommendations"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error", e))
        }
    }
    
    override suspend fun toggleFavorite(gameId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val game = gameDao.getGameById(gameId)
            if (game != null) {
                gameDao.updateFavoriteStatus(gameId, !game.isFavorite)
                Result.success(Unit)
            } else {
                Result.failure(ApiException("Game not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun syncGamesFromRemote(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = gameApiService.getGames(page = 1, limit = 50)
            if (response.isSuccessful && response.body() != null) {
                val games = response.body()!!.data.map { it.toDomain() }
                gameDao.insertGames(games.map { it.toEntity() })
                Result.success(Unit)
            } else {
                Result.failure(ApiException("Failed to sync games"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error", e))
        }
    }
}