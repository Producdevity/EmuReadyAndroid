package com.emuready.emuready.domain.usecases

import com.emuready.emuready.domain.entities.CreateListingForm
import com.emuready.emuready.domain.entities.ListingType
import com.emuready.emuready.domain.entities.PcListing
import com.emuready.emuready.domain.repositories.ListingRepository
import javax.inject.Inject

class CreateListingUseCase @Inject constructor(
    private val listingRepository: ListingRepository
) {
    suspend operator fun invoke(form: CreateListingForm): Result<Any> {
        if (!form.validate()) {
            return Result.failure(IllegalArgumentException("Invalid form data"))
        }
        return when (form.type) {
            ListingType.MOBILE_DEVICE -> listingRepository.createListing(form)
            ListingType.PC_CONFIGURATION -> listingRepository.createPcListing(form)
            ListingType.UNKNOWN -> Result.failure(IllegalArgumentException("Unknown listing type"))
        }
    }
}