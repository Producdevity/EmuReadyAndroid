package com.emuready.emuready.data.local.dao

import androidx.room.*
import com.emuready.emuready.data.local.entities.DeviceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Query("SELECT * FROM devices WHERE id = :deviceId")
    suspend fun getDeviceById(deviceId: String): DeviceEntity?
    
    @Query("SELECT * FROM devices ORDER BY registeredAt DESC")
    fun getAllDevices(): Flow<List<DeviceEntity>>
    
    @Query("SELECT * FROM devices WHERE isVerified = 1")
    fun getVerifiedDevices(): Flow<List<DeviceEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: DeviceEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevices(devices: List<DeviceEntity>)
    
    @Update
    suspend fun updateDevice(device: DeviceEntity)
    
    @Delete
    suspend fun deleteDevice(device: DeviceEntity)
    
    @Query("DELETE FROM devices WHERE id = :deviceId")
    suspend fun deleteDeviceById(deviceId: String)
}