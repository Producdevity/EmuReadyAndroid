package com.emuready.emuready.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcInputHelper
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
import com.emuready.emuready.data.remote.dto.TrpcResponseDtos
import com.emuready.emuready.domain.entities.PcListing
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PcListingsPagingSource(
    private val trpcApiService: EmuReadyTrpcApiService,
    private val gameId: String
) : PagingSource<Int, PcListing>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PcListing> {
        val page = params.key ?: 1
        return try {
            val input = TrpcInputHelper.createInput(
                TrpcRequestDtos.GetPcListingsSchema(
                    page = page,
                    limit = params.loadSize,
                    gameId = gameId
                )
            )
            val responseWrapper = trpcApiService.getPcListings(input = input)
            val response = responseWrapper

            if (response.error != null) {
                return LoadResult.Error(Exception(response.error.message))
            }

            // getPcListings returns MobilePcListingsResponse
            val listingsResponse = response.result?.data
            val listings = listingsResponse?.listings?.map { it.toDomain() } ?: emptyList()

            LoadResult.Page(
                data = listings,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (listings.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PcListing>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1) ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
