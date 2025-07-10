package com.emuready.emuready.domain.usecases

import com.emuready.emuready.domain.entities.*
import com.emuready.emuready.domain.repositories.EmulatorRepository
import javax.inject.Inject

/**
 * Get all available emulators
 */
class GetEmulatorsUseCase @Inject constructor(
    private val emulatorRepository: EmulatorRepository
) {
    suspend operator fun invoke(
        systemId: String? = null,
        search: String? = null
    ): Result<List<Emulator>> {
        return emulatorRepository.getEmulators(systemId, search)
    }
}

/**
 * Get emulator by ID
 */
class GetEmulatorByIdUseCase @Inject constructor(
    private val emulatorRepository: EmulatorRepository
) {
    suspend operator fun invoke(emulatorId: String): Result<Emulator> {
        return emulatorRepository.getEmulatorById(emulatorId)
    }
}

/**
 * Get custom fields for an emulator
 */
class GetEmulatorCustomFieldsUseCase @Inject constructor(
    private val emulatorRepository: EmulatorRepository
) {
    suspend operator fun invoke(emulatorId: String): Result<List<CustomFieldDefinition>> {
        return emulatorRepository.getCustomFields(emulatorId)
    }
}

/**
 * Get emulator configuration presets
 */
class GetEmulatorPresetsUseCase @Inject constructor(
    private val emulatorRepository: EmulatorRepository
) {
    suspend operator fun invoke(emulatorId: String): Result<List<EmulatorPreset>> {
        return emulatorRepository.getPresets(emulatorId)
    }
}

/**
 * Launch game with Eden emulator
 */
class LaunchGameWithEdenUseCase @Inject constructor(
    private val emulatorRepository: EmulatorRepository
) {
    suspend operator fun invoke(
        gameId: String,
        configuration: Map<String, String>
    ): Result<Unit> {
        return emulatorRepository.launchGameWithEden(gameId, configuration)
    }
}

/**
 * Get Eden emulator configuration for a specific listing
 */
class GetEdenConfigurationUseCase @Inject constructor(
    private val emulatorRepository: EmulatorRepository
) {
    suspend operator fun invoke(listingId: String): Result<Map<String, String>> {
        return emulatorRepository.getEdenConfiguration(listingId)
    }
}

/**
 * Verify if Eden emulator is installed
 */
class VerifyEdenInstalledUseCase @Inject constructor(
    private val emulatorRepository: EmulatorRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return emulatorRepository.isEdenInstalled()
    }
}

/**
 * Get supported gaming systems
 */
class GetSupportedSystemsUseCase @Inject constructor(
    private val emulatorRepository: EmulatorRepository
) {
    suspend operator fun invoke(): Result<List<System>> {
        return emulatorRepository.getSupportedSystems()
    }
}

/**
 * Create custom emulator preset
 */
class CreateEmulatorPresetUseCase @Inject constructor(
    private val emulatorRepository: EmulatorRepository
) {
    suspend operator fun invoke(
        emulatorId: String,
        name: String,
        configuration: Map<String, String>
    ): Result<EmulatorPreset> {
        return if (name.isNotBlank() && configuration.isNotEmpty()) {
            emulatorRepository.createPreset(emulatorId, name, configuration)
        } else {
            Result.failure(IllegalArgumentException("Invalid preset data"))
        }
    }
}

/**
 * Update emulator preset
 */
class UpdateEmulatorPresetUseCase @Inject constructor(
    private val emulatorRepository: EmulatorRepository
) {
    suspend operator fun invoke(
        presetId: String,
        name: String,
        configuration: Map<String, String>
    ): Result<EmulatorPreset> {
        return if (name.isNotBlank() && configuration.isNotEmpty()) {
            emulatorRepository.updatePreset(presetId, name, configuration)
        } else {
            Result.failure(IllegalArgumentException("Invalid preset data"))
        }
    }
}

/**
 * Delete emulator preset
 */
class DeleteEmulatorPresetUseCase @Inject constructor(
    private val emulatorRepository: EmulatorRepository
) {
    suspend operator fun invoke(presetId: String): Result<Unit> {
        return emulatorRepository.deletePreset(presetId)
    }
}

/**
 * Get popular emulators
 */
class GetPopularEmulatorsUseCase @Inject constructor(
    private val emulatorRepository: EmulatorRepository
) {
    suspend operator fun invoke(limit: Int = 10): Result<List<Emulator>> {
        return emulatorRepository.getPopularEmulators(limit)
    }
}

/**
 * Check if user is verified developer for an emulator
 */
class CheckDeveloperVerificationUseCase @Inject constructor(
    private val emulatorRepository: EmulatorRepository
) {
    suspend operator fun invoke(emulatorId: String): Result<Boolean> {
        return emulatorRepository.isDeveloperVerified(emulatorId)
    }
}