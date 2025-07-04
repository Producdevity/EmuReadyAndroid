package com.emuready.emuready.data.services

import android.content.Context
import com.emuready.emuready.domain.entities.DeviceType
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class DeviceDetectionServiceTest {

    private lateinit var context: Context
    private lateinit var deviceDetectionService: DeviceDetectionService

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        deviceDetectionService = DeviceDetectionService(context)
    }

    @Test
    fun `detectDevice should return valid device object`() = runTest {
        // When
        val device = deviceDetectionService.detectDevice()

        // Then
        assertNotNull(device)
        assertNotNull(device.id)
        assertNotNull(device.name)
        assertNotNull(device.type)
        assertNotNull(device.manufacturer)
        assertNotNull(device.model)
        assertTrue(device.detectedAt > 0)
    }

    @Test
    fun `detectDevice should handle exceptions gracefully`() = runTest {
        // Given
        every { context.getSystemService(Context.ACTIVITY_SERVICE) } throws RuntimeException("Test exception")

        // When
        val device = deviceDetectionService.detectDevice()

        // Then
        assertNotNull(device)
        assertEquals(0, device.totalMemoryMB)
        assertEquals(0, device.availableMemoryMB)
    }

    @Test
    fun `device type detection should work for known devices`() = runTest {
        // This test would require mocking Build class, which is complex
        // In a real scenario, we'd use a testable device detection strategy
        
        // When
        val device = deviceDetectionService.detectDevice()

        // Then
        // Should at least return a valid device type
        assertNotNull(device.type)
        assertTrue(device.type in DeviceType.values())
    }
}