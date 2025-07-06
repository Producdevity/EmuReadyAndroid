package com.emuready.emuready.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcRequestBuilder
import com.emuready.emuready.data.remote.dto.GetGamesSchema
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
            val page = params.key ?: 1
            val pageSize = params.loadSize
            
            // Map SortOption to API sort parameter
            val apiSort = when (sortBy) {
                SortOption.POPULARITY -> "popularity"
                SortOption.ALPHABETICAL -> "title"
                SortOption.RATING -> "rating"
                SortOption.DATE_ADDED -> "date"
                SortOption.LISTING_COUNT -> "listings"
            }
            
            val request = requestBuilder.buildQuery(
                GetGamesSchema(
                    search = search,
                    systemId = systemIds.firstOrNull(), // API might only support single system filter
                    limit = pageSize
                )
            )
            
            val response = trpcApiService.getGames(request)
            val result = response.`0`
            
            if (result.error != null) {
                LoadResult.Error(Exception(result.error.message))
            } else if (result.result?.data != null) {
                val games = result.result.data.map { it.toDomain() }
                
                LoadResult.Page(
                    data = games,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (games.isEmpty() || games.size < pageSize) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Invalid response format"))
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