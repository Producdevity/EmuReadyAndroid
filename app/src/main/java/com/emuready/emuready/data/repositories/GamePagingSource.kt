package com.emuready.emuready.data.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emuready.emuready.data.local.dao.GameDao
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.mappers.toEntity
import com.emuready.emuready.data.remote.api.GameApiService
import com.emuready.emuready.domain.entities.Game

class GamePagingSource(
    private val gameApiService: GameApiService,
    private val gameDao: GameDao,
    private val search: String?,
    private val genre: String?,
    private val sort: String
) : PagingSource<Int, Game>() {
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        return try {
            val page = params.key ?: 1
            val response = gameApiService.getGames(
                page = page,
                limit = params.loadSize,
                search = search,
                genre = genre,
                sort = sort
            )
            
            if (response.isSuccessful && response.body() != null) {
                val paginatedResponse = response.body()!!
                val games = paginatedResponse.data.map { it.toDomain() }
                
                // Cache games locally
                gameDao.insertGames(games.map { it.toEntity() })
                
                LoadResult.Page(
                    data = games,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (paginatedResponse.hasNext) page + 1 else null
                )
            } else {
                LoadResult.Error(Exception("Failed to load games"))
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