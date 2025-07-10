package com.emuready.emuready.domain.usecases

import androidx.paging.PagingData
import com.emuready.emuready.domain.entities.*
import com.emuready.emuready.domain.repositories.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Get paginated notifications
 */
class GetNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(unreadOnly: Boolean = false): Flow<PagingData<Notification>> {
        return notificationRepository.getNotifications(unreadOnly)
    }
}

/**
 * Get unread notification count
 */
class GetUnreadNotificationCountUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(): Result<Int> {
        return notificationRepository.getUnreadCount()
    }
}

/**
 * Mark notification as read
 */
class MarkNotificationAsReadUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: String): Result<Unit> {
        return notificationRepository.markAsRead(notificationId)
    }
}

/**
 * Mark all notifications as read
 */
class MarkAllNotificationsAsReadUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return notificationRepository.markAllAsRead()
    }
}

/**
 * Delete notification
 */
class DeleteNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: String): Result<Unit> {
        return notificationRepository.deleteNotification(notificationId)
    }
}

/**
 * Clear all notifications
 */
class ClearAllNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return notificationRepository.clearAllNotifications()
    }
}

/**
 * Get notification preferences
 */
class GetNotificationPreferencesUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(): Result<NotificationPreferences> {
        return notificationRepository.getNotificationPreferences()
    }
}

/**
 * Update notification preferences
 */
class UpdateNotificationPreferencesUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(preferences: NotificationPreferences): Result<Unit> {
        return notificationRepository.updateNotificationPreferences(preferences)
    }
}

/**
 * Subscribe to push notifications
 */
class SubscribeToPushNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(fcmToken: String): Result<Unit> {
        return notificationRepository.subscribeToPushNotifications(fcmToken)
    }
}

/**
 * Unsubscribe from push notifications
 */
class UnsubscribeFromPushNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return notificationRepository.unsubscribeFromPushNotifications()
    }
}