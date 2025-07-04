package com.emuready.emuready.presentation.viewmodels

import com.emuready.emuready.core.utils.Result
import com.emuready.emuready.data.services.DeviceManager
import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.domain.entities.DeviceType
import com.emuready.emuready.domain.usecases.device.ClearDeviceDataUseCase
import com.emuready.emuready.domain.usecases.device.DetectDeviceUseCase
import com.emuready.emuready.domain.usecases.device.GetCurrentDeviceUseCase
import com.emuready.emuready.domain.usecases.device.GetDeviceHistoryUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeviceManagementViewModelTest {

    private lateinit var detectDeviceUseCase: DetectDeviceUseCase
    private lateinit var getCurrentDeviceUseCase: GetCurrentDeviceUseCase
    private lateinit var getDeviceHistoryUseCase: GetDeviceHistoryUseCase
    private lateinit var clearDeviceDataUseCase: ClearDeviceDataUseCase
    private lateinit var deviceManager: DeviceManager
    private lateinit var viewModel: DeviceManagementViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        detectDeviceUseCase = mockk()
        getCurrentDeviceUseCase = mockk()
        getDeviceHistoryUseCase = mockk()
        clearDeviceDataUseCase = mockk()
        deviceManager = mockk()

        every { getCurrentDeviceUseCase() } returns flowOf(null)
        every { getDeviceHistoryUseCase() } returns flowOf(emptyList())

        viewModel = DeviceManagementViewModel(
            detectDeviceUseCase,
            getCurrentDeviceUseCase,
            getDeviceHistoryUseCase,
            clearDeviceDataUseCase,
            deviceManager
        )
    }

    @Test
    fun `initial state should be correct`() = runTest {
        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertNull(uiState.currentDevice)
        assertTrue(uiState.detectedDevices.isEmpty())
        assertNull(uiState.compatibilityInfo)
        assertFalse(uiState.isLoading)
        assertNull(uiState.error)
    }

    @Test
    fun `refreshDeviceInfo should update state correctly on success`() = runTest {
        // Given
        val testDevice = createTestDevice()
        coEvery { detectDeviceUseCase() } returns flowOf(
            Result.Loading,
            Result.Success(testDevice)
        )

        // When
        viewModel.refreshDeviceInfo()
        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertNull(uiState.error)
    }

    @Test
    fun `refreshDeviceInfo should handle error correctly`() = runTest {
        // Given
        val errorMessage = "Detection failed"
        coEvery { detectDeviceUseCase() } returns flowOf(
            Result.Loading,
            Result.Error(errorMessage)
        )

        // When
        viewModel.refreshDeviceInfo()
        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(errorMessage, uiState.error)
    }

    @Test
    fun `clearDeviceData should update state correctly on success`() = runTest {
        // Given
        coEvery { clearDeviceDataUseCase() } returns flowOf(
            Result.Loading,
            Result.Success(Unit)
        )

        // When
        viewModel.clearDeviceData()
        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertNull(uiState.currentDevice)
        assertTrue(uiState.detectedDevices.isEmpty())
        assertNull(uiState.compatibilityInfo)
        assertNull(uiState.error)
    }

    @Test
    fun `clearError should clear error state`() = runTest {
        // Given - Set error state first
        coEvery { detectDeviceUseCase() } returns flowOf(
            Result.Error("Test error")
        )
        viewModel.refreshDeviceInfo()
        advanceUntilIdle()

        // When
        viewModel.clearError()

        // Then
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `state should update when current device changes`() = runTest {
        // Given
        val testDevice = createTestDevice()
        val compatibilityInfo = DeviceManager.DeviceCompatibilityInfo(
            isCompatible = true,
            compatibilityScore = 95,
            recommendedSettings = mapOf("resolution" to "1280x800"),
            limitations = emptyList(),
            notes = "Excellent compatibility"
        )

        every { getCurrentDeviceUseCase() } returns flowOf(testDevice)
        every { deviceManager.getDeviceCompatibilityInfo(testDevice) } returns compatibilityInfo

        // When
        val newViewModel = DeviceManagementViewModel(
            detectDeviceUseCase,
            getCurrentDeviceUseCase,
            getDeviceHistoryUseCase,
            clearDeviceDataUseCase,
            deviceManager
        )
        advanceUntilIdle()

        // Then
        val uiState = newViewModel.uiState.value
        assertEquals(testDevice, uiState.currentDevice)
        assertNotNull(uiState.compatibilityInfo)
        assertEquals(95, uiState.compatibilityInfo?.compatibilityScore)
    }

    private fun createTestDevice(): Device {
        return Device(
            id = "test_device",
            name = "Test Device",
            type = DeviceType.STEAM_DECK,
            manufacturer = "Valve",
            model = "Steam Deck",
            cpuArchitecture = "x86_64",
            cpuInfo = "AMD Custom APU",
            totalMemoryMB = 16384,
            availableMemoryMB = 12000,
            androidVersion = "13",
            apiLevel = 33,
            isEmulatorCompatible = true,
            detectedAt = System.currentTimeMillis()
        )
    }
}