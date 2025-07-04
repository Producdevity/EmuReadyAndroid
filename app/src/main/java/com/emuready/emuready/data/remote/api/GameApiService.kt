package com.emuready.emuready.data.remote.api

import com.emuready.emuready.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface GameApiService {
    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("search") search: String? = null,
        @Query("genre") genre: String? = null,
        @Query("sort") sort: String = "popularity"
    ): Response<PaginatedResponseDto<GameDto>>
    
    @GET("games/{id}")
    suspend fun getGameDetail(@Path("id") gameId: String): Response<GameDetailDto>
    
    @GET("games/{id}/listings")
    suspend fun getGameListings(
        @Path("id") gameId: String,
        @Query("device") deviceFilter: String? = null
    ): Response<List<GameListingDto>>
    
    @GET("games/featured")
    suspend fun getFeaturedGames(): Response<List<GameDto>>
    
    @GET("games/recommendations")
    suspend fun getRecommendations(@Query("userId") userId: String): Response<List<GameDto>>
}