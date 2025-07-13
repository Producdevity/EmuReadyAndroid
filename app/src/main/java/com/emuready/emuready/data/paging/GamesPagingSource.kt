package com.emuready.emuready.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcInputHelper
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
import com.emuready.emuready.data.remote.dto.TrpcResponseDtos
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.presentation.viewmodels.SortOption
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import android.util.Log

class GamesPagingSource(
    private val trpcApiService: EmuReadyTrpcApiService,
    private val search: String? = null,
    private val sortBy: SortOption = SortOption.DEFAULT,
    private val systemIds: Set<String> = emptySet(),
    private val deviceIds: Set<String> = emptySet(),
    private val emulatorIds: Set<String> = emptySet(),
    private val performanceIds: Set<String> = emptySet()
) : PagingSource<Int, Game>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        return try {
            val offset = params.key ?: 0
            val pageSize = params.loadSize
            
            Log.d("GamesPagingSource", "Loading offset=$offset, pageSize=$pageSize, search=$search")
            
            // Build the request parameters for games.get endpoint
            // API limits: limit max 50, search/systemId can be null (omitted from JSON)
            val requestData = TrpcRequestDtos.GetGamesSchema(
                offset = offset,
                limit = minOf(pageSize, 50), // API max limit is 50
                search = search?.takeIf { it.isNotBlank() },
                systemId = systemIds.firstOrNull()?.takeIf { it.isNotBlank() },
                hideGamesWithNoListings = false
            )
            
            // Make tRPC request
            val input = TrpcInputHelper.createInput(requestData)
            Log.d("GamesPagingSource", "Input created: $input")
            
            val responseWrapper = trpcApiService.getGames(input = input)
            Log.d("GamesPagingSource", "Response received: $responseWrapper")
            
            if (responseWrapper.error != null) {
                Log.e("GamesPagingSource", "Error: ${responseWrapper.error}")
                LoadResult.Error(Exception(responseWrapper.error.message))
            } else if (responseWrapper.result?.data != null) {
                // Games endpoint returns TrpcPaginatedGamesResponse
                val gamesData = responseWrapper.result.data
                Log.d("GamesPagingSource", "Games data: $gamesData")
                
                val games = gamesData.json.map { it.toDomain() }
                Log.d("GamesPagingSource", "Mapped ${games.size} games")
                
                LoadResult.Page(
                    data = games,
                    prevKey = if (offset == 0) null else offset - pageSize,
                    nextKey = if (games.size < pageSize) null else offset + pageSize
                )
            } else {
                Log.e("GamesPagingSource", "No data received - result: ${responseWrapper.result}")
                LoadResult.Error(Exception("No data received"))
            }
        } catch (e: Exception) {
            Log.e("GamesPagingSource", "Exception: ${e.message}", e)
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