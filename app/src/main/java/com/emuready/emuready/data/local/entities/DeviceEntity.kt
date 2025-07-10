package com.emuready.emuready.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class DeviceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val manufacturer: String,
    val model: String,
    val chipset: String,
    val gpu: String,
    val ramSize: Int,
    val storageSize: Int,
    val screenSize: Float,
    val screenResolution: String,
    val operatingSystem: String,
    val isVerified: Boolean,
    val benchmarkScore: Int?,
    val registeredAt: String // Store as ISO string
)