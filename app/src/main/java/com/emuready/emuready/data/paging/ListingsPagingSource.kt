package com.emuready.emuready.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcInputHelper
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
import com.emuready.emuready.data.remote.dto.TrpcResponseDtos
import com.emuready.emuready.domain.entities.Listing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ListingsPagingSource(
    private val trpcApiService: EmuReadyTrpcApiService,
    private val gameId: String? = null,
    private val deviceId: String? = null,
    private val emulatorId: String? = null
) : PagingSource<Int, Listing>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Listing> {
        return withContext(Dispatchers.IO) {
            try {
                val offset = params.key ?: 0
                
                val listings = if (gameId != null) {
                    // Use getListingsByGame for game-specific listings
                    val input = TrpcInputHelper.createInput(TrpcRequestDtos.GameIdRequest(gameId = gameId))
                    val responseWrapper = trpcApiService.getListingsByGame(input = input)
                    val response = responseWrapper
                    
                    if (response.error != null) {
                        return@withContext LoadResult.Error(Exception(response.error.message))
                    }
                    
                    // getListingsByGame returns a list directly
                    val listingsData = response.result?.data
                    listingsData?.map { it.toDomain() } ?: emptyList()
                } else {
                    // Use getListings for general listings
                    val input = TrpcInputHelper.createInput(
                        TrpcRequestDtos.GetListingsSchema(
                            offset = offset,
                            limit = params.loadSize,
                            deviceId = deviceId,
                            emulatorId = emulatorId
                        )
                    )
                    val responseWrapper = trpcApiService.getListings(input = input)
                    val response = responseWrapper
                    
                    if (response.error != null) {
                        return@withContext LoadResult.Error(Exception(response.error.message))
                    }
                    
                    // getListings returns MobileListingsResponse
                    val listingsResponse = response.result?.data
                    listingsResponse?.listings?.map { it.toDomain() } ?: emptyList()
                }
                
                LoadResult.Page(
                    data = listings,
                    prevKey = if (offset == 0) null else offset - params.loadSize,
                    nextKey = if (listings.isEmpty()) null else offset + params.loadSize
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Listing>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}