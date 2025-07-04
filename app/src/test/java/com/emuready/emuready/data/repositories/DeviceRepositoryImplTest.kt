package com.emuready.emuready.data.repositories

import com.emuready.emuready.data.services.DeviceManager
import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.domain.entities.DeviceType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeviceRepositoryImplTest {

    private lateinit var deviceManager: DeviceManager
    private lateinit var deviceRepository: DeviceRepositoryImpl

    @Before
    fun setUp() {
        deviceManager = mockk()
        deviceRepository = DeviceRepositoryImpl(deviceManager)
    }

    @Test
    fun `detectCurrentDevice should call deviceManager detectAndSaveCurrentDevice`() = runTest {
        // Given
        val expectedDevice = createTestDevice()
        coEvery { deviceManager.detectAndSaveCurrentDevice() } returns expectedDevice

        // When
        val result = deviceRepository.detectCurrentDevice()

        // Then
        assertEquals(expectedDevice, result)
        coVerify { deviceManager.detectAndSaveCurrentDevice() }
    }

    @Test
    fun `getCurrentDevice should return deviceManager currentDevice flow`() = runTest {
        // Given
        val expectedDevice = createTestDevice()
        every { deviceManager.currentDevice } returns flowOf<Device?>(expectedDevice)

        // When
        val result = deviceRepository.getCurrentDevice().first()

        // Then
        assertEquals(expectedDevice, result)
    }

    @Test
    fun `getDetectedDevices should return deviceManager detectedDevices flow`() = runTest {
        // Given
        val expectedDevices = listOf(createTestDevice())
        every { deviceManager.detectedDevices } returns flowOf<List<Device>>(expectedDevices)

        // When
        val result = deviceRepository.getDetectedDevices().first()

        // Then
        assertEquals(expectedDevices, result)
    }

    @Test
    fun `clearDeviceData should call deviceManager clearDeviceData`() = runTest {
        // Given
        every { deviceManager.clearDeviceData() } returns Unit

        // When
        deviceRepository.clearDeviceData()

        // Then
        verify { deviceManager.clearDeviceData() }
    }

    @Test
    fun `isDeviceDetectionStale should return deviceManager isDeviceDetectionStale result`() {
        // Given
        every { deviceManager.isDeviceDetectionStale() } returns true

        // When
        val result = deviceRepository.isDeviceDetectionStale()

        // Then
        assertTrue(result)
        verify { deviceManager.isDeviceDetectionStale() }
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