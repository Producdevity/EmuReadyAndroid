package com.emuready.emuready.data.repositories

import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
import com.emuready.emuready.data.remote.api.trpc.TrpcRequestBuilder
// import com.emuready.emuready.data.remote.dto.CreateListingReportSchema // TODO: Add missing schema
import com.emuready.emuready.domain.entities.ReportReason
import com.emuready.emuready.domain.exceptions.ApiException
import com.emuready.emuready.domain.exceptions.NetworkException
import com.emuready.emuready.domain.repositories.ReportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val trpcApiService: EmuReadyTrpcApiService
) : ReportRepository {
    
    private val requestBuilder = TrpcRequestBuilder()
    
    override suspend fun reportListing(
        listingId: String,
        reason: ReportReason,
        description: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val reportReason = when (reason) {
                ReportReason.INAPPROPRIATE_CONTENT -> TrpcRequestDtos.ReportReason.INAPPROPRIATE_CONTENT
                ReportReason.SPAM -> TrpcRequestDtos.ReportReason.SPAM
                ReportReason.MISLEADING_INFORMATION -> TrpcRequestDtos.ReportReason.MISLEADING_INFORMATION
                ReportReason.FAKE_LISTING -> TrpcRequestDtos.ReportReason.FAKE_LISTING
                ReportReason.COPYRIGHT_VIOLATION -> TrpcRequestDtos.ReportReason.COPYRIGHT_VIOLATION
                ReportReason.OTHER -> TrpcRequestDtos.ReportReason.OTHER
            }
            
            val request = requestBuilder.buildRequest(
                TrpcRequestDtos.CreateListingReportSchema(listingId, reportReason, description)
            )
            val responseWrapper = trpcApiService.createListingReport(request)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else {
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun checkUserHasReports(userId: String): Result<Pair<Boolean, Int>> = withContext(Dispatchers.IO) {
        try {
            val responseWrapper = trpcApiService.checkUserHasReports(userId = userId)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val userReports = response.result.data
                Result.success(Pair(userReports.hasReports, userReports.reportCount))
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
}