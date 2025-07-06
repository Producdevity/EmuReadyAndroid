package com.emuready.emuready.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.emuready.emuready.data.local.dao.GameDao
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.mappers.toEntity
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcRequestBuilder
import com.emuready.emuready.data.remote.dto.*
import com.emuready.emuready.data.remote.api.IdRequest
import com.emuready.emuready.data.remote.api.LimitRequest
import com.emuready.emuready.data.remote.api.QueryRequest
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
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl @Inject constructor(
    private val trpcApiService: EmuReadyTrpcApiService,
    private val gameDao: GameDao
) : GameRepository {
    
    private val requestBuilder = TrpcRequestBuilder()
    
    override fun getGames(
        search: String?,
        genre: String?,
        sortBy: GameSortOption
    ): Flow<PagingData<Game>> {
        // For now, return empty flow until paging source is implemented
        return kotlinx.coroutines.flow.flowOf(androidx.paging.PagingData.empty())
    }
    
    override suspend fun getGameDetail(gameId: String): Result<GameDetail> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildQuery(IdRequest(gameId))
            val response = trpcApiService.getGameById(request)
            
            val result = response.`0`
            if (result.error != null) {
                Result.failure(ApiException(result.error.message))
            } else if (result.result?.data != null) {
                val mobileGame = result.result.data
                // Convert MobileGame to GameDetail
                val gameDetail = GameDetail(
                    id = mobileGame.id,
                    title = mobileGame.title,
                    titleId = mobileGame.id,
                    coverImageUrl = mobileGame.imageUrl ?: "",
                    screenshotUrls = listOfNotNull(mobileGame.imageUrl),
                    description = "", // Not provided in mobile API
                    releaseDate = LocalDate.now(), // Not provided in mobile API
                    developer = "", // Not provided in mobile API
                    publisher = "", // Not provided in mobile API
                    genres = emptyList(), // Not provided in mobile API
                    compatibilityRatings = emptyMap(), // Would need to be calculated from listings
                    listings = emptyList(), // Would need separate API call
                    averageRating = 0.0f, // Not provided in mobile API
                    totalRatings = 0, // Not provided in mobile API
                    lastUpdated = Instant.now() // Using current time
                )
                Result.success(gameDetail)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
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
            val request = requestBuilder.buildQuery(LimitRequest(limit = 10))
            val response = trpcApiService.getPopularGames(request)
            
            val result = response.`0`
            if (result.error != null) {
                Result.failure(ApiException(result.error.message))
            } else if (result.result?.data != null) {
                val games = result.result.data.map { it.toDomain() }
                // Cache featured games
                gameDao.insertGames(games.map { it.toEntity() })
                Result.success(games)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getRecommendations(userId: String): Result<List<Game>> = withContext(Dispatchers.IO) {
        try {
            // Use getPopularGames as there's no specific recommendations endpoint in the new API
            val request = requestBuilder.buildQuery(LimitRequest(limit = 10))
            val response = trpcApiService.getPopularGames(request)
            
            val result = response.`0`
            if (result.error != null) {
                Result.failure(ApiException(result.error.message))
            } else if (result.result?.data != null) {
                val games = result.result.data.map { it.toDomain() }
                Result.success(games)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
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
            val request = requestBuilder.buildQuery(GetGamesSchema(limit = 50))
            val response = trpcApiService.getGames(request)
            
            val result = response.`0`
            if (result.error != null) {
                Result.failure(ApiException(result.error.message))
            } else if (result.result?.data != null) {
                val games = result.result.data.map { it.toDomain() }
                gameDao.insertGames(games.map { it.toEntity() })
                Result.success(Unit)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    /**
     * Search games using the new tRPC API
     */
    suspend fun searchGames(query: String): Result<List<Game>> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildQuery(QueryRequest(query))
            val response = trpcApiService.searchGames(request)
            
            val result = response.`0`
            if (result.error != null) {
                Result.failure(ApiException(result.error.message))
            } else if (result.result?.data != null) {
                val games = result.result.data.map { it.toDomain() }
                Result.success(games)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
}