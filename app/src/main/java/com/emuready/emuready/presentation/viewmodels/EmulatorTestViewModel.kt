package com.emuready.emuready.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emuready.emuready.core.services.EdenEmulatorService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EmulatorTestState(
    val isEdenInstalled: Boolean = false,
    val titleId: String = "",
    val selectedPreset: String = "Known Working Config",
    val availablePresets: List<String> = emptyList(),
    val isLaunching: Boolean = false,
    val testResults: List<String> = emptyList(),
    val launchResult: Result<Unit>? = null
)

@HiltViewModel
class EmulatorTestViewModel @Inject constructor(
    private val edenEmulatorService: EdenEmulatorService
) : ViewModel() {
    
    private val _state = MutableStateFlow(EmulatorTestState())
    val state: StateFlow<EmulatorTestState> = _state.asStateFlow()
    
    init {
        checkInstallation()
        loadPresets()
    }
    
    fun checkInstallation() {
        viewModelScope.launch {
            val isInstalled = edenEmulatorService.isEdenInstalled()
            _state.value = _state.value.copy(isEdenInstalled = isInstalled)
            
            if (isInstalled) {
                addTestResult("Eden emulator detected successfully")
            } else {
                addTestResult("Eden emulator not found - please install it from the Play Store")
            }
        }
    }
    
    fun updateTitleId(titleId: String) {
        _state.value = _state.value.copy(titleId = titleId)
    }
    
    fun selectPreset(preset: String) {
        _state.value = _state.value.copy(selectedPreset = preset)
        addTestResult("Selected preset: $preset")
    }
    
    fun launchGame() {
        val currentState = _state.value
        if (!currentState.isEdenInstalled || currentState.titleId.isEmpty()) {
            return
        }
        
        viewModelScope.launch {
            _state.value = _state.value.copy(isLaunching = true)
            
            val result = edenEmulatorService.launchGameWithPreset(
                titleId = currentState.titleId,
                presetName = currentState.selectedPreset
            )
            
            _state.value = _state.value.copy(
                isLaunching = false,
                launchResult = result
            )
            
            result.fold(
                onSuccess = {
                    addTestResult("Successfully launched game with Title ID: ${currentState.titleId}")
                },
                onFailure = { error ->
                    addTestResult("Failed to launch game: ${error.message}")
                }
            )
        }
    }
    
    fun clearLaunchResult() {
        _state.value = _state.value.copy(launchResult = null)
    }
    
    private fun loadPresets() {
        viewModelScope.launch {
            val presets = edenEmulatorService.getAvailablePresets()
            _state.value = _state.value.copy(availablePresets = presets)
        }
    }
    
    private fun addTestResult(result: String) {
        val currentResults = _state.value.testResults.toMutableList()
        currentResults.add("${currentResults.size + 1}. $result")
        _state.value = _state.value.copy(testResults = currentResults)
    }
}