package com.emuready.emuready.domain.repositories

import com.emuready.emuready.domain.entities.*

interface EmulatorRepository {
    // Basic emulator operations
    suspend fun getEmulators(systemId: String? = null, search: String? = null): Result<List<Emulator>>
    
    suspend fun getEmulatorById(emulatorId: String): Result<Emulator>
    
    suspend fun getCustomFields(emulatorId: String): Result<List<CustomFieldDefinition>>
    
    suspend fun getPopularEmulators(limit: Int = 10): Result<List<Emulator>>
    
    // Emulator presets
    suspend fun getPresets(emulatorId: String): Result<List<EmulatorPreset>>
    
    suspend fun createPreset(emulatorId: String, name: String, configuration: Map<String, String>): Result<EmulatorPreset>
    
    suspend fun updatePreset(presetId: String, name: String, configuration: Map<String, String>): Result<EmulatorPreset>
    
    suspend fun deletePreset(presetId: String): Result<Unit>
    
    // Gaming systems
    suspend fun getSupportedSystems(): Result<List<System>>
    
    // Eden emulator integration
    suspend fun launchGameWithEden(gameId: String, configuration: Map<String, String>): Result<Unit>
    
    suspend fun getEdenConfiguration(listingId: String): Result<Map<String, String>>
    
    suspend fun isEdenInstalled(): Result<Boolean>
    
    // Developer verification
    suspend fun isDeveloperVerified(emulatorId: String): Result<Boolean>
    
    suspend fun verifyListing(listingId: String, notes: String?): Result<Unit>
    
    suspend fun getVerifiedEmulators(): Result<List<Emulator>>
}