package com.emuready.emuready.domain.repositories

import com.emuready.emuready.domain.entities.ReportReason

interface ReportRepository {
    
    /**
     * Report a listing for inappropriate content or other violations
     */
    suspend fun reportListing(
        listingId: String,
        reason: ReportReason,
        description: String
    ): Result<Unit>
    
    /**
     * Check if a user has reports against them
     */
    suspend fun checkUserHasReports(userId: String): Result<Pair<Boolean, Int>>
}