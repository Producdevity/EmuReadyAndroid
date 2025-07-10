package com.emuready.emuready.domain.usecases

import com.emuready.emuready.domain.entities.*
import com.emuready.emuready.domain.repositories.DeviceRepository
import javax.inject.Inject

/**
 * Get all available devices
 */
class GetDevicesUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(
        search: String? = null,
        brandId: String? = null
    ): Result<List<Device>> {
        return deviceRepository.getDevices(search, brandId)
    }
}

/**
 * Get device by ID
 */
class GetDeviceByIdUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(deviceId: String): Result<Device> {
        return deviceRepository.getDeviceById(deviceId)
    }
}

/**
 * Get device brands
 */
class GetDeviceBrandsUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(): Result<List<String>> {
        return deviceRepository.getDeviceBrands()
    }
}

/**
 * Register a new device
 */
class RegisterDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(form: DeviceRegistrationForm): Result<Device> {
        return if (form.isValid()) {
            deviceRepository.registerDevice(form)
        } else {
            Result.failure(IllegalArgumentException("Invalid device form: ${form.getValidationErrors().joinToString(", ")}"))
        }
    }
}

/**
 * Get user's registered devices
 */
class GetUserDevicesUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(): Result<List<Device>> {
        return deviceRepository.getUserDevices()
    }
}

/**
 * Update device information
 */
class UpdateDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(deviceId: String, form: DeviceRegistrationForm): Result<Device> {
        return if (form.isValid()) {
            deviceRepository.updateDevice(deviceId, form)
        } else {
            Result.failure(IllegalArgumentException("Invalid device form: ${form.getValidationErrors().joinToString(", ")}"))
        }
    }
}

/**
 * Delete a registered device
 */
class DeleteDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(deviceId: String): Result<Unit> {
        return deviceRepository.deleteDevice(deviceId)
    }
}

/**
 * Get SoCs for device registration
 */
class GetSoCsUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(search: String? = null): Result<List<String>> {
        return deviceRepository.getSoCs(search)
    }
}

/**
 * Validate device registration form
 */
class ValidateDeviceFormUseCase @Inject constructor() {
    operator fun invoke(form: DeviceRegistrationForm): Result<Unit> {
        return if (form.isValid()) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException(form.getValidationErrors().joinToString(", ")))
        }
    }
}

/**
 * Get popular devices
 */
class GetPopularDevicesUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(limit: Int = 10): Result<List<Device>> {
        return deviceRepository.getPopularDevices(limit)
    }
}

/**
 * Search devices with advanced filters
 */
class SearchDevicesUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(
        query: String,
        brand: String? = null,
        minRam: Int? = null,
        maxRam: Int? = null,
        os: String? = null
    ): Result<List<Device>> {
        return deviceRepository.searchDevices(
            query = query,
            brand = brand,
            minRam = minRam,
            maxRam = maxRam,
            os = os
        )
    }
}