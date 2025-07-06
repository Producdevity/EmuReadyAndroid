package com.emuready.emuready.data.repositories

import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.UserIdRequest
import com.emuready.emuready.data.remote.api.trpc.TrpcRequestBuilder
import com.emuready.emuready.data.remote.dto.CreateListingReportSchema
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
                ReportReason.INAPPROPRIATE_CONTENT -> com.emuready.emuready.data.remote.dto.ReportReason.INAPPROPRIATE_CONTENT
                ReportReason.SPAM -> com.emuready.emuready.data.remote.dto.ReportReason.SPAM
                ReportReason.MISLEADING_INFORMATION -> com.emuready.emuready.data.remote.dto.ReportReason.MISLEADING_INFORMATION
                ReportReason.FAKE_LISTING -> com.emuready.emuready.data.remote.dto.ReportReason.FAKE_LISTING
                ReportReason.COPYRIGHT_VIOLATION -> com.emuready.emuready.data.remote.dto.ReportReason.COPYRIGHT_VIOLATION
                ReportReason.OTHER -> com.emuready.emuready.data.remote.dto.ReportReason.OTHER
            }
            
            val request = requestBuilder.buildQuery(
                CreateListingReportSchema(listingId, reportReason, description)
            )
            val response = trpcApiService.createListingReport(request)
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data?.json != null) {
                Result.success(Unit)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun checkUserHasReports(userId: String): Result<Pair<Boolean, Int>> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildQuery(UserIdRequest(userId))
            val response = trpcApiService.checkUserHasReports(request)
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data?.json != null) {
                val userReports = response.result.data.json
                Result.success(Pair(userReports.hasReports, userReports.reportCount))
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
}