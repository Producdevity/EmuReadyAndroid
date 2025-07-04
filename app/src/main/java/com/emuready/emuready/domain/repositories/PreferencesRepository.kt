package com.emuready.emuready.domain.repositories

import com.emuready.emuready.domain.entities.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val userPreferences: Flow<UserPreferences>
    
    suspend fun updateThemeMode(themeMode: String): Result<Unit>
    
    suspend fun updateLanguage(language: String): Result<Unit>
    
    suspend fun updateNotificationsEnabled(enabled: Boolean): Result<Unit>
    
    suspend fun updateAutoLaunchEmulator(enabled: Boolean): Result<Unit>
}