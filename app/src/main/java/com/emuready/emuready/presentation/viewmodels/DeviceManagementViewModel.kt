package com.emuready.emuready.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emuready.emuready.core.utils.Result
import com.emuready.emuready.data.services.DeviceManager
import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.domain.usecases.device.ClearDeviceDataUseCase
import com.emuready.emuready.domain.usecases.device.DetectDeviceUseCase
import com.emuready.emuready.domain.usecases.device.GetCurrentDeviceUseCase
import com.emuready.emuready.domain.usecases.device.GetDeviceHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceManagementViewModel @Inject constructor(
    private val detectDeviceUseCase: DetectDeviceUseCase,
    private val getCurrentDeviceUseCase: GetCurrentDeviceUseCase,
    private val getDeviceHistoryUseCase: GetDeviceHistoryUseCase,
    private val clearDeviceDataUseCase: ClearDeviceDataUseCase,
    private val deviceManager: DeviceManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DeviceManagementUiState())
    val uiState: StateFlow<DeviceManagementUiState> = _uiState.asStateFlow()
    
    init {
        observeDeviceChanges()
    }
    
    private fun observeDeviceChanges() {
        viewModelScope.launch {
            combine(
                getCurrentDeviceUseCase(),
                getDeviceHistoryUseCase()
            ) { currentDevice, detectedDevices ->
                val compatibilityInfo = currentDevice?.let { device ->
                    deviceManager.getDeviceCompatibilityInfo(device)
                }
                
                _uiState.value = _uiState.value.copy(
                    currentDevice = currentDevice,
                    detectedDevices = detectedDevices,
                    compatibilityInfo = compatibilityInfo,
                    isLoading = false
                )
            }.collect { }
        }
    }
    
    fun loadDeviceInfo() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // If no device is detected or detection is stale, refresh
                if (deviceManager.getCurrentDevice() == null || deviceManager.isDeviceDetectionStale()) {
                    deviceManager.detectAndSaveCurrentDevice()
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load device info: ${e.message}"
                )
            }
        }
    }
    
    fun refreshDeviceInfo() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            detectDeviceUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }
    
    fun clearDeviceData() {
        viewModelScope.launch {
            clearDeviceDataUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            currentDevice = null,
                            detectedDevices = emptyList(),
                            compatibilityInfo = null,
                            error = null,
                            isLoading = false
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class DeviceManagementUiState(
    val currentDevice: Device? = null,
    val detectedDevices: List<Device> = emptyList(),
    val compatibilityInfo: DeviceManager.DeviceCompatibilityInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)