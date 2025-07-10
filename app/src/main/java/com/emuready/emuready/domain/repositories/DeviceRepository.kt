package com.emuready.emuready.domain.repositories

import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.domain.entities.DeviceRegistrationForm

interface DeviceRepository {
    // Basic device operations
    suspend fun getDevices(search: String? = null, brandId: String? = null): Result<List<Device>>
    
    suspend fun getDeviceById(deviceId: String): Result<Device>
    
    suspend fun getDeviceBrands(): Result<List<String>>
    
    suspend fun getSoCs(search: String? = null): Result<List<String>>
    
    // User device management
    suspend fun getUserDevices(): Result<List<Device>>
    
    suspend fun registerDevice(form: DeviceRegistrationForm): Result<Device>
    
    suspend fun updateDevice(deviceId: String, form: DeviceRegistrationForm): Result<Device>
    
    suspend fun deleteDevice(deviceId: String): Result<Unit>
    
    // Device discovery and recommendations
    suspend fun getPopularDevices(limit: Int = 10): Result<List<Device>>
    
    suspend fun searchDevices(
        query: String,
        brand: String? = null,
        minRam: Int? = null,
        maxRam: Int? = null,
        os: String? = null
    ): Result<List<Device>>
    
    // Device detection (local functionality)
    suspend fun detectCurrentDevice(): Device
    
    suspend fun saveDetectedDevice(device: Device)
    
    suspend fun getDetectedDevice(): Device?
    
    // Additional methods for use cases
    suspend fun clearDeviceData(): Result<Unit>
    
    suspend fun getCurrentDevice(): Result<Device>
    
    suspend fun getDetectedDevices(): Result<List<Device>>
}