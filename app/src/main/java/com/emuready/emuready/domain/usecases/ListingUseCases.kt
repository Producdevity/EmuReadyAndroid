package com.emuready.emuready.domain.usecases

import androidx.paging.PagingData
import com.emuready.emuready.domain.entities.*
import com.emuready.emuready.domain.repositories.ListingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Get paginated listings with filtering and search
 */
class GetListingsUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    operator fun invoke(
        search: String? = null,
        gameId: String? = null,
        systemId: String? = null,
        deviceId: String? = null,
        emulatorId: String? = null
    ): Flow<PagingData<Listing>> {
        return listingRepository.getListings(
            search = search,
            gameId = gameId,
            systemId = systemId,
            deviceId = deviceId,
            emulatorId = emulatorId
        )
    }
}

/**
 * Get listings for a specific game
 */
class GetListingsByGameUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    suspend operator fun invoke(gameId: String): Result<List<Listing>> {
        return listingRepository.getListingsByGameId(gameId)
    }
}

/**
 * Get listings by user
 */
class GetUserListingsUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    suspend operator fun invoke(userId: String): Result<List<Listing>> {
        return listingRepository.getListingsByUserId(userId)
    }
}

/**
 * Get a specific listing by ID
 */
class GetListingByIdUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    suspend operator fun invoke(listingId: String): Result<Listing> {
        return listingRepository.getListingById(listingId)
    }
}

/**
 * Get featured/top listings
 */
class GetFeaturedListingsUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    suspend operator fun invoke(): Result<List<Listing>> {
        return listingRepository.getFeaturedListings()
    }
}


/**
 * Update an existing listing
 */
class UpdateListingUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    suspend operator fun invoke(listingId: String, form: CreateListingForm): Result<Listing> {
        return if (form.isValid()) {
            listingRepository.updateListing(listingId, form)
        } else {
            Result.failure(IllegalArgumentException("Invalid listing form: ${form.getValidationErrors().joinToString(", ")}"))
        }
    }
}

/**
 * Delete a listing
 */
class DeleteListingUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    suspend operator fun invoke(listingId: String): Result<Unit> {
        return listingRepository.deleteListing(listingId)
    }
}

/**
 * Vote on a listing (upvote/downvote)
 */
class VoteListingUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    suspend operator fun invoke(listingId: String, isUpvote: Boolean): Result<Unit> {
        return listingRepository.voteListing(listingId, isUpvote)
    }
}

/**
 * Get user's vote on a listing
 */
class GetUserVoteUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    suspend operator fun invoke(listingId: String): Result<Boolean?> {
        return listingRepository.getUserVote(listingId)
    }
}

/**
 * Get comments for a listing
 */
class GetListingCommentsUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    suspend operator fun invoke(listingId: String): Result<List<Comment>> {
        return listingRepository.getListingComments(listingId)
    }
}

/**
 * Create a comment on a listing
 */
class CreateCommentUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    suspend operator fun invoke(listingId: String, content: String): Result<Comment> {
        return if (content.isNotBlank()) {
            listingRepository.createComment(listingId, content.trim())
        } else {
            Result.failure(IllegalArgumentException("Comment content cannot be empty"))
        }
    }
}

/**
 * Validate listing form data
 */
class ValidateListingFormUseCase @Inject constructor() {
    operator fun invoke(form: CreateListingForm): Result<Unit> {
        return if (form.isValid()) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException(form.getValidationErrors().joinToString(", ")))
        }
    }
}

/**
 * Validate PC listing form data
 */
class ValidatePcListingFormUseCase @Inject constructor() {
    operator fun invoke(form: CreateListingForm): Result<Unit> {
        return if (form.isValid()) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException(form.getValidationErrors().joinToString(", ")))
        }
    }
}

class GetMobileListingsForGameUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    operator fun invoke(gameId: String): Flow<PagingData<Listing>> {
        return listingRepository.getMobileListingsForGame(gameId)
    }
}

class GetPcListingsForGameUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    operator fun invoke(gameId: String): Flow<PagingData<PcListing>> {
        return listingRepository.getPcListingsForGame(gameId)
    }
}