package com.emuready.emuready.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.paging.ListingsPagingSource
import com.emuready.emuready.data.paging.MobileListingsPagingSource
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcInputHelper
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
import com.emuready.emuready.domain.entities.*
import com.emuready.emuready.domain.exceptions.ApiException
import com.emuready.emuready.domain.exceptions.NetworkException
import com.emuready.emuready.domain.repositories.AuthRepository
import com.emuready.emuready.domain.repositories.ListingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import androidx.paging.PagingSource
import androidx.paging.PagingState

// Temporary empty paging sources until proper implementation
class EmptyPagingSource : PagingSource<Int, Listing>() {
    override fun getRefreshKey(state: PagingState<Int, Listing>): Int? = null
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Listing> = LoadResult.Page(
        data = emptyList(),
        prevKey = null,
        nextKey = null
    )
}

class EmptyPcPagingSource : PagingSource<Int, PcListing>() {
    override fun getRefreshKey(state: PagingState<Int, PcListing>): Int? = null
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PcListing> = LoadResult.Page(
        data = emptyList(),
        prevKey = null,
        nextKey = null
    )
}

@Singleton
class ListingRepositoryImpl @Inject constructor(
    private val trpcApiService: EmuReadyTrpcApiService,
    private val authRepository: AuthRepository
) : ListingRepository {
    
    override suspend fun getListingsByGameId(gameId: String): Result<List<Listing>> = withContext(Dispatchers.IO) {
        try {
            val input = TrpcInputHelper.createInput(TrpcRequestDtos.GameIdRequest(gameId = gameId))
            val responseWrapper = trpcApiService.getListingsByGame(input = input)
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val listings = response.result.data.map { it.toDomain() }
                Result.success(listings)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getListingsByUserId(userId: String): Result<List<Listing>> = withContext(Dispatchers.IO) {
        try {
            val input = TrpcInputHelper.createInput(TrpcRequestDtos.UserIdRequest(userId = userId))
            val responseWrapper = trpcApiService.getUserListings(input = input)
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val listings = response.result.data.map { it.toDomain() }
                Result.success(listings)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getListingById(listingId: String): Result<Listing> = withContext(Dispatchers.IO) {
        try {
            val input = TrpcInputHelper.createInput(TrpcRequestDtos.IdRequest(id = listingId))
            val responseWrapper = trpcApiService.getListingById(input = input)
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val listing = response.result.data.toDomain()
                Result.success(listing)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getFeaturedListings(): Result<List<Listing>> = withContext(Dispatchers.IO) {
        try {
            val responseWrapper = trpcApiService.getFeaturedListings()
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val listings = response.result.data.map { it.toDomain() }
                Result.success(listings)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override fun getListings(
        search: String?,
        gameId: String?,
        systemId: String?,
        deviceId: String?,
        emulatorId: String?
    ): Flow<PagingData<Listing>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ListingsPagingSource(
                    trpcApiService = trpcApiService,
                    gameId = gameId,
                    deviceId = deviceId,
                    emulatorId = emulatorId
                )
            }
        ).flow
    }
    
    override suspend fun createListing(form: CreateListingForm): Result<Listing> = withContext(Dispatchers.IO) {
        try {
            val createRequest = TrpcRequestDtos.CreateListingSchema(
                gameId = form.gameId,
                deviceId = form.deviceId ?: "",
                emulatorId = form.emulatorId,
                performanceId = form.performanceRating,
                notes = form.description,
                customFieldValues = form.customSettings.map { (fieldId, value) ->
                    TrpcRequestDtos.CustomFieldValueInput(
                        customFieldDefinitionId = fieldId,
                        value = value
                    )
                }
            )
            
            val request = com.emuready.emuready.data.remote.api.trpc.TrpcRequest(
                com.emuready.emuready.data.remote.api.trpc.TrpcRequestBody(createRequest)
            )
            val responseWrapper = trpcApiService.createListing(request)
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val listing = response.result.data.toDomain()
                Result.success(listing)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun updateListing(listingId: String, form: CreateListingForm): Result<Listing> = withContext(Dispatchers.IO) {
        try {
            val updateRequest = TrpcRequestDtos.UpdateListingSchema(
                id = listingId,
                performanceId = form.performanceRating,
                notes = form.description,
                customFieldValues = form.customSettings.map { (fieldId, value) ->
                    TrpcRequestDtos.CustomFieldValueInput(
                        customFieldDefinitionId = fieldId,
                        value = value
                    )
                }
            )
            
            val request = com.emuready.emuready.data.remote.api.trpc.TrpcRequest(
                com.emuready.emuready.data.remote.api.trpc.TrpcRequestBody(updateRequest)
            )
            val responseWrapper = trpcApiService.updateListing(request)
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val listing = response.result.data.toDomain()
                Result.success(listing)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun deleteListing(listingId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val request = com.emuready.emuready.data.remote.api.trpc.TrpcRequest(
                com.emuready.emuready.data.remote.api.trpc.TrpcRequestBody(TrpcRequestDtos.DeleteListingSchema(id = listingId))
            )
            val responseWrapper = trpcApiService.deleteListing(request)
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else {
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun voteListing(listingId: String, isUpvote: Boolean): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val request = com.emuready.emuready.data.remote.api.trpc.TrpcRequest(
                com.emuready.emuready.data.remote.api.trpc.TrpcRequestBody(TrpcRequestDtos.VoteListingSchema(listingId = listingId, value = isUpvote))
            )
            val responseWrapper = trpcApiService.voteListing(request)
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else {
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getUserVote(listingId: String): Result<Boolean?> = withContext(Dispatchers.IO) {
        try {
            val input = TrpcInputHelper.createInput(TrpcRequestDtos.IdRequest(id = listingId))
            val responseWrapper = trpcApiService.getUserVote(input = input)
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val vote = response.result.data.vote
                Result.success(vote)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getListingComments(listingId: String): Result<List<Comment>> = withContext(Dispatchers.IO) {
        try {
            val input = TrpcInputHelper.createInput(TrpcRequestDtos.IdRequest(id = listingId))
            val responseWrapper = trpcApiService.getListingComments(input = input)
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val comments = response.result.data.comments.map { it.toDomain() }
                Result.success(comments)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun createComment(listingId: String, content: String): Result<Comment> = withContext(Dispatchers.IO) {
        try {
            val request = com.emuready.emuready.data.remote.api.trpc.TrpcRequest(
                com.emuready.emuready.data.remote.api.trpc.TrpcRequestBody(TrpcRequestDtos.CreateCommentSchema(listingId = listingId, content = content))
            )
            val responseWrapper = trpcApiService.createComment(request)
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val comment = response.result.data.toDomain()
                Result.success(comment)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }

    override fun getMobileListingsForGame(gameId: String): Flow<PagingData<Listing>> {
        // TODO: Fix MobileListingsPagingSource reference
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MobileListingsPagingSource(
                    trpcApiService = trpcApiService,
                    gameId = gameId
                )
            }
        ).flow
    }

    override fun getPcListingsForGame(gameId: String): Flow<PagingData<PcListing>> {
        // TODO: Fix PcListingsPagingSource reference
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                // PcListingsPagingSource(
                //     trpcApiService = trpcApiService,
                //     requestBuilder = requestBuilder,
                //     gameId = gameId
                // )
                // Temporary empty paging source
                EmptyPcPagingSource()
            }
        ).flow
    }
    
    override suspend fun createPcListing(form: CreateListingForm): Result<PcListing> {
        // TODO: Implement PC listing creation
        return Result.failure(NotImplementedError("PC listing creation not implemented yet"))
    }
}