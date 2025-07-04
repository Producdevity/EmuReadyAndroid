package com.emuready.emuready.domain.usecases

import com.emuready.emuready.domain.entities.CreateListingForm
import com.emuready.emuready.domain.entities.GameListing
import com.emuready.emuready.domain.repositories.ListingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListingsByGameUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    operator fun invoke(gameId: String): Flow<List<GameListing>> {
        return listingRepository.getListingsByGameId(gameId)
    }
}

class GetUserListingsUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    operator fun invoke(userId: String): Flow<List<GameListing>> {
        return listingRepository.getListingsByUserId(userId)
    }
}

class GetDeviceListingsUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    operator fun invoke(deviceId: String): Flow<List<GameListing>> {
        return listingRepository.getListingsByDeviceId(deviceId)
    }
}

class CreateListingUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    suspend operator fun invoke(form: CreateListingForm): Result<GameListing> {
        return if (form.isValid()) {
            listingRepository.createListing(form)
        } else {
            Result.failure(IllegalArgumentException("Invalid listing form"))
        }
    }
}

class UpdateListingUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    suspend operator fun invoke(listingId: String, form: CreateListingForm): Result<GameListing> {
        return if (form.isValid()) {
            listingRepository.updateListing(listingId, form)
        } else {
            Result.failure(IllegalArgumentException("Invalid listing form"))
        }
    }
}

class DeleteListingUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    suspend operator fun invoke(listingId: String): Result<Unit> {
        return listingRepository.deleteListing(listingId)
    }
}

class LikeListingUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    suspend operator fun invoke(listingId: String): Result<Unit> {
        return listingRepository.likeListing(listingId)
    }
}

class UnlikeListingUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    suspend operator fun invoke(listingId: String): Result<Unit> {
        return listingRepository.unlikeListing(listingId)
    }
}