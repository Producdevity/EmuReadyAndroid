package com.emuready.emuready.domain.entities

/**
 * Emulator application for gaming systems
 */
data class Emulator(
    val id: String,
    val name: String,
    val logoUrl: String?,
    val supportedSystems: List<System>,
    val customFields: List<CustomFieldDefinition>
) {
    companion object {
        fun empty() = Emulator(
            id = "",
            name = "",
            logoUrl = null,
            supportedSystems = emptyList(),
            customFields = emptyList()
        )
    }
    
    val hasCustomFields: Boolean get() = customFields.isNotEmpty()
    
    val isEden: Boolean get() = name.lowercase().contains("eden")
    
    val supportedSystemNames: List<String> get() = supportedSystems.map { it.name }
}

/**
 * Emulator configuration preset
 */
data class EmulatorPreset(
    val id: String,
    val emulatorId: String,
    val name: String,
    val description: String?,
    val configuration: Map<String, String>, // Setting key to value
    val isOfficial: Boolean, // Created by emulator developers
    val isPublic: Boolean, // Shared with community
    val createdBy: String, // User ID
    val createdAt: Long,
    val usageCount: Int // How many times it's been used
) {
    val isPopular: Boolean get() = usageCount > 100
    
    val displayName: String get() = if (isOfficial) "$name (Official)" else name
}

/**
 * Eden emulator integration
 */
object EdenIntegration {
    const val PACKAGE_NAME = "dev.eden.eden_emulator"
    const val LAUNCH_ACTION = "dev.eden.eden_emulator.LAUNCH_WITH_CUSTOM_CONFIG"
    
    /**
     * Convert configuration map to Eden INI format
     */
    fun configurationToINI(config: Map<String, String>): String {
        return config.entries.joinToString("\n") { (key, value) ->
            "$key=$value"
        }
    }
    
    /**
     * Parse Eden INI format to configuration map
     */
    fun parseINIConfiguration(ini: String): Map<String, String> {
        return ini.lines()
            .filter { it.isNotBlank() && it.contains("=") }
            .associate { line ->
                val parts = line.split("=", limit = 2)
                parts[0].trim() to parts[1].trim()
            }
    }
}