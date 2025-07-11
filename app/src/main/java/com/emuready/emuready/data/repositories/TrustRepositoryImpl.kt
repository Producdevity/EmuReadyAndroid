package com.emuready.emuready.data.repositories

import com.emuready.emuready.data.mappers.*
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
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
            val responseWrapper = trpcApiService.getMyTrustInfo()
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val trustInfo = response.result.data.toDomain()
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
            val responseWrapper = trpcApiService.getUserTrustInfo(userId = userId)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val trustInfo = response.result.data.toDomain()
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
            val responseWrapper = trpcApiService.getTrustLevels()
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val trustLevels = response.result.data.map { it.toDomain() }
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
            val responseWrapper = trpcApiService.isVerifiedDeveloper(userId = "", emulatorId = emulatorId)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val isVerified = response.result.data.isVerified
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
            val responseWrapper = trpcApiService.verifyListing(TrpcRequestBuilder().buildRequest(TrpcRequestDtos.VerifyListingSchema(listingId = listingId, notes = notes)))
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
    
    override suspend fun removeVerification(verificationId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val responseWrapper = trpcApiService.removeVerification(TrpcRequestBuilder().buildRequest(TrpcRequestDtos.RemoveVerificationSchema(verificationId = verificationId)))
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
    
    override suspend fun getListingVerifications(listingId: String): Result<List<Verification>> = withContext(Dispatchers.IO) {
        try {
            val responseWrapper = trpcApiService.getListingVerifications(listingId = listingId)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val verifications = (response.result.data as? List<*>)?.mapNotNull { (it as? com.emuready.emuready.data.remote.dto.TrpcResponseDtos.MobileVerification)?.toDomain() } ?: emptyList()
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
            val responseWrapper = trpcApiService.getMyVerifications(limit = 50, page = 1)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val verifications = (response.result.data as? List<*>)?.mapNotNull { (it as? com.emuready.emuready.data.remote.dto.TrpcResponseDtos.MobileVerification)?.toDomain() } ?: emptyList()
                Result.success(verifications)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
}