package com.emuready.emuready.domain.repositories

import com.emuready.emuready.domain.entities.TrustLevel
import com.emuready.emuready.domain.entities.UserTrustInfo
import com.emuready.emuready.domain.entities.Verification

interface TrustRepository {
    suspend fun getMyTrustInfo(): Result<UserTrustInfo>
    suspend fun getUserTrustInfo(userId: String): Result<UserTrustInfo>
    suspend fun getTrustLevels(): Result<List<TrustLevel>>
    suspend fun isVerifiedDeveloper(emulatorId: String): Result<Boolean>
    suspend fun verifyListing(listingId: String, notes: String? = null): Result<Unit>
    suspend fun removeVerification(verificationId: String): Result<Unit>
    suspend fun getListingVerifications(listingId: String): Result<List<Verification>>
    suspend fun getMyVerifications(): Result<List<Verification>>
}