package com.emuready.emuready.domain.usecases

import com.emuready.emuready.domain.entities.TrustLevel
import com.emuready.emuready.domain.entities.UserTrustInfo
import com.emuready.emuready.domain.repositories.TrustRepository
import javax.inject.Inject

class GetMyTrustInfoUseCase @Inject constructor(
    private val trustRepository: TrustRepository
) {
    suspend operator fun invoke(): Result<UserTrustInfo> {
        return trustRepository.getMyTrustInfo()
    }
}

class GetUserTrustInfoUseCase @Inject constructor(
    private val trustRepository: TrustRepository
) {
    suspend operator fun invoke(userId: String): Result<UserTrustInfo> {
        return trustRepository.getUserTrustInfo(userId)
    }
}

class GetTrustLevelsUseCase @Inject constructor(
    private val trustRepository: TrustRepository
) {
    suspend operator fun invoke(): Result<List<TrustLevel>> {
        return trustRepository.getTrustLevels()
    }
}

class VerifyListingUseCase @Inject constructor(
    private val trustRepository: TrustRepository
) {
    suspend operator fun invoke(listingId: String, notes: String? = null): Result<Unit> {
        return trustRepository.verifyListing(listingId, notes)
    }
}

class RemoveVerificationUseCase @Inject constructor(
    private val trustRepository: TrustRepository
) {
    suspend operator fun invoke(verificationId: String): Result<Unit> {
        return trustRepository.removeVerification(verificationId)
    }
}

