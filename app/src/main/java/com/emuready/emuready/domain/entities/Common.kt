package com.emuready.emuready.domain.entities

data class PaginatedResponse<T>(
    val data: List<T>,
    val page: Int,
    val totalPages: Int,
    val totalItems: Int,
    val hasNext: Boolean
)

enum class GameSortOption {
    TITLE, RATING, DATE, POPULARITY
}

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val language: String = "en",
    val notificationsEnabled: Boolean = true,
    val autoLaunchEmulator: Boolean = false
)

sealed class SettingsCategory {
    object Account : SettingsCategory()
    object Devices : SettingsCategory()
    object Notifications : SettingsCategory()
    object Appearance : SettingsCategory()
    object Privacy : SettingsCategory()
    object About : SettingsCategory()
}