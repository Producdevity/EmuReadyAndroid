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
            val pageSize = minOf(params.loadSize, 50) // API max limit is 50
            
            // Use appropriate API endpoint based on sort option and search
            val response = when {
                sortBy == SortOption.POPULARITY && search.isNullOrBlank() -> {
                    val request = requestBuilder.buildQuery(com.emuready.emuready.data.remote.api.LimitRequest(limit = pageSize))
                    trpcApiService.getPopularGames(request)
                }
                !search.isNullOrBlank() -> {
                    val request = requestBuilder.buildQuery(com.emuready.emuready.data.remote.api.QueryRequest(search))
                    trpcApiService.searchGames(request)
                }
                else -> {
                    val request = requestBuilder.buildQuery(
                        GetGamesSchema(
                            search = search,
                            systemId = systemIds.firstOrNull(),
                            limit = pageSize
                        )
                    )
                    trpcApiService.getGames(request)
                }
            }
            
            if (response.error != null) {
                LoadResult.Error(Exception(response.error.message))
            } else if (response.result?.data?.json != null) {
                var games = response.result.data.json.map { it.toDomain() }
                
                // Apply client-side filtering since API doesn't support all filters
                if (deviceIds.isNotEmpty() || emulatorIds.isNotEmpty() || performanceIds.isNotEmpty()) {
                    // Note: These filters would require additional API calls to get listing data
                    // For now, we'll show all games and let the user filter on the detail page
                }
                
                // Apply client-side sorting for non-popularity sorts
                when (sortBy) {
                    SortOption.ALPHABETICAL -> games = games.sortedBy { it.title }
                    SortOption.RATING -> games = games.sortedByDescending { it.averageRating }
                    SortOption.LISTING_COUNT -> games = games.sortedByDescending { it.listingCount }
                    SortOption.POPULARITY -> { /* Already sorted by API */ }
                    SortOption.DATE_ADDED -> { /* Use default order */ }
                }
                
                // Simple pagination: return all results on first page, empty on subsequent pages
                val finalGames = if (page == 1) games else emptyList()
                
                LoadResult.Page(
                    data = finalGames,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (finalGames.isEmpty() || page > 1) null else page + 1
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