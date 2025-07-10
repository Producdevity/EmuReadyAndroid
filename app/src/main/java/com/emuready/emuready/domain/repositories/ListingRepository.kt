package com.emuready.emuready.domain.repositories

import androidx.paging.PagingData
import com.emuready.emuready.domain.entities.*
import kotlinx.coroutines.flow.Flow

interface ListingRepository {
    // Basic listing retrieval
    suspend fun getListingsByGameId(gameId: String): Result<List<Listing>>
    
    suspend fun getListingsByUserId(userId: String): Result<List<Listing>>
    
    suspend fun getListingById(listingId: String): Result<Listing>
    
    suspend fun getFeaturedListings(): Result<List<Listing>>
    
    // Paginated listing retrieval
    fun getListings(
        search: String? = null,
        gameId: String? = null,
        systemId: String? = null,
        deviceId: String? = null,
        emulatorId: String? = null
    ): Flow<PagingData<Listing>>
    
    // Listing management
    suspend fun createListing(form: CreateListingForm): Result<Listing>
    suspend fun createPcListing(form: CreateListingForm): Result<PcListing>
    
    suspend fun updateListing(listingId: String, form: CreateListingForm): Result<Listing>
    
    suspend fun deleteListing(listingId: String): Result<Unit>
    
    // Voting system
    suspend fun voteListing(listingId: String, isUpvote: Boolean): Result<Unit>
    
    suspend fun getUserVote(listingId: String): Result<Boolean?>
    
    // Comments
    suspend fun getListingComments(listingId: String): Result<List<Comment>>
    
    suspend fun createComment(listingId: String, content: String): Result<Comment>

    fun getMobileListingsForGame(gameId: String): Flow<PagingData<Listing>>

    fun getPcListingsForGame(gameId: String): Flow<PagingData<PcListing>>
}