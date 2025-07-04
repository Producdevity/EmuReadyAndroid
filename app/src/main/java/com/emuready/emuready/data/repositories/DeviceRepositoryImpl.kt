package com.emuready.emuready.data.repositories

import com.emuready.emuready.data.services.DeviceManager
import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.domain.repositories.DeviceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepositoryImpl @Inject constructor(
    private val deviceManager: DeviceManager
) : DeviceRepository {
    
    override suspend fun detectCurrentDevice(): Device {
        return deviceManager.detectAndSaveCurrentDevice()
    }
    
    override fun getCurrentDevice(): Flow<Device?> {
        return deviceManager.currentDevice
    }
    
    override fun getDetectedDevices(): Flow<List<Device>> {
        return deviceManager.detectedDevices
    }
    
    override suspend fun saveDevice(device: Device) {
        // DeviceManager handles persistence internally
        // This could be extracted to a separate persistence layer if needed
    }
    
    override suspend fun clearDeviceData() {
        deviceManager.clearDeviceData()
    }
    
    override fun isDeviceDetectionStale(): Boolean {
        return deviceManager.isDeviceDetectionStale()
    }
}