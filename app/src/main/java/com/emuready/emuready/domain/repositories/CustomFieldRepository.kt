package com.emuready.emuready.domain.repositories

import com.emuready.emuready.domain.entities.CustomFieldDefinition

interface CustomFieldRepository {
    
    /**
     * Get custom field definitions for a specific emulator
     */
    suspend fun getCustomFieldsByEmulator(emulatorId: String): Result<List<CustomFieldDefinition>>
}