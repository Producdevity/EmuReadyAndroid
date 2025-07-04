package com.emuready.emuready.data.repositories

import com.emuready.emuready.data.local.dao.GameListingDao
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.mappers.toEntity
import com.emuready.emuready.data.mappers.toRequest
import com.emuready.emuready.data.remote.api.ListingApiService
import com.emuready.emuready.domain.entities.CreateListingForm
import com.emuready.emuready.domain.entities.GameListing
import com.emuready.emuready.domain.exceptions.ApiException
import com.emuready.emuready.domain.exceptions.NetworkException
import com.emuready.emuready.domain.repositories.AuthRepository
import com.emuready.emuready.domain.repositories.ListingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListingRepositoryImpl @Inject constructor(
    private val listingApiService: ListingApiService,
    private val listingDao: GameListingDao,
    private val authRepository: AuthRepository
) : ListingRepository {
    
    override fun getListingsByGameId(gameId: String): Flow<List<GameListing>> {
        return listingDao.getListingsByGameId(gameId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getListingsByUserId(userId: String): Flow<List<GameListing>> {
        return listingDao.getListingsByUserId(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getListingsByDeviceId(deviceId: String): Flow<List<GameListing>> {
        return listingDao.getListingsByDeviceId(deviceId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun createListing(form: CreateListingForm): Result<GameListing> = withContext(Dispatchers.IO) {
        try {
            val response = listingApiService.createListing(form.toRequest())
            if (response.isSuccessful && response.body() != null) {
                val listing = response.body()!!.toDomain()
                listingDao.insertListing(listing.toEntity())
                Result.success(listing)
            } else {
                Result.failure(ApiException("Failed to create listing"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error", e))
        }
    }
    
    override suspend fun updateListing(listingId: String, form: CreateListingForm): Result<GameListing> = withContext(Dispatchers.IO) {
        try {
            val updateRequest = com.emuready.emuready.data.remote.dto.UpdateListingRequest(
                performanceRating = form.performanceRating,
                playabilityRating = form.playabilityRating,
                gpuDriver = form.gpuDriver,
                configurationPreset = form.configurationPreset,
                customSettings = form.customSettings,
                description = form.description,
                screenshotUrls = form.screenshots.map { it.toString() }
            )
            
            val response = listingApiService.updateListing(listingId, updateRequest)
            if (response.isSuccessful && response.body() != null) {
                val listing = response.body()!!.toDomain()
                listingDao.updateListing(listing.toEntity())
                Result.success(listing)
            } else {
                Result.failure(ApiException("Failed to update listing"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error", e))
        }
    }
    
    override suspend fun deleteListing(listingId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = listingApiService.deleteListing(listingId)
            if (response.isSuccessful) {
                listingDao.deleteListingById(listingId)
                Result.success(Unit)
            } else {
                Result.failure(ApiException("Failed to delete listing"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error", e))
        }
    }
    
    override suspend fun likeListing(listingId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = listingApiService.likeListing(listingId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(ApiException("Failed to like listing"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error", e))
        }
    }
    
    override suspend fun unlikeListing(listingId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = listingApiService.unlikeListing(listingId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(ApiException("Failed to unlike listing"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error", e))
        }
    }
    
    override suspend fun syncUserListings(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Get current user ID from authentication repository
            val userResult = authRepository.getCurrentUser()
            if (userResult.isFailure || userResult.getOrNull() == null) {
                return@withContext Result.failure(Exception("User not authenticated"))
            }
            val currentUser = userResult.getOrNull()!!
            val userId = currentUser.id
            val response = listingApiService.getUserListings(userId)
            if (response.isSuccessful && response.body() != null) {
                val listings = response.body()!!.map { it.toDomain() }
                listingDao.insertListings(listings.map { it.toEntity() })
                Result.success(Unit)
            } else {
                Result.failure(ApiException("Failed to sync listings"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error", e))
        }
    }
}