package com.emuready.emuready.domain.usecases

import com.emuready.emuready.domain.entities.CustomFieldDefinition
import com.emuready.emuready.domain.repositories.CustomFieldRepository
import javax.inject.Inject

class GetCustomFieldsUseCase @Inject constructor(
    private val customFieldRepository: CustomFieldRepository
) {
    suspend operator fun invoke(emulatorId: String): Result<List<CustomFieldDefinition>> {
        return customFieldRepository.getCustomFieldsByEmulator(emulatorId)
    }
}