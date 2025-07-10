package com.emuready.emuready.data.repositories

import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcRequestBuilder
import com.emuready.emuready.domain.exceptions.ApiException
import com.emuready.emuready.domain.exceptions.NetworkException
import com.emuready.emuready.domain.repositories.StatsRepository
import com.emuready.emuready.domain.usecases.AppStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepositoryImpl @Inject constructor(
    private val trpcApiService: EmuReadyTrpcApiService
) : StatsRepository {
    
    private val requestBuilder = TrpcRequestBuilder()
    
    override suspend fun getStats(): Result<AppStats> = withContext(Dispatchers.IO) {
        try {
            println("StatsRepository: Starting getStats API call...")
            val request = requestBuilder.buildRequest(Unit)
            println("StatsRepository: Request built: $request")
            
            val responseWrapper = trpcApiService.getAppStats(request)
            val response = responseWrapper.`0`
            println("StatsRepository: Response received: $response")
            
            if (response.error != null) {
                println("StatsRepository: API Error: ${response.error}")
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val stats = response.result.data
                println("StatsRepository: Successfully got stats: $stats")
                Result.success(
                    AppStats(
                        totalGames = stats.totalGames,
                        totalDevices = stats.totalDevices,
                        totalListings = stats.totalListings,
                        totalEmulators = stats.totalEmulators,
                        totalUsers = stats.totalUsers
                    )
                )
            } else {
                println("StatsRepository: Invalid response format - no data field")
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            println("StatsRepository: Exception in getStats: ${e.message}")
            e.printStackTrace()
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
}