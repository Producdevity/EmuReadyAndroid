package com.emuready.emuready.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcRequestBuilder
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.presentation.viewmodels.SortOption
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FilteredGamesPagingSource(
    private val trpcApiService: EmuReadyTrpcApiService,
    private val requestBuilder: TrpcRequestBuilder,
    private val search: String?,
    private val sortBy: SortOption,
    private val systemIds: Set<String>,
    private val deviceIds: Set<String>,
    private val emulatorIds: Set<String>,
    private val performanceIds: Set<String>
) : PagingSource<Int, Game>() {

    private inline fun <reified T> createQueryParam(data: T): String {
        return requestBuilder.buildQueryParam(Json.encodeToString(data))
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        return try {
            val offset = params.key ?: 0
            val pageSize = params.loadSize
            
            // Build the request parameters for games.get endpoint
            val requestData = TrpcRequestDtos.GetGamesSchema(
                offset = offset,
                limit = pageSize,
                search = search,
                systemId = systemIds.firstOrNull(),
                hideGamesWithNoListings = false
            )
            
            // Make tRPC request
            val queryParam = createQueryParam(requestData)
            val responseWrapper = trpcApiService.getGames(batch = 1, input = queryParam)
            val response = responseWrapper.`0`
            
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
