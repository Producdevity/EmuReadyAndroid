package com.emuready.emuready.domain.usecases

import com.emuready.emuready.domain.repositories.StatsRepository
import javax.inject.Inject

data class AppStats(
    val totalGames: Int,
    val totalDevices: Int,
    val totalListings: Int,
    val totalEmulators: Int,
    val totalUsers: Int
)

class GetStatsUseCase @Inject constructor(
    private val statsRepository: StatsRepository
) {
    suspend operator fun invoke(): Result<AppStats> {
        return statsRepository.getStats()
    }
}