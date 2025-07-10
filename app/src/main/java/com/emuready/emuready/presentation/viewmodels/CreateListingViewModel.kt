package com.emuready.emuready.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emuready.emuready.domain.entities.*
import com.emuready.emuready.domain.usecases.CreateListingUseCase
import com.emuready.emuready.domain.usecases.GetGameDetailUseCase
import com.emuready.emuready.domain.usecases.GetCpusUseCase
import com.emuready.emuready.domain.usecases.GetGpusUseCase
import com.emuready.emuready.domain.usecases.SearchGamesUseCase
import com.emuready.emuready.domain.usecases.GetDevicesUseCase
import com.emuready.emuready.domain.usecases.GetEmulatorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MobileDeviceFormData(
    val deviceId: String? = null,
    val emulatorId: String? = null,
    val customDeviceName: String = "",
    val customDeviceSpecs: String = ""
)

data class PCConfigurationFormData(
    val cpu: String = "",
    val gpu: String = "",
    val ram: String = "",
    val operatingSystem: String = "",
    val emulatorVersion: String = ""
)

data class CreateListingUiState(
    val isLoading: Boolean = false,
    val selectedGame: Game? = null,
    val listingType: ListingType? = null,
    val performanceLevel: PerformanceLevel? = null,
    val emulatorConfig: String = "",
    val notes: String = "",
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val showAuthRequired: Boolean = false,
    
    // Form data
    val mobileDeviceForm: MobileDeviceFormData = MobileDeviceFormData(),
    val pcConfigurationForm: PCConfigurationFormData = PCConfigurationFormData(),
    
    // Search and selection data
    val isSearchingGames: Boolean = false,
    val gameSearchResults: List<Game> = emptyList(),
    val availableDevices: List<Device> = emptyList(),
    val availableEmulators: List<Emulator> = emptyList(),
    val availableCpus: List<Cpu> = emptyList(),
    val availableGpus: List<Gpu> = emptyList()
) {
    fun canShowPerformanceForm(): Boolean {
        return selectedGame != null && listingType != null
    }
    
    fun canSubmit(): Boolean {
        return selectedGame != null && 
               listingType != null && 
               performanceLevel != null &&
               when (listingType) {
                   ListingType.MOBILE_DEVICE -> mobileDeviceForm.deviceId != null || mobileDeviceForm.customDeviceName.isNotEmpty()
                   ListingType.PC_CONFIGURATION -> pcConfigurationForm.cpu.isNotEmpty() && pcConfigurationForm.gpu.isNotEmpty()
                   ListingType.UNKNOWN -> false
               }
    }
}

@HiltViewModel
class CreateListingViewModel @Inject constructor(
    private val searchGamesUseCase: SearchGamesUseCase,
    private val getGameDetailUseCase: GetGameDetailUseCase,
    private val createListingUseCase: CreateListingUseCase,
    private val getDevicesUseCase: GetDevicesUseCase,
    private val getEmulatorsUseCase: GetEmulatorsUseCase,
    private val getCpusUseCase: GetCpusUseCase,
    private val getGpusUseCase: GetGpusUseCase,
    private val authRepository: com.emuready.emuready.domain.repositories.AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CreateListingUiState())
    val uiState: StateFlow<CreateListingUiState> = _uiState.asStateFlow()
    
    init {
        loadInitialData()
        observeAuthState()
    }
    
    private fun observeAuthState() {
        authRepository.authState
            .onEach { authState ->
                _uiState.value = _uiState.value.copy(
                    isAuthenticated = authState is AuthState.Authenticated
                )
            }
            .launchIn(viewModelScope)
    }
    
    private fun loadInitialData() {
        viewModelScope.launch {
            // Load available devices and emulators
            getDevicesUseCase().fold(
                onSuccess = { devices ->
                    _uiState.value = _uiState.value.copy(availableDevices = devices)
                },
                onFailure = { /* Handle error silently */ }
            )
            
            getEmulatorsUseCase().fold(
                onSuccess = { emulators ->
                    _uiState.value = _uiState.value.copy(availableEmulators = emulators)
                },
                onFailure = { /* Handle error silently */ }
            )

            getCpusUseCase().fold(
                onSuccess = { cpus ->
                    _uiState.value = _uiState.value.copy(availableCpus = cpus)
                },
                onFailure = { /* Handle error silently */ }
            )

            getGpusUseCase().fold(
                onSuccess = { gpus ->
                    _uiState.value = _uiState.value.copy(availableGpus = gpus)
                },
                onFailure = { /* Handle error silently */ }
            )
        }
    }
    
    fun selectGame(gameId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            getGameDetailUseCase(gameId).fold(
                onSuccess = { gameDetail ->
                    _uiState.value = _uiState.value.copy(
                        selectedGame = gameDetail.game,
                        isLoading = false,
                        gameSearchResults = emptyList() // Clear search results
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message,
                        isLoading = false
                    )
                }
            )
        }
    }
    
    fun searchGames(query: String) {
        if (query.trim().length < 2) {
            _uiState.value = _uiState.value.copy(gameSearchResults = emptyList())
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearchingGames = true)

            searchGamesUseCase(query).fold(
                onSuccess = { games ->
                    _uiState.value = _uiState.value.copy(
                        gameSearchResults = games,
                        isSearchingGames = false
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message,
                        isSearchingGames = false
                    )
                }
            )
        }
    }
    
    fun selectListingType(type: ListingType) {
        _uiState.value = _uiState.value.copy(listingType = type)
    }
    
    fun selectPerformanceLevel(level: PerformanceLevel) {
        _uiState.value = _uiState.value.copy(performanceLevel = level)
    }
    
    fun updateEmulatorConfig(config: String) {
        _uiState.value = _uiState.value.copy(emulatorConfig = config)
    }
    
    fun updateNotes(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }
    
    fun updateMobileDeviceForm(update: (MobileDeviceFormData) -> MobileDeviceFormData) {
        _uiState.value = _uiState.value.copy(
            mobileDeviceForm = update(_uiState.value.mobileDeviceForm)
        )
    }
    
    fun updatePCConfigurationForm(update: (PCConfigurationFormData) -> PCConfigurationFormData) {
        _uiState.value = _uiState.value.copy(
            pcConfigurationForm = update(_uiState.value.pcConfigurationForm)
        )
    }
    
    fun submitListing(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentState = _uiState.value
        
        // Check authentication first
        if (!currentState.isAuthenticated) {
            _uiState.value = _uiState.value.copy(showAuthRequired = true)
            return
        }
        
        if (!currentState.canSubmit()) {
            onError("Please fill in all required fields")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true)
            
            try {
                val form = createFormFromCurrentState(currentState)
                
                createListingUseCase(form).fold(
                    onSuccess = {
                        _uiState.value = _uiState.value.copy(isSubmitting = false)
                        onSuccess()
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isSubmitting = false,
                            error = error.message
                        )
                        onError(error.message ?: "Failed to create listing")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    error = e.message
                )
                onError(e.message ?: "Failed to create listing")
            }
        }
    }
    
    fun dismissAuthRequired() {
        _uiState.value = _uiState.value.copy(showAuthRequired = false)
    }
    
    private fun createFormFromCurrentState(state: CreateListingUiState): CreateListingForm {
        val game = state.selectedGame!!
        val listingType = state.listingType!!
        val performanceLevel = state.performanceLevel!!
        
        return when (listingType) {
            ListingType.MOBILE_DEVICE -> {
                val deviceForm = state.mobileDeviceForm
                CreateListingForm(
                    gameId = game.id,
                    deviceId = deviceForm.deviceId ?: "",
                    emulatorId = deviceForm.emulatorId ?: "",
                    performanceRating = performanceLevel.value,
                    description = state.notes,
                    customSettings = mapOf(
                        "customDeviceName" to deviceForm.customDeviceName,
                        "customDeviceSpecs" to deviceForm.customDeviceSpecs
                    ),
                    type = ListingType.MOBILE_DEVICE
                )
            }
            ListingType.PC_CONFIGURATION -> {
                val pcForm = state.pcConfigurationForm
                CreateListingForm(
                    gameId = game.id,
                    deviceId = null,
                    emulatorId = "",
                    performanceRating = performanceLevel.value,
                    description = state.notes,
                    customSettings = mapOf(
                        "cpu" to pcForm.cpu,
                        "gpu" to pcForm.gpu,
                        "ram" to pcForm.ram,
                        "operatingSystem" to pcForm.operatingSystem,
                        "emulatorVersion" to pcForm.emulatorVersion
                    ),
                    type = ListingType.PC_CONFIGURATION,
                    cpuId = "amd-ryzen-7000",
                    gpuId = "nvidia-rtx-4070",
                    memorySize = 16
                )
            }
            ListingType.UNKNOWN -> throw IllegalArgumentException("Unsupported listing type: $listingType")
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun resetForm() {
        _uiState.value = CreateListingUiState(
            availableDevices = _uiState.value.availableDevices,
            availableEmulators = _uiState.value.availableEmulators
        )
    }
}

private fun PerformanceLevel.toRating(): Float {
    return when (this) {
        PerformanceLevel.PERFECT -> 1.0f
        PerformanceLevel.GREAT -> 0.8f
        PerformanceLevel.GOOD -> 0.6f
        PerformanceLevel.PLAYABLE -> 0.4f
        PerformanceLevel.POOR -> 0.2f
        PerformanceLevel.UNPLAYABLE -> 0.0f
    }
}