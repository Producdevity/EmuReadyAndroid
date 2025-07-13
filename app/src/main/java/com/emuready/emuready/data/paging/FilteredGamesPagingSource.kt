package com.emuready.emuready.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcInputHelper
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.presentation.viewmodels.SortOption
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FilteredGamesPagingSource(
    private val trpcApiService: EmuReadyTrpcApiService,
    private val search: String?,
    private val sortBy: SortOption,
    private val systemIds: Set<String>,
    private val deviceIds: Set<String>,
    private val emulatorIds: Set<String>,
    private val performanceIds: Set<String>
) : PagingSource<Int, Game>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        return try {
            val offset = params.key ?: 0
            val pageSize = params.loadSize
            
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
            val responseWrapper = trpcApiService.getGames(input = input)
            val response = responseWrapper
            
            if (response.error != null) {
                LoadResult.Error(Exception(response.error.message))
            } else if (response.result?.data?.json != null) {
                // Games endpoint returns games directly
                val games = response.result.data.json.map { it.toDomain() }
                
                LoadResult.Page(
                    data = games,
                    prevKey = if (offset == 0) null else offset - pageSize,
                    nextKey = if (games.size < pageSize) null else offset + pageSize
                )
            } else {
                LoadResult.Error(Exception("No data received"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1) ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
