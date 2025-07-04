package com.emuready.emuready.data.services

import android.content.Context
import android.content.SharedPreferences
import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.domain.entities.DeviceType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val deviceDetectionService: DeviceDetectionService
) {
    
    private val preferences: SharedPreferences = context.getSharedPreferences("device_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }
    
    private val _currentDevice = MutableStateFlow<Device?>(null)
    val currentDevice: StateFlow<Device?> = _currentDevice.asStateFlow()
    
    private val _detectedDevices = MutableStateFlow<List<Device>>(emptyList())
    val detectedDevices: StateFlow<List<Device>> = _detectedDevices.asStateFlow()
    
    init {
        loadSavedDevices()
    }
    
    suspend fun detectAndSaveCurrentDevice(): Device {
        val device = deviceDetectionService.detectDevice()
        _currentDevice.value = device
        saveDevice(device)
        return device
    }
    
    fun getCurrentDevice(): Device? {
        return _currentDevice.value
    }
    
    fun getDeviceCompatibilityInfo(device: Device): DeviceCompatibilityInfo {
        return when (device.type) {
            DeviceType.STEAM_DECK -> DeviceCompatibilityInfo(
                isCompatible = true,
                compatibilityScore = 95,
                recommendedSettings = getSteamDeckSettings(),
                limitations = emptyList(),
                notes = "Excellent compatibility with most emulators"
            )
            
            DeviceType.ROG_ALLY -> DeviceCompatibilityInfo(
                isCompatible = true,
                compatibilityScore = 92,
                recommendedSettings = getROGAllySettings(),
                limitations = listOf("May require custom TDP settings"),
                notes = "Great compatibility with AMD APU optimization"
            )
            
            DeviceType.LENOVO_LEGION_GO -> DeviceCompatibilityInfo(
                isCompatible = true,
                compatibilityScore = 90,
                recommendedSettings = getLegionGoSettings(),
                limitations = listOf("Screen resolution may need adjustment"),
                notes = "Good compatibility with larger screen considerations"
            )
            
            DeviceType.GPD_WIN -> DeviceCompatibilityInfo(
                isCompatible = true,
                compatibilityScore = 85,
                recommendedSettings = getGPDWinSettings(),
                limitations = listOf("Limited by smaller screen and controls"),
                notes = "Compatible but may need control scheme adjustments"
            )
            
            DeviceType.AYA_NEO -> DeviceCompatibilityInfo(
                isCompatible = true,
                compatibilityScore = 88,
                recommendedSettings = getAyaNeoSettings(),
                limitations = listOf("Battery life considerations"),
                notes = "Good compatibility with AMD APU"
            )
            
            DeviceType.ONEXPLAYER -> DeviceCompatibilityInfo(
                isCompatible = true,
                compatibilityScore = 87,
                recommendedSettings = getOneXPlayerSettings(),
                limitations = listOf("May require thermal management"),
                notes = "Compatible with performance tuning"
            )
            
            DeviceType.HANDHELD_PC -> DeviceCompatibilityInfo(
                isCompatible = true,
                compatibilityScore = 80,
                recommendedSettings = getGenericHandheldSettings(),
                limitations = listOf("Compatibility depends on specific hardware"),
                notes = "Basic compatibility, may need manual tuning"
            )
            
            DeviceType.PHONE_TABLET -> DeviceCompatibilityInfo(
                isCompatible = device.isEmulatorCompatible,
                compatibilityScore = if (device.isEmulatorCompatible) 70 else 30,
                recommendedSettings = getMobileSettings(),
                limitations = listOf("Touch controls", "Limited performance", "Battery drain"),
                notes = if (device.isEmulatorCompatible) "Limited compatibility" else "Not recommended for emulation"
            )
            
            DeviceType.GPD_POCKET -> DeviceCompatibilityInfo(
                isCompatible = false,
                compatibilityScore = 20,
                recommendedSettings = emptyMap(),
                limitations = listOf("Insufficient performance", "Small screen", "Limited controls"),
                notes = "Not suitable for emulation"
            )
            
            DeviceType.UNKNOWN -> DeviceCompatibilityInfo(
                isCompatible = false,
                compatibilityScore = 0,
                recommendedSettings = emptyMap(),
                limitations = listOf("Unknown hardware capabilities"),
                notes = "Cannot determine compatibility"
            )
        }
    }
    
    private fun saveDevice(device: Device) {
        val deviceJson = json.encodeToString(device)
        preferences.edit()
            .putString("current_device", deviceJson)
            .putLong("last_detection", System.currentTimeMillis())
            .apply()
        
        // Add to detected devices list
        val currentDevices = _detectedDevices.value.toMutableList()
        val existingIndex = currentDevices.indexOfFirst { it.id == device.id }
        if (existingIndex >= 0) {
            currentDevices[existingIndex] = device
        } else {
            currentDevices.add(device)
        }
        _detectedDevices.value = currentDevices
        
        // Save detected devices list
        val devicesJson = json.encodeToString(currentDevices)
        preferences.edit()
            .putString("detected_devices", devicesJson)
            .apply()
    }
    
    private fun loadSavedDevices() {
        try {
            val currentDeviceJson = preferences.getString("current_device", null)
            if (currentDeviceJson != null) {
                val device = json.decodeFromString<Device>(currentDeviceJson)
                _currentDevice.value = device
            }
            
            val detectedDevicesJson = preferences.getString("detected_devices", null)
            if (detectedDevicesJson != null) {
                val devices = json.decodeFromString<List<Device>>(detectedDevicesJson)
                _detectedDevices.value = devices
            }
        } catch (e: Exception) {
            // If loading fails, clear corrupted data
            preferences.edit().clear().apply()
        }
    }
    
    fun clearDeviceData() {
        preferences.edit().clear().apply()
        _currentDevice.value = null
        _detectedDevices.value = emptyList()
    }
    
    fun isDeviceDetectionStale(): Boolean {
        val lastDetection = preferences.getLong("last_detection", 0)
        val staleThreshold = 24 * 60 * 60 * 1000 // 24 hours
        return System.currentTimeMillis() - lastDetection > staleThreshold
    }
    
    private fun getSteamDeckSettings(): Map<String, String> = mapOf(
        "resolution" to "1280x800",
        "fps_limit" to "60",
        "tdp" to "auto",
        "upscaling" to "fsr",
        "vsync" to "on"
    )
    
    private fun getROGAllySettings(): Map<String, String> = mapOf(
        "resolution" to "1920x1080",
        "fps_limit" to "60",
        "tdp" to "25W",
        "upscaling" to "fsr",
        "vsync" to "on"
    )
    
    private fun getLegionGoSettings(): Map<String, String> = mapOf(
        "resolution" to "2560x1600",
        "fps_limit" to "60",
        "scaling" to "125%",
        "upscaling" to "fsr",
        "vsync" to "on"
    )
    
    private fun getGPDWinSettings(): Map<String, String> = mapOf(
        "resolution" to "1280x720",
        "fps_limit" to "45",
        "scaling" to "100%",
        "upscaling" to "linear",
        "vsync" to "on"
    )
    
    private fun getAyaNeoSettings(): Map<String, String> = mapOf(
        "resolution" to "1280x800",
        "fps_limit" to "60",
        "tdp" to "auto",
        "upscaling" to "fsr",
        "vsync" to "on"
    )
    
    private fun getOneXPlayerSettings(): Map<String, String> = mapOf(
        "resolution" to "1920x1200",
        "fps_limit" to "60",
        "scaling" to "125%",
        "upscaling" to "fsr",
        "vsync" to "on"
    )
    
    private fun getGenericHandheldSettings(): Map<String, String> = mapOf(
        "resolution" to "1280x720",
        "fps_limit" to "45",
        "scaling" to "100%",
        "upscaling" to "linear",
        "vsync" to "on"
    )
    
    private fun getMobileSettings(): Map<String, String> = mapOf(
        "resolution" to "native",
        "fps_limit" to "30",
        "scaling" to "100%",
        "upscaling" to "linear",
        "vsync" to "off"
    )
    
    data class DeviceCompatibilityInfo(
        val isCompatible: Boolean,
        val compatibilityScore: Int,
        val recommendedSettings: Map<String, String>,
        val limitations: List<String>,
        val notes: String
    )
}