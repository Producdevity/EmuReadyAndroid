package com.emuready.emuready.domain.entities

import java.time.Instant

/**
 * Gaming handheld device for emulation compatibility testing
 * Based on the EmuReady specification requirements
 */
data class Device(
    val id: String,
    val name: String,
    val manufacturer: String,
    val model: String,
    val chipset: String, // SoC name
    val gpu: String,
    val ramSize: Int, // RAM in GB
    val storageSize: Int, // Storage in GB
    val screenSize: Float, // Screen size in inches
    val screenResolution: String, // e.g., "1920x1080"
    val operatingSystem: String, // e.g., "Android", "SteamOS", "Windows"
    val isVerified: Boolean, // Verified by community/developers
    val benchmarkScore: Int?, // Performance benchmark score
    val registeredAt: Instant,
    val type: DeviceType = DeviceType.UNKNOWN,
    val isEmulatorCompatible: Boolean = true
) {
    companion object {
        fun empty() = Device(
            id = "",
            name = "",
            manufacturer = "",
            model = "",
            chipset = "",
            gpu = "",
            ramSize = 0,
            storageSize = 0,
            screenSize = 0f,
            screenResolution = "",
            operatingSystem = "",
            isVerified = false,
            benchmarkScore = null,
            registeredAt = Instant.now(),
            type = DeviceType.UNKNOWN,
            isEmulatorCompatible = false
        )
    }
    
    val displayName: String get() = "$manufacturer $model"
    
    val memoryDescription: String get() = "${ramSize}GB RAM / ${storageSize}GB Storage"
    
    val resolutionDescription: String get() = "$screenResolution (${screenSize}\")"
    
    val isHighPerformance: Boolean get() = benchmarkScore != null && benchmarkScore > 10000
}

/**
 * PC CPU component
 */
data class CpuComponent(
    val id: String,
    val brand: ComponentBrand,
    val modelName: String,
    val coreCount: Int,
    val baseClockSpeed: Float // in GHz
) {
    val displayName: String get() = "${brand.displayName} $modelName"
    
    val fullDescription: String get() = "$displayName (${coreCount} cores @ ${baseClockSpeed}GHz)"
}

/**
 * PC GPU component
 */
data class GpuComponent(
    val id: String,
    val brand: ComponentBrand,
    val modelName: String,
    val vramSize: Int, // VRAM in GB
    val architecture: String?
) {
    val displayName: String get() = "${brand.displayName} $modelName"
    
    val fullDescription: String get() = "$displayName (${vramSize}GB VRAM)"
}

/**
 * Component brand enum
 */
enum class ComponentBrand(val displayName: String) {
    INTEL("Intel"),
    AMD("AMD"),
    NVIDIA("NVIDIA"),
    QUALCOMM("Qualcomm"),
    MEDIATEK("MediaTek"),
    SAMSUNG("Samsung"),
    APPLE("Apple"),
    OTHER("Other")
}

// Type aliases for compatibility
typealias Cpu = CpuComponent
typealias Gpu = GpuComponent

/**
 * Device registration form
 */
data class DeviceRegistrationForm(
    val name: String = "",
    val manufacturer: String = "",
    val model: String = "",
    val chipset: String = "",
    val ramSize: Int = 8,
    val storageSize: Int = 256,
    val screenSize: Float = 7.0f,
    val screenResolution: String = "",
    val operatingSystem: String = "",
    val benchmarkScore: Int? = null
) {
    fun isValid(): Boolean = listOf(
        name.isNotEmpty(),
        manufacturer.isNotEmpty(),
        model.isNotEmpty(),
        chipset.isNotEmpty(),
        ramSize > 0,
        storageSize > 0,
        screenSize > 0,
        screenResolution.isNotEmpty(),
        operatingSystem.isNotEmpty()
    ).all { it }
    
    fun getValidationErrors(): List<String> {
        val errors = mutableListOf<String>()
        
        if (name.isEmpty()) errors.add("Device name is required")
        if (manufacturer.isEmpty()) errors.add("Manufacturer is required")
        if (model.isEmpty()) errors.add("Model is required")
        if (chipset.isEmpty()) errors.add("Chipset/SoC is required")
        if (ramSize <= 0) errors.add("RAM size must be greater than 0")
        if (storageSize <= 0) errors.add("Storage size must be greater than 0")
        if (screenSize <= 0) errors.add("Screen size must be greater than 0")
        if (screenResolution.isEmpty()) errors.add("Screen resolution is required")
        if (operatingSystem.isEmpty()) errors.add("Operating system is required")
        
        return errors
    }
}