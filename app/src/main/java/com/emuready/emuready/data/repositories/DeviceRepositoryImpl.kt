package com.emuready.emuready.data.repositories

import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcInputHelper
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
import com.emuready.emuready.data.remote.dto.TrpcResponseDtos
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.services.DeviceManager
import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.domain.entities.DeviceRegistrationForm
import com.emuready.emuready.domain.exceptions.ApiException
import com.emuready.emuready.domain.exceptions.NetworkException
import com.emuready.emuready.domain.repositories.DeviceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepositoryImpl @Inject constructor(
    private val trpcApiService: EmuReadyTrpcApiService,
    private val deviceManager: DeviceManager
) : DeviceRepository {
    
    
    override suspend fun getDevices(search: String?, brandId: String?): Result<List<Device>> = withContext(Dispatchers.IO) {
        try {
            val input = TrpcInputHelper.createInput(TrpcRequestDtos.SearchWithBrandRequest(search = search?.takeIf { it.isNotBlank() }, brandId = brandId?.takeIf { it.isNotBlank() }))
            val responseWrapper = trpcApiService.getDevices(input = input)
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val devices = response.result.data.map { it.toDomain() }
                Result.success(devices)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getDeviceById(deviceId: String): Result<Device> = withContext(Dispatchers.IO) {
        try {
            // Note: API doesn't have specific getDeviceById, so we search by ID
            val input = TrpcInputHelper.createInput(TrpcRequestDtos.SearchWithBrandRequest(search = deviceId.takeIf { it.isNotBlank() }, brandId = null))
            val responseWrapper = trpcApiService.getDevices(input = input)
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val device = response.result.data.find { it.id == deviceId }
                if (device != null) {
                    Result.success(device.toDomain())
                } else {
                    Result.failure(ApiException("Device not found"))
                }
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getDeviceBrands(): Result<List<String>> = withContext(Dispatchers.IO) {
        try {
            val responseWrapper = trpcApiService.getDeviceBrands()
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val brands = response.result.data.map { it.name }
                Result.success(brands)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getSoCs(search: String?): Result<List<String>> = withContext(Dispatchers.IO) {
        try {
            val responseWrapper = trpcApiService.getSocs()
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val socs = response.result.data
                    .filter { search == null || it.name.contains(search, ignoreCase = true) }
                    .map { it.name }
                Result.success(socs)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getUserDevices(): Result<List<Device>> = withContext(Dispatchers.IO) {
        try {
            // This would require user authentication context
            // For now, return local detected devices
            val detectedDevice = getDetectedDevice()
            if (detectedDevice != null) {
                Result.success(listOf(detectedDevice))
            } else {
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun registerDevice(form: DeviceRegistrationForm): Result<Device> = withContext(Dispatchers.IO) {
        try {
            // Convert form to device entity and save locally
            val device = Device(
                id = generateDeviceId(),
                name = form.name,
                manufacturer = form.manufacturer,
                model = form.model,
                chipset = form.chipset,
                gpu = "", // Would need to be extracted from chipset
                ramSize = form.ramSize,
                storageSize = form.storageSize,
                screenSize = form.screenSize,
                screenResolution = form.screenResolution,
                operatingSystem = form.operatingSystem,
                isVerified = false,
                benchmarkScore = form.benchmarkScore,
                registeredAt = java.time.Instant.now(),
                type = com.emuready.emuready.domain.entities.DeviceType.HANDHELD_PC,
                isEmulatorCompatible = true
            )
            
            saveDetectedDevice(device)
            Result.success(device)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateDevice(deviceId: String, form: DeviceRegistrationForm): Result<Device> {
        // For local storage, we would replace the existing device
        return registerDevice(form)
    }
    
    override suspend fun deleteDevice(deviceId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Remove from local storage
            deviceManager.clearDeviceData()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getPopularDevices(limit: Int): Result<List<Device>> = withContext(Dispatchers.IO) {
        try {
            val input = TrpcInputHelper.createInput(TrpcRequestDtos.LimitRequest(limit = limit))
            val responseWrapper = trpcApiService.getDevices(input = input)
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val devices = response.result.data.map { it.toDomain() }
                    .sortedByDescending { it.benchmarkScore ?: 0 }
                    .take(limit)
                Result.success(devices)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun searchDevices(
        query: String,
        brand: String?,
        minRam: Int?,
        maxRam: Int?,
        os: String?
    ): Result<List<Device>> = withContext(Dispatchers.IO) {
        try {
            val input = TrpcInputHelper.createInput(TrpcRequestDtos.SearchWithBrandRequest(search = query.takeIf { it.isNotBlank() }, brandId = brand?.takeIf { it.isNotBlank() }))
            val responseWrapper = trpcApiService.getDevices(input = input)
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                var devices = response.result.data.map { it.toDomain() }
                
                // Apply additional filters
                if (minRam != null) {
                    devices = devices.filter { it.ramSize >= minRam }
                }
                if (maxRam != null) {
                    devices = devices.filter { it.ramSize <= maxRam }
                }
                if (os != null) {
                    devices = devices.filter { it.operatingSystem.contains(os, ignoreCase = true) }
                }
                
                Result.success(devices)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    // Local device detection functionality
    override suspend fun detectCurrentDevice(): Device {
        return deviceManager.detectAndSaveCurrentDevice()
    }
    
    override suspend fun saveDetectedDevice(device: Device) {
        // Save to local storage via DeviceManager
        // This would be implemented in DeviceManager
    }
    
    override suspend fun getDetectedDevice(): Device? {
        // Get from local storage via DeviceManager
        return try {
            detectCurrentDevice()
        } catch (e: Exception) {
            null
        }
    }
    
    // Additional methods for use cases
    override suspend fun clearDeviceData(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Clear locally stored device data
            // This would clear local database entries
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getCurrentDevice(): Result<Device> = withContext(Dispatchers.IO) {
        try {
            val device = detectCurrentDevice()
            Result.success(device)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getDetectedDevices(): Result<List<Device>> = withContext(Dispatchers.IO) {
        try {
            // Return list of previously detected devices from local storage
            // For now, return the current device
            val currentDevice = detectCurrentDevice()
            Result.success(listOf(currentDevice))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun generateDeviceId(): String {
        return "device_${java.lang.System.currentTimeMillis()}_${(1000..9999).random()}"
    }
}