package com.emuready.emuready.domain.repositories

import com.emuready.emuready.domain.entities.GameListing
import com.emuready.emuready.domain.entities.CreateListingForm
import kotlinx.coroutines.flow.Flow

interface ListingRepository {
    fun getListingsByGameId(gameId: String): Flow<List<GameListing>>
    
    fun getListingsByUserId(userId: String): Flow<List<GameListing>>
    
    fun getListingsByDeviceId(deviceId: String): Flow<List<GameListing>>
    
    suspend fun createListing(form: CreateListingForm): Result<GameListing>
    
    suspend fun updateListing(listingId: String, form: CreateListingForm): Result<GameListing>
    
    suspend fun deleteListing(listingId: String): Result<Unit>
    
    suspend fun likeListing(listingId: String): Result<Unit>
    
    suspend fun unlikeListing(listingId: String): Result<Unit>
    
    suspend fun syncUserListings(): Result<Unit>
}