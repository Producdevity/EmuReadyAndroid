package com.emuready.emuready.data.remote.api

import com.emuready.emuready.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ListingApiService {
    @POST("listings")
    suspend fun createListing(@Body listing: CreateListingRequest): Response<GameListingDto>
    
    @PUT("listings/{id}")
    suspend fun updateListing(
        @Path("id") listingId: String,
        @Body listing: UpdateListingRequest
    ): Response<GameListingDto>
    
    @DELETE("listings/{id}")
    suspend fun deleteListing(@Path("id") listingId: String): Response<Unit>
    
    @POST("listings/{id}/like")
    suspend fun likeListing(@Path("id") listingId: String): Response<Unit>
    
    @DELETE("listings/{id}/like")
    suspend fun unlikeListing(@Path("id") listingId: String): Response<Unit>
    
    @GET("listings/user/{userId}")
    suspend fun getUserListings(@Path("userId") userId: String): Response<List<GameListingDto>>
}