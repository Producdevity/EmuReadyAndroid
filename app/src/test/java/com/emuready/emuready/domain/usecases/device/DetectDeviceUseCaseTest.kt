package com.emuready.emuready.domain.usecases.device

import com.emuready.emuready.core.utils.Result
import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.domain.entities.DeviceType
import com.emuready.emuready.domain.repositories.DeviceRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DetectDeviceUseCaseTest {

    private lateinit var deviceRepository: DeviceRepository
    private lateinit var detectDeviceUseCase: DetectDeviceUseCase

    @Before
    fun setUp() {
        deviceRepository = mockk()
        detectDeviceUseCase = DetectDeviceUseCase(deviceRepository)
    }

    @Test
    fun `invoke should emit loading then success when device detection succeeds`() = runTest {
        // Given
        val expectedDevice = Device(
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

        coEvery { deviceRepository.detectCurrentDevice() } returns expectedDevice

        // When
        val results = detectDeviceUseCase().toList()

        // Then
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Loading)
        assertTrue(results[1] is Result.Success)
        assertEquals(expectedDevice, (results[1] as Result.Success).data)
    }

    @Test
    fun `invoke should emit loading then error when device detection fails`() = runTest {
        // Given
        val errorMessage = "Device detection failed"
        coEvery { deviceRepository.detectCurrentDevice() } throws Exception(errorMessage)

        // When
        val results = detectDeviceUseCase().toList()

        // Then
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Loading)
        assertTrue(results[1] is Result.Error)
        assertEquals(errorMessage, (results[1] as Result.Error).message)
    }

    @Test
    fun `invoke should handle unknown exceptions gracefully`() = runTest {
        // Given
        coEvery { deviceRepository.detectCurrentDevice() } throws Exception()

        // When
        val results = detectDeviceUseCase().toList()

        // Then
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Loading)
        assertTrue(results[1] is Result.Error)
        assertEquals("Unknown error occurred", (results[1] as Result.Error).message)
    }
}