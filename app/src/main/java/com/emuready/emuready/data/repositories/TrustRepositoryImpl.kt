package com.emuready.emuready.data.repositories

import com.emuready.emuready.data.mappers.*
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.EmulatorIdRequest
import com.emuready.emuready.data.remote.api.ListingIdRequest
import com.emuready.emuready.data.remote.api.UserIdRequest
import com.emuready.emuready.data.remote.api.VerifyListingRequest
import com.emuready.emuready.data.remote.api.trpc.TrpcRequestBuilder
import com.emuready.emuready.domain.entities.TrustLevel
import com.emuready.emuready.domain.entities.UserTrustInfo
import com.emuready.emuready.domain.entities.Verification
import com.emuready.emuready.domain.exceptions.ApiException
import com.emuready.emuready.domain.exceptions.NetworkException
import com.emuready.emuready.domain.repositories.TrustRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrustRepositoryImpl @Inject constructor(
    private val trpcApiService: EmuReadyTrpcApiService
) : TrustRepository {
    
    private val requestBuilder = TrpcRequestBuilder()
    
    override suspend fun getMyTrustInfo(): Result<UserTrustInfo> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildQuery(Unit)
            val response = trpcApiService.getMyTrustInfo(request)
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data?.json != null) {
                val trustInfo = response.result.data.json.toDomain()
                Result.success(trustInfo)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getUserTrustInfo(userId: String): Result<UserTrustInfo> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildQuery(UserIdRequest(userId))
            val response = trpcApiService.getUserTrustInfo(request)
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data?.json != null) {
                val trustInfo = response.result.data.json.toDomain()
                Result.success(trustInfo)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getTrustLevels(): Result<List<TrustLevel>> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildQuery(Unit)
            val response = trpcApiService.getTrustLevels(request)
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data?.json != null) {
                val trustLevels = response.result.data.json.map { it.toDomain() }
                Result.success(trustLevels)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun isVerifiedDeveloper(emulatorId: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildQuery(EmulatorIdRequest(emulatorId))
            val response = trpcApiService.isVerifiedDeveloper(request)
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data?.json != null) {
                val isVerified = response.result.data.json.success
                Result.success(isVerified)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun verifyListing(listingId: String, notes: String?): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildQuery(VerifyListingRequest(listingId, notes))
            val response = trpcApiService.verifyListing(request)
            
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
    
    override suspend fun removeVerification(verificationId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildQuery(com.emuready.emuready.data.remote.api.IdRequest(verificationId))
            val response = trpcApiService.removeVerification(request)
            
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
    
    override suspend fun getListingVerifications(listingId: String): Result<List<Verification>> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildQuery(ListingIdRequest(listingId))
            val response = trpcApiService.getListingVerifications(request)
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data?.json != null) {
                val verifications = response.result.data.json.verifications.map { it.toDomain() }
                Result.success(verifications)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getMyVerifications(): Result<List<Verification>> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildQuery(com.emuready.emuready.data.remote.api.PaginationRequest())
            val response = trpcApiService.getMyVerifications(request)
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data?.json != null) {
                val verifications = response.result.data.json.map { it.toDomain() }
                Result.success(verifications)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
}