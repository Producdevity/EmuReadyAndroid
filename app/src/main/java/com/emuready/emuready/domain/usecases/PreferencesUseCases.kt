package com.emuready.emuready.domain.usecases

import com.emuready.emuready.domain.entities.UserPreferences
import com.emuready.emuready.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserPreferencesUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<UserPreferences> {
        return preferencesRepository.userPreferences
    }
}

class UpdateThemeModeUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(themeMode: String): Result<Unit> {
        return preferencesRepository.updateThemeMode(themeMode)
    }
}

class UpdateLanguageUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(language: String): Result<Unit> {
        return preferencesRepository.updateLanguage(language)
    }
}

class UpdateNotificationsEnabledUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(enabled: Boolean): Result<Unit> {
        return preferencesRepository.updateNotificationsEnabled(enabled)
    }
}

class UpdateAutoLaunchEmulatorUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(enabled: Boolean): Result<Unit> {
        return preferencesRepository.updateAutoLaunchEmulator(enabled)
    }
}