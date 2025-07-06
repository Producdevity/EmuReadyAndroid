package com.emuready.emuready.domain.usecases

import com.emuready.emuready.domain.repositories.ReportRepository
import javax.inject.Inject

class CheckUserReportsUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(userId: String): Result<Pair<Boolean, Int>> {
        return reportRepository.checkUserHasReports(userId)
    }
}