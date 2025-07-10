package com.emuready.emuready.domain.entities

/**
 * User notification entity
 */
data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val isRead: Boolean,
    val createdAt: Long,
    val targetId: String? = null, // ID of related entity (listing, game, etc.)
    val targetType: String? = null, // Type of related entity
    val actionUrl: String? = null // Deep link URL
) {
    val isRecent: Boolean get() = java.lang.System.currentTimeMillis() - createdAt < 24 * 60 * 60 * 1000L // 24 hours
    
    val timeAgo: String get() {
        val diff = java.lang.System.currentTimeMillis() - createdAt
        val minutes = diff / (1000L * 60L)
        val hours = diff / (1000L * 60L * 60L)
        val days = diff / (1000L * 60L * 60L * 24L)
        
        return when {
            minutes < 1L -> "Just now"
            minutes < 60L -> "${minutes}m ago"
            hours < 24L -> "${hours}h ago"
            days < 30L -> "${days}d ago"
            else -> "${days / 30L}mo ago"
        }
    }
}

/**
 * Notification types
 */
enum class NotificationType(val displayName: String, val icon: String) {
    INFO("Info", "info"),
    SUCCESS("Success", "check_circle"),
    WARNING("Warning", "warning"),
    ERROR("Error", "error"),
    LISTING_APPROVED("Listing Approved", "thumb_up"),
    LISTING_REJECTED("Listing Rejected", "thumb_down"),
    NEW_COMMENT("New Comment", "comment"),
    LISTING_VOTED("Listing Voted", "how_to_vote"),
    DEVICE_VERIFIED("Device Verified", "verified"),
    SYSTEM_UPDATE("System Update", "system_update"),
    ACHIEVEMENT("Achievement", "emoji_events"),
    COMMUNITY("Community", "group")
}

/**
 * Notification preferences
 */
data class NotificationPreferences(
    val pushNotificationsEnabled: Boolean = true,
    val emailNotificationsEnabled: Boolean = true,
    val listingUpdatesEnabled: Boolean = true,
    val commentNotificationsEnabled: Boolean = true,
    val voteNotificationsEnabled: Boolean = true,
    val systemNotificationsEnabled: Boolean = true,
    val marketingNotificationsEnabled: Boolean = false,
    val quietHoursEnabled: Boolean = false,
    val quietHoursStart: String = "22:00", // HH:mm format
    val quietHoursEnd: String = "08:00" // HH:mm format
) {
    fun isNotificationAllowed(type: NotificationType): Boolean {
        return when (type) {
            NotificationType.LISTING_APPROVED,
            NotificationType.LISTING_REJECTED -> listingUpdatesEnabled
            NotificationType.NEW_COMMENT -> commentNotificationsEnabled
            NotificationType.LISTING_VOTED -> voteNotificationsEnabled
            NotificationType.SYSTEM_UPDATE,
            NotificationType.INFO,
            NotificationType.WARNING,
            NotificationType.ERROR -> systemNotificationsEnabled
            else -> true
        }
    }
}