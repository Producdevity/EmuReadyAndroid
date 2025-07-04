package com.emuready.emuready.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.emuready.emuready.domain.entities.ThemeMode
import com.emuready.emuready.domain.entities.UserPreferences
import com.emuready.emuready.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesRepository {
    
    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val AUTO_LAUNCH_EMULATOR = booleanPreferencesKey("auto_launch_emulator")
    }
    
    override val userPreferences: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(androidx.datastore.preferences.core.emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserPreferences(
                themeMode = ThemeMode.valueOf(
                    preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
                ),
                language = preferences[PreferencesKeys.LANGUAGE] ?: "en",
                notificationsEnabled = preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true,
                autoLaunchEmulator = preferences[PreferencesKeys.AUTO_LAUNCH_EMULATOR] ?: false
            )
        }
    
    override suspend fun updateThemeMode(themeMode: String): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.THEME_MODE] = themeMode
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateLanguage(language: String): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.LANGUAGE] = language
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateNotificationsEnabled(enabled: Boolean): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateAutoLaunchEmulator(enabled: Boolean): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.AUTO_LAUNCH_EMULATOR] = enabled
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}