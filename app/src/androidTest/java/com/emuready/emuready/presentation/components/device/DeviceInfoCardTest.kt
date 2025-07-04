package com.emuready.emuready.presentation.components.device

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.domain.entities.DeviceType
import com.emuready.emuready.presentation.ui.theme.EmuReadyTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeviceInfoCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun deviceInfoCard_displaysDeviceInformation() {
        // Given
        val testDevice = Device(
            id = "test_device",
            name = "Steam Deck Test",
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

        // When
        composeTestRule.setContent {
            EmuReadyTheme {
                DeviceInfoCard(device = testDevice)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Steam Deck Test").assertIsDisplayed()
        composeTestRule.onNodeWithText("STEAM DECK").assertIsDisplayed()
        composeTestRule.onNodeWithText("Valve").assertIsDisplayed()
        composeTestRule.onNodeWithText("Steam Deck").assertIsDisplayed()
        composeTestRule.onNodeWithText("13 (API 33)").assertIsDisplayed()
        composeTestRule.onNodeWithText("x86_64").assertIsDisplayed()
        composeTestRule.onNodeWithText("Yes").assertIsDisplayed()
    }

    @Test
    fun deviceInfoCard_displaysMemoryInformation() {
        // Given
        val testDevice = Device(
            id = "test_device",
            name = "Test Device",
            type = DeviceType.PHONE_TABLET,
            manufacturer = "Samsung",
            model = "Galaxy S23",
            cpuArchitecture = "arm64-v8a",
            cpuInfo = "Snapdragon 8 Gen 2",
            totalMemoryMB = 8192,
            availableMemoryMB = 6000,
            androidVersion = "14",
            apiLevel = 34,
            isEmulatorCompatible = false,
            detectedAt = System.currentTimeMillis()
        )

        // When
        composeTestRule.setContent {
            EmuReadyTheme {
                DeviceInfoCard(device = testDevice)
            }
        }

        // Then
        composeTestRule.onNodeWithText("8192 MB total, 6000 MB available").assertIsDisplayed()
        composeTestRule.onNodeWithText("No").assertIsDisplayed()
    }

    @Test
    fun deviceInfoCard_displaysCorrectDeviceType() {
        // Given
        val testDevice = Device(
            id = "rog_ally",
            name = "ROG Ally",
            type = DeviceType.ROG_ALLY,
            manufacturer = "ASUS",
            model = "RC71L",
            cpuArchitecture = "x86_64",
            cpuInfo = "AMD Ryzen Z1 Extreme",
            totalMemoryMB = 16384,
            availableMemoryMB = 14000,
            androidVersion = "13",
            apiLevel = 33,
            isEmulatorCompatible = true,
            detectedAt = System.currentTimeMillis()
        )

        // When
        composeTestRule.setContent {
            EmuReadyTheme {
                DeviceInfoCard(device = testDevice)
            }
        }

        // Then
        composeTestRule.onNodeWithText("ROG ALLY").assertIsDisplayed()
        composeTestRule.onNodeWithText("ASUS").assertIsDisplayed()
    }
}