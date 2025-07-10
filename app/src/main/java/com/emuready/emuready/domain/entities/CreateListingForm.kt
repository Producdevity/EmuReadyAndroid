package com.emuready.emuready.domain.entities

/**
 * Form data for creating a new listing
 */
data class CreateListingForm(
    val gameId: String,
    val deviceId: String? = null,
    val emulatorId: String,
    val performanceRating: Int,
    val description: String,
    val customSettings: Map<String, String> = emptyMap(),
    val screenshots: List<String> = emptyList(),
    val isPublic: Boolean = true,
    val type: ListingType = ListingType.MOBILE_DEVICE,
    // PC specific fields
    val cpuId: String? = null,
    val gpuId: String? = null,
    val memorySize: Int? = null,
    val osType: String? = null,
    val osVersion: String? = null
) {
    fun validate(): Boolean {
        return when (type) {
            ListingType.MOBILE_DEVICE -> {
                gameId.isNotBlank() && 
                deviceId?.isNotBlank() == true && 
                emulatorId.isNotBlank() && 
                performanceRating > 0 && 
                description.isNotBlank()
            }
            ListingType.PC_CONFIGURATION -> {
                gameId.isNotBlank() && 
                cpuId?.isNotBlank() == true && 
                gpuId?.isNotBlank() == true && 
                memorySize != null && memorySize > 0 && 
                emulatorId.isNotBlank() && 
                performanceRating > 0 && 
                description.isNotBlank()
            }
            ListingType.UNKNOWN -> false
        }
    }
    
    fun isValid(): Boolean = validate()
    
    fun getValidationErrors(): List<String> {
        val errors = mutableListOf<String>()
        
        if (gameId.isBlank()) errors.add("Game ID is required")
        if (emulatorId.isBlank()) errors.add("Emulator ID is required")
        if (performanceRating <= 0) errors.add("Performance rating must be greater than 0")
        if (description.isBlank()) errors.add("Description is required")
        
        when (type) {
            ListingType.MOBILE_DEVICE -> {
                if (deviceId?.isBlank() != false) errors.add("Device ID is required for mobile listings")
            }
            ListingType.PC_CONFIGURATION -> {
                if (cpuId?.isBlank() != false) errors.add("CPU ID is required for PC listings")
                if (gpuId?.isBlank() != false) errors.add("GPU ID is required for PC listings")
                if (memorySize == null || memorySize <= 0) errors.add("Memory size must be greater than 0 for PC listings")
            }
            ListingType.UNKNOWN -> {
                errors.add("Listing type must be specified")
            }
        }
        
        return errors
    }
}