package com.emuready.emuready.domain.entities

/**
 * Modern Listing entity that matches the new tRPC API structure
 * This replaces the old Listing.kt with the correct properties
 */
data class Listing(
    val id: String,
    val gameId: String,
    val game: Game? = null,
    val type: ListingType,
    val title: String,
    val description: String?,
    val deviceId: String?,
    val emulatorId: String?,
    val emulatorConfig: String?,
    val performanceLevel: PerformanceLevel,
    val overallRating: Float,
    val framerate: Float,
    val stabilityRating: Float,
    val audioRating: Float,
    val visualRating: Float,
    val customFields: Map<String, String>,
    val userId: String,
    val username: String,
    val isVerified: Boolean,
    val likeCount: Int,
    val dislikeCount: Int,
    val commentCount: Int,
    val createdAt: Long,
    val updatedAt: Long
) {
    companion object {
        fun empty() = Listing(
            id = "",
            gameId = "",
            game = null,
            type = ListingType.UNKNOWN,
            title = "",
            description = null,
            deviceId = null,
            emulatorId = null,
            emulatorConfig = null,
            performanceLevel = PerformanceLevel.UNPLAYABLE,
            overallRating = 0f,
            framerate = 0f,
            stabilityRating = 0f,
            audioRating = 0f,
            visualRating = 0f,
            customFields = emptyMap(),
            userId = "",
            username = "",
            isVerified = false,
            likeCount = 0,
            dislikeCount = 0,
            commentCount = 0,
            createdAt = java.lang.System.currentTimeMillis(),
            updatedAt = java.lang.System.currentTimeMillis()
        )
    }
}

/**
 * PC Configuration Listing - maintains the existing structure
 */
data class PcListing(
    val id: String,
    val gameId: String,
    val userId: String,
    val cpuId: String,
    val gpuId: String,
    val emulatorId: String,
    val memorySize: Int, // RAM in GB
    val performanceId: String,
    val description: String,
    val screenshotUrls: List<String>,
    val customFieldValues: Map<String, String>, // Field ID to value
    val approvalStatus: ApprovalStatus,
    val isVerified: Boolean, // Verified by emulator developer
    val score: Int, // Calculated from votes
    val totalVotes: Int,
    val createdAt: Long,
    val updatedAt: Long
) {
    val performanceRating: PerformanceScale get() = 
        PerformanceScale.fromId(performanceId.toIntOrNull() ?: 1)
    
    val isApproved: Boolean get() = approvalStatus == ApprovalStatus.APPROVED
    
    val hasScreenshots: Boolean get() = screenshotUrls.isNotEmpty()
    
    val hasCustomSettings: Boolean get() = customFieldValues.isNotEmpty()
}