package com.emuready.emuready.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emuready.emuready.presentation.ui.theme.ThemeManager
import com.emuready.emuready.presentation.ui.theme.ThemeMode
import com.emuready.emuready.presentation.ui.theme.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeManager: ThemeManager
) : ViewModel() {
    
    val themePreferences: StateFlow<ThemePreferences> = themeManager.themePreferences
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemePreferences()
        )
    
    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            themeManager.setThemeMode(mode)
        }
    }
    
    fun setDynamicColors(enabled: Boolean) {
        viewModelScope.launch {
            themeManager.setDynamicColors(enabled)
        }
    }
}