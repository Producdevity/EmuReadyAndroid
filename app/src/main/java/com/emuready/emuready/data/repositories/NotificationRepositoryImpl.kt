package com.emuready.emuready.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcRequestBuilder
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
import com.emuready.emuready.domain.entities.*
import com.emuready.emuready.domain.exceptions.ApiException
import com.emuready.emuready.domain.exceptions.NetworkException
import com.emuready.emuready.domain.repositories.NotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val trpcApiService: EmuReadyTrpcApiService
) : NotificationRepository {
    
    private val requestBuilder = TrpcRequestBuilder()
    
    override fun getNotifications(unreadOnly: Boolean): Flow<PagingData<Notification>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                // NotificationsPagingSource will be implemented when notifications API is ready
                // For now, return empty paging source
                androidx.paging.PagingSource.LoadResult.Page(
                    data = emptyList<Notification>(),
                    prevKey = null,
                    nextKey = null
                ).let { result ->
                    object : androidx.paging.PagingSource<Int, Notification>() {
                        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
                            return LoadResult.Page(
                                data = emptyList(),
                                prevKey = null,
                                nextKey = null
                            )
                        }
                        override fun getRefreshKey(state: androidx.paging.PagingState<Int, Notification>): Int? = null
                    }
                }
            }
        ).flow
    }
    
    override suspend fun getUnreadCount(): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val responseWrapper = trpcApiService.getUnreadNotificationCount()
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                Result.success(response.result.data.count)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun markAsRead(notificationId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildRequest(TrpcRequestDtos.MarkNotificationReadSchema(notificationId = notificationId))
            val responseWrapper = trpcApiService.markNotificationAsRead(request)
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else {
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun markAllAsRead(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val responseWrapper = trpcApiService.markAllNotificationsAsRead()
            val response = responseWrapper
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else {
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun deleteNotification(notificationId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // API doesn't have delete notification endpoint, so mark as read
            markAsRead(notificationId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun clearAllNotifications(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Mark all as read since there's no clear endpoint
            markAllAsRead()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getNotificationPreferences(): Result<NotificationPreferences> = withContext(Dispatchers.IO) {
        try {
            // This would be stored locally or fetched from user preferences
            // For now, return default preferences
            val defaultPreferences = NotificationPreferences()
            Result.success(defaultPreferences)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateNotificationPreferences(preferences: NotificationPreferences): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // This would be stored locally or sent to user preferences API
            // For now, just return success
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun subscribeToPushNotifications(fcmToken: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // This would send the FCM token to the backend
            // For now, just return success
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun unsubscribeFromPushNotifications(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // This would remove the FCM token from the backend
            // For now, just return success
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun createLocalNotification(
        title: String,
        message: String,
        type: NotificationType,
        targetId: String?,
        targetType: String?
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // This would create a local notification
            // Implementation would involve Android NotificationManager
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun scheduleNotification(
        title: String,
        message: String,
        type: NotificationType,
        delayMillis: Long
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // This would schedule a notification using WorkManager or AlarmManager
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}