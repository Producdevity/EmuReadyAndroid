package com.emuready.emuready.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcRequestBuilder
import com.emuready.emuready.data.remote.dto.GetListingsSchema
import com.emuready.emuready.data.remote.dto.MobileListing
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.presentation.viewmodels.SortOption

class GamesPagingSource(
    private val trpcApiService: EmuReadyTrpcApiService,
    private val requestBuilder: TrpcRequestBuilder,
    private val search: String? = null,
    private val sortBy: SortOption = SortOption.POPULARITY,
    private val systemIds: Set<String> = emptySet(),
    private val deviceIds: Set<String> = emptySet(),
    private val emulatorIds: Set<String> = emptySet(),
    private val performanceIds: Set<String> = emptySet()
) : PagingSource<Int, Game>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        return try {
            val currentKey = params.key ?: 1
            val pageSize = params.loadSize
            
            // Build the request parameters according to GetListingsSchema
            val requestData = GetListingsSchema(
                limit = pageSize,
                page = currentKey,
                search = search,
                systemId = systemIds.firstOrNull(), // GetListingsSchema only supports single systemId
                deviceId = deviceIds.firstOrNull(), // GetListingsSchema only supports single deviceId
                emulatorId = emulatorIds.firstOrNull() // GetListingsSchema only supports single emulatorId
            )
            
            // Encode the request as a query parameter
            val inputParam = requestBuilder.buildQueryParam(requestData)
            val responseList = trpcApiService.getListings(input = inputParam)
            val response = responseList.firstOrNull()
            
            if (response?.error != null) {
                LoadResult.Error(Exception(response.error.message))
            } else if (response?.result?.data?.json != null) {
                val listings = response.result.data.json
                
                // Extract unique games from listings and calculate their compatibility
                val gameMap = mutableMapOf<String, MutableList<MobileListing>>()
                listings.forEach { listing ->
                    val gameId = listing.game.id
                    gameMap.getOrPut(gameId) { mutableListOf() }.add(listing)
                }
                
                val games = gameMap.map { (gameId, gameListings) ->
                    val firstListing = gameListings.first()
                    val game = firstListing.game.toDomain()
                    
                    // Calculate average compatibility from all listings for this game
                    val avgCompatibility = gameListings.map { it.performance.rank }.average().toFloat() / 5.0f
                    
                    game.copy(
                        averageCompatibility = avgCompatibility.coerceIn(0.0f, 1.0f),
                        totalListings = gameListings.size
                    )
                }
                
                LoadResult.Page(
                    data = games,
                    prevKey = if (currentKey == 1) null else currentKey - 1,
                    nextKey = if (games.isEmpty()) null else currentKey + 1
                )
            } else {
                LoadResult.Error(Exception("No data received"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}