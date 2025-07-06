package com.emuready.emuready.domain.repositories

import com.emuready.emuready.domain.usecases.AppStats

interface StatsRepository {
    suspend fun getStats(): Result<AppStats>
}