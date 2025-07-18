package com.emuready.emuready.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcInputHelper
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
import com.emuready.emuready.data.remote.dto.TrpcResponseDtos
import com.emuready.emuready.domain.entities.Listing
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MobileListingsPagingSource(
    private val trpcApiService: EmuReadyTrpcApiService,
    private val gameId: String
) : PagingSource<Int, Listing>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Listing> {
        return try {
            val offset = params.key ?: 0
            
            // Use getListingsByGame for game-specific listings
            val input = TrpcInputHelper.createInput(TrpcRequestDtos.GameIdRequest(gameId = gameId))
            val responseWrapper = trpcApiService.getListingsByGame(input = input)
            val response = responseWrapper
            
            if (response.error != null) {
                return LoadResult.Error(Exception(response.error.message))
            }
            
            // getListingsByGame returns a list directly  
            val listingsData = response.result?.data
            val listings = listingsData?.map { it.toDomain() } ?: emptyList()

            LoadResult.Page(
                data = listings,
                prevKey = if (offset == 0) null else offset - params.loadSize,
                nextKey = if (listings.isEmpty()) null else offset + params.loadSize
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Listing>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1) ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
