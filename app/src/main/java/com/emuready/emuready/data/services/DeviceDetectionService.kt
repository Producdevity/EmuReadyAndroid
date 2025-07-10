package com.emuready.emuready.data.services

import android.content.Context
import android.os.Build
import android.util.Log
import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.domain.entities.DeviceType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceDetectionService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val TAG = "DeviceDetectionService"
    
    suspend fun detectDevice(): Device = withContext(Dispatchers.IO) {
        try {
            val cpuInfo = getCpuInfo()
            val memoryInfo = getMemoryInfo()
            val deviceType = detectDeviceType()
            
            // Create Device entity with correct parameters
            Device(
                id = generateDeviceId(),
                name = getDeviceName(),
                manufacturer = Build.MANUFACTURER,
                model = Build.MODEL,
                chipset = Build.HARDWARE,
                gpu = "Unknown",
                ramSize = memoryInfo.totalMemoryMB / 1024,
                storageSize = 32,
                screenSize = 7.0f,
                screenResolution = "${context.resources.displayMetrics.widthPixels}x${context.resources.displayMetrics.heightPixels}",
                operatingSystem = "Android ${Build.VERSION.RELEASE}",
                isVerified = false,
                benchmarkScore = null,
                registeredAt = java.time.Instant.ofEpochMilli(java.lang.System.currentTimeMillis()),
                type = deviceType,
                isEmulatorCompatible = isEmulatorCompatible(deviceType, cpuInfo)
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error detecting device", e)
            createFallbackDevice()
        }
    }
    
    private fun generateDeviceId(): String {
        return "${Build.MANUFACTURER}_${Build.MODEL}_${Build.ID}".replace(" ", "_")
    }
    
    private fun getDeviceName(): String {
        return "${Build.MANUFACTURER} ${Build.MODEL}".trim()
    }
    
    private fun detectDeviceType(): DeviceType {
        val model = Build.MODEL.lowercase()
        val manufacturer = Build.MANUFACTURER.lowercase()
        
        return when {
            // Steam Deck
            model.contains("steam deck") || manufacturer.contains("valve") -> DeviceType.STEAM_DECK
            
            // ROG Ally
            model.contains("rog ally") || (manufacturer.contains("asus") && model.contains("ally")) -> DeviceType.ROG_ALLY
            
            // Lenovo Legion Go
            model.contains("legion go") || (manufacturer.contains("lenovo") && model.contains("legion")) -> DeviceType.LENOVO_LEGION_GO
            
            // GPD devices
            manufacturer.contains("gpd") -> when {
                model.contains("win") -> DeviceType.GPD_WIN
                model.contains("pocket") -> DeviceType.GPD_POCKET
                else -> DeviceType.HANDHELD_PC
            }
            
            // AYA devices
            manufacturer.contains("aya") -> DeviceType.AYA_NEO
            
            // OneXPlayer
            manufacturer.contains("onexplayer") || model.contains("onexplayer") -> DeviceType.ONEXPLAYER
            
            // Ayaneo
            manufacturer.contains("ayaneo") || model.contains("ayaneo") -> DeviceType.AYA_NEO
            
            // Generic handheld detection
            isHandheldDevice() -> DeviceType.HANDHELD_PC
            
            // Default to phone/tablet
            else -> DeviceType.PHONE_TABLET
        }
    }
    
    private fun isHandheldDevice(): Boolean {
        // Check for common handheld indicators
        val model = Build.MODEL.lowercase()
        val manufacturer = Build.MANUFACTURER.lowercase()
        
        val handheldKeywords = listOf(
            "handheld", "gaming", "portable", "steam", "deck", "ally", "legion", "neo", "win", "pocket"
        )
        
        return handheldKeywords.any { keyword ->
            model.contains(keyword) || manufacturer.contains(keyword)
        }
    }
    
    private fun getCpuInfo(): String {
        return try {
            val cpuInfo = StringBuilder()
            BufferedReader(FileReader("/proc/cpuinfo")).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    if (line!!.startsWith("model name") || line!!.startsWith("Hardware") || line!!.startsWith("Processor")) {
                        cpuInfo.append(line).append("\n")
                    }
                }
            }
            cpuInfo.toString().ifEmpty { "CPU info not available" }
        } catch (e: IOException) {
            Log.w(TAG, "Could not read CPU info", e)
            "CPU: ${Build.HARDWARE}"
        }
    }
    
    private fun getMemoryInfo(): MemoryInfo {
        return try {
            val memInfo = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            val activityManager = android.app.ActivityManager.MemoryInfo()
            memInfo.getMemoryInfo(activityManager)
            
            val totalMemoryMB = activityManager.totalMem / (1024 * 1024)
            val availableMemoryMB = activityManager.availMem / (1024 * 1024)
            
            MemoryInfo(totalMemoryMB.toInt(), availableMemoryMB.toInt())
        } catch (e: Exception) {
            Log.w(TAG, "Could not get memory info", e)
            MemoryInfo(0, 0)
        }
    }
    
    private fun isEmulatorCompatible(deviceType: DeviceType, cpuInfo: String): Boolean {
        return when (deviceType) {
            DeviceType.STEAM_DECK,
            DeviceType.ROG_ALLY,
            DeviceType.LENOVO_LEGION_GO,
            DeviceType.GPD_WIN,
            DeviceType.AYA_NEO,
            DeviceType.ONEXPLAYER,
            DeviceType.HANDHELD_PC -> true
            
            DeviceType.GPD_POCKET -> false // Usually too weak for emulation
            
            DeviceType.PHONE_TABLET -> {
                // Check for powerful processors
                val powerfulChips = listOf(
                    "snapdragon 8", "snapdragon 888", "snapdragon 865",
                    "exynos 2100", "exynos 990",
                    "kirin 9000", "kirin 990",
                    "dimensity 9000", "dimensity 1200"
                )
                
                powerfulChips.any { chip ->
                    cpuInfo.lowercase().contains(chip)
                }
            }
            
            DeviceType.UNKNOWN -> false
        }
    }
    
    private fun createFallbackDevice(): Device {
        return Device(
            id = generateDeviceId(),
            name = getDeviceName(),
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            chipset = Build.HARDWARE,
            gpu = "Unknown",
            ramSize = 4,
            storageSize = 32,
            screenSize = 7.0f,
            screenResolution = "${context.resources.displayMetrics.widthPixels}x${context.resources.displayMetrics.heightPixels}",
            operatingSystem = "Android ${Build.VERSION.RELEASE}",
            isVerified = false,
            benchmarkScore = null,
            registeredAt = java.time.Instant.ofEpochMilli(java.lang.System.currentTimeMillis()),
            type = DeviceType.UNKNOWN,
            isEmulatorCompatible = false
        )
    }
    
    data class MemoryInfo(
        val totalMemoryMB: Int,
        val availableMemoryMB: Int
    )
}