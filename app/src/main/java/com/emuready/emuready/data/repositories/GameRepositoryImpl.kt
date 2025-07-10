package com.emuready.emuready.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.emuready.emuready.data.local.dao.GameDao
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.mappers.toEntity
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcRequestBuilder
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
import com.emuready.emuready.data.remote.dto.TrpcResponseDtos
import com.emuready.emuready.data.paging.FilteredGamesPagingSource
import com.emuready.emuready.data.paging.GamesPagingSource
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.domain.entities.GameDetail
import com.emuready.emuready.domain.entities.Listing
import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.domain.entities.Emulator
import com.emuready.emuready.domain.entities.GameSortOption
import com.emuready.emuready.domain.entities.Cpu
import com.emuready.emuready.domain.entities.Gpu
import com.emuready.emuready.domain.exceptions.ApiException
import com.emuready.emuready.domain.exceptions.NetworkException
import com.emuready.emuready.domain.repositories.GameRepository
import com.emuready.emuready.presentation.viewmodels.SortOption
import com.emuready.emuready.presentation.viewmodels.FilterType
import com.emuready.emuready.presentation.ui.screens.FilterOption
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
        sortBy: SortOption,
        systemIds: Set<String>,
        deviceIds: Set<String>,
        emulatorIds: Set<String>,
        performanceIds: Set<String>
    ): Flow<PagingData<Game>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GamesPagingSource(
                    trpcApiService = trpcApiService,
                    requestBuilder = requestBuilder,
                    search = search,
                    sortBy = sortBy,
                    systemIds = systemIds,
                    deviceIds = deviceIds,
                    emulatorIds = emulatorIds,
                    performanceIds = performanceIds
                )
            }
        ).flow
    }
    
    override suspend fun getGameDetail(gameId: String): Result<GameDetail> = withContext(Dispatchers.IO) {
        try {
            // Get game details from API
            val queryParam = requestBuilder.buildQueryParam("""{"id":"$gameId"}""")
            val gameResponseWrapper = trpcApiService.getGameById(batch = 1, gameId = queryParam)
            val gameResponse = gameResponseWrapper.`0`
            
            if (gameResponse.error != null) {
                return@withContext Result.failure(ApiException(gameResponse.error.message))
            }
            
            val gameData = gameResponse.result?.data
                ?: return@withContext Result.failure(ApiException("Game not found"))
            
            // Get game listings - simplified for now
            val listings: List<Listing> = emptyList() // Will be enhanced when API integration is stable
            
            // Create GameDetail from fetched data
            val game = gameData.toDomain()
            val gameDetail = GameDetail(
                game = game,
                description = null,
                screenshots = emptyList<String>(),
                developer = null,
                publisher = null,
                releaseDate = null,
                genres = emptyList<String>(),
                recentListings = listings.take(5),
                topRatedListings = listings.sortedByDescending { it.overallRating }.take(5),
                compatibleDevices = emptyList<Device>(),
                recommendedEmulators = emptyList<Emulator>(),
                averageRating = game.averageCompatibility * 5f,
                ratingDistribution = emptyMap<Int, Int>(),
                tags = emptyList<String>(),
                isVerified = false,
                lastUpdated = game.lastUpdated
            )
            
            // Cache the game locally
            try {
                gameDao.insertGame(game.toEntity())
            } catch (e: Exception) {
                // Continue even if caching fails
            }
            
            Result.success(gameDetail)
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
            android.util.Log.d("GameRepository", "Calling getPopularGames API...")
            // For GET requests, pass empty query parameter for endpoints that don't need parameters
            val responseWrapper = trpcApiService.getPopularGames(input = null)
            val response = responseWrapper.`0`
            
            android.util.Log.d("GameRepository", "API response received. Error: ${response.error}, Result: ${response.result}")
            
            if (response.error != null) {
                android.util.Log.e("GameRepository", "API returned error: ${response.error.message}")
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data?.json != null) {
                val games = response.result.data.json.map { it.toDomain() }
                android.util.Log.d("GameRepository", "Successfully parsed ${games.size} games")
                // Cache featured games
                try {
                    gameDao.insertGames(games.map { it.toEntity() })
                } catch (e: Exception) {
                    // Continue even if caching fails
                }
                Result.success(games)
            } else {
                android.util.Log.e("GameRepository", "Invalid response format: ${response.result}")
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            android.util.Log.e("GameRepository", "Network error: ${e.message}", e)
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getRecommendations(userId: String): Result<List<Game>> = withContext(Dispatchers.IO) {
        try {
            // Use getPopularGames as there's no specific recommendations endpoint in the new API
            val responseWrapper = trpcApiService.getPopularGames(input = null)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data?.json != null) {
                val games = response.result.data.json.map { it.toDomain() }
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
            val queryParam = requestBuilder.buildQueryParam("""{"limit":100}""")
            val responseWrapper = trpcApiService.getGames(batch = 1, input = queryParam)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data?.json != null) {
                val games = response.result.data.json.map { it.toDomain() }
                gameDao.insertGames(games.map { it.toEntity() })
                Result.success(Unit)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getAvailableFilters(): Map<FilterType, List<FilterOption>> = withContext(Dispatchers.IO) {
        try {
            // Fetch all the filter options from the API
            val systemsRequest = requestBuilder.buildRequest(Unit)
            val devicesRequest = requestBuilder.buildRequest(TrpcRequestDtos.GetDevicesSchema(limit = 100))
            val emulatorsRequest = requestBuilder.buildRequest(TrpcRequestDtos.GetEmulatorsSchema(limit = 100))
            val performanceRequest = requestBuilder.buildRequest(Unit)
            
            val systemsResponseWrapper = trpcApiService.getSystems(systemsRequest)
            val devicesResponseWrapper = trpcApiService.getDevices(devicesRequest)
            val emulatorsResponseWrapper = trpcApiService.getEmulators(emulatorsRequest)
            val performanceResponseWrapper = trpcApiService.getPerformanceScales(performanceRequest)
            
            val systemsResponse = systemsResponseWrapper.`0`
            val devicesResponse = devicesResponseWrapper.`0`
            val emulatorsResponse = emulatorsResponseWrapper.`0`
            val performanceResponse = performanceResponseWrapper.`0`
            
            val filters = mutableMapOf<FilterType, List<FilterOption>>()
            
            // Systems
            systemsResponse.result?.data?.let { systems ->
                filters[FilterType.SYSTEM] = systems.map { system ->
                    FilterOption(
                        id = system.id,
                        name = system.name,
                        count = 0 // Systems don't have direct listing counts in the API
                    )
                }
            }
            
            // Devices
            devicesResponse.result?.data?.let { devices ->
                filters[FilterType.DEVICE] = devices.map { device ->
                    FilterOption(
                        id = device.id,
                        name = device.modelName,
                        count = device._count.listings
                    )
                }
            }
            
            // Emulators
            emulatorsResponse.result?.data?.let { emulators ->
                filters[FilterType.EMULATOR] = emulators.map { emulator ->
                    FilterOption(
                        id = emulator.id,
                        name = emulator.name,
                        count = emulator._count.listings
                    )
                }
            }
            
            // Performance
            performanceResponse.result?.data?.let { performance ->
                filters[FilterType.PERFORMANCE] = performance.map { perf ->
                    FilterOption(
                        id = perf.id.toString(),
                        name = perf.label,
                        count = 0 // Performance scales don't have direct listing counts in the API
                    )
                }
            }
            
            filters
        } catch (e: Exception) {
            // Return empty filters on error
            emptyMap()
        }
    }

    override suspend fun getCpus(search: String?, brandId: String?, limit: Int?): Result<List<Cpu>> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildRequest(TrpcRequestDtos.GetCpusSchema(search = search, brandId = brandId, limit = limit))
            val responseWrapper = trpcApiService.getCpusEnhanced(request)
            val response = responseWrapper.`0`

            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val cpus = response.result.data.items.map { it.toDomain() }
                Result.success(cpus)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }

    override suspend fun getGpus(search: String?, brandId: String?, limit: Int?): Result<List<Gpu>> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildRequest(TrpcRequestDtos.GetGpusSchema(search = search, brandId = brandId, limit = limit))
            val responseWrapper = trpcApiService.getGpusEnhanced(request)
            val response = responseWrapper.`0`

            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val gpus = response.result.data.items.map { it.toDomain() }
                Result.success(gpus)
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
    override suspend fun searchGames(query: String): Result<List<Game>> = withContext(Dispatchers.IO) {
        try {
            val queryParam = requestBuilder.buildQueryParam("""{"query":"$query"}""")
            val responseWrapper = trpcApiService.searchGames(batch = 1, query = queryParam)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data?.json != null) {
                val games = response.result.data.json.map { it.toDomain() }
                Result.success(games)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }

    override fun getGamesFromFilteredListings(
        search: String?,
        sortBy: SortOption,
        systemIds: Set<String>,
        deviceIds: Set<String>,
        emulatorIds: Set<String>,
        performanceIds: Set<String>
    ): Flow<PagingData<Game>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                FilteredGamesPagingSource(
                    trpcApiService = trpcApiService,
                    requestBuilder = requestBuilder,
                    search = search,
                    sortBy = sortBy,
                    systemIds = systemIds,
                    deviceIds = deviceIds,
                    emulatorIds = emulatorIds,
                    performanceIds = performanceIds
                )
            }
        ).flow
    }
}