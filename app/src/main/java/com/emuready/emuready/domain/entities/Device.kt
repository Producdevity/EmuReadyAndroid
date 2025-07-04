package com.emuready.emuready.domain.entities

data class Device(
    val id: String,
    val name: String,
    val type: DeviceType,
    val manufacturer: String,
    val model: String,
    val cpuArchitecture: String,
    val cpuInfo: String,
    val totalMemoryMB: Int,
    val availableMemoryMB: Int,
    val androidVersion: String,
    val apiLevel: Int,
    val isEmulatorCompatible: Boolean,
    val detectedAt: Long
)

enum class DeviceType {
    STEAM_DECK,
    ROG_ALLY,
    LENOVO_LEGION_GO,
    GPD_WIN,
    GPD_POCKET,
    AYA_NEO,
    ONEXPLAYER,
    HANDHELD_PC,
    PHONE_TABLET,
    UNKNOWN
}