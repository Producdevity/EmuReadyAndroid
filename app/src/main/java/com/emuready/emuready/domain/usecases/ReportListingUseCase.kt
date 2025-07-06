package com.emuready.emuready.domain.usecases

import com.emuready.emuready.domain.entities.ReportReason
import com.emuready.emuready.domain.repositories.ReportRepository
import javax.inject.Inject

class ReportListingUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(
        listingId: String,
        reason: ReportReason,
        description: String
    ): Result<Unit> {
        return reportRepository.reportListing(listingId, reason, description)
    }
}