package com.emuready.emuready.domain.repositories

import androidx.paging.PagingData
import com.emuready.emuready.domain.entities.Notification
import com.emuready.emuready.domain.entities.NotificationPreferences
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    // Basic notification operations
    fun getNotifications(unreadOnly: Boolean = false): Flow<PagingData<Notification>>
    
    suspend fun getUnreadCount(): Result<Int>
    
    suspend fun markAsRead(notificationId: String): Result<Unit>
    
    suspend fun markAllAsRead(): Result<Unit>
    
    suspend fun deleteNotification(notificationId: String): Result<Unit>
    
    suspend fun clearAllNotifications(): Result<Unit>
    
    // Notification preferences
    suspend fun getNotificationPreferences(): Result<NotificationPreferences>
    
    suspend fun updateNotificationPreferences(preferences: NotificationPreferences): Result<Unit>
    
    // Push notification management
    suspend fun subscribeToPushNotifications(fcmToken: String): Result<Unit>
    
    suspend fun unsubscribeFromPushNotifications(): Result<Unit>
    
    // Local notification handling
    suspend fun createLocalNotification(
        title: String,
        message: String,
        type: com.emuready.emuready.domain.entities.NotificationType,
        targetId: String? = null,
        targetType: String? = null
    ): Result<Unit>
    
    suspend fun scheduleNotification(
        title: String,
        message: String,
        type: com.emuready.emuready.domain.entities.NotificationType,
        delayMillis: Long
    ): Result<Unit>
}