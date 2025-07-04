package com.emuready.emuready.domain.repositories

import com.emuready.emuready.domain.entities.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    suspend fun detectCurrentDevice(): Device
    
    fun getCurrentDevice(): Flow<Device?>
    
    fun getDetectedDevices(): Flow<List<Device>>
    
    suspend fun saveDevice(device: Device)
    
    suspend fun clearDeviceData()
    
    fun isDeviceDetectionStale(): Boolean
}