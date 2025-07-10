package com.emuready.emuready.presentation.ui.theme

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

data class ThemePreferences(
    val isDarkMode: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val useDynamicColors: Boolean = true
)

val LocalThemePreferences = compositionLocalOf { ThemePreferences() }

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

@Singleton
class ThemeManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    private val THEME_MODE = booleanPreferencesKey("theme_mode_system") // simplified to boolean for now
    private val USE_DYNAMIC_COLORS = booleanPreferencesKey("use_dynamic_colors")
    
    val themePreferences: Flow<ThemePreferences> = context.dataStore.data.map { preferences ->
        ThemePreferences(
            isDarkMode = preferences[IS_DARK_MODE] ?: false,
            themeMode = if (preferences[THEME_MODE] != false) ThemeMode.SYSTEM else ThemeMode.LIGHT,
            useDynamicColors = preferences[USE_DYNAMIC_COLORS] ?: true
        )
    }
    
    suspend fun setDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = isDark
            preferences[THEME_MODE] = false // Disable system mode when manually set
        }
    }
    
    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE] = mode == ThemeMode.SYSTEM
            when (mode) {
                ThemeMode.LIGHT -> preferences[IS_DARK_MODE] = false
                ThemeMode.DARK -> preferences[IS_DARK_MODE] = true
                ThemeMode.SYSTEM -> {
                    // Will use system preference
                }
            }
        }
    }
    
    suspend fun setDynamicColors(useDynamic: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USE_DYNAMIC_COLORS] = useDynamic
        }
    }
}