package com.emuready.emuready.presentation.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.emuready.emuready.presentation.ui.theme.EmuReadyTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DeviceManagementScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun deviceManagementScreen_displaysCorrectInitialState() {
        // When
        composeTestRule.setContent {
            EmuReadyTheme {
                DeviceManagementScreen(
                    onNavigateBack = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Current Device").assertIsDisplayed()
        composeTestRule.onNodeWithText("Device History").assertIsDisplayed()
        composeTestRule.onNodeWithText("Device Management").assertIsDisplayed()
        composeTestRule.onNodeWithText("Refresh Device Info").assertIsDisplayed()
        composeTestRule.onNodeWithText("Clear Device Data").assertIsDisplayed()
    }

    @Test
    fun deviceManagementScreen_refreshButtonIsClickable() {
        // When
        composeTestRule.setContent {
            EmuReadyTheme {
                DeviceManagementScreen(
                    onNavigateBack = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Refresh Device Info")
            .assertIsDisplayed()
            .performClick()
    }

    @Test
    fun deviceManagementScreen_clearDataButtonIsClickable() {
        // When
        composeTestRule.setContent {
            EmuReadyTheme {
                DeviceManagementScreen(
                    onNavigateBack = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Clear Device Data")
            .assertIsDisplayed()
            .performClick()
    }

    @Test
    fun deviceManagementScreen_displaysNoDeviceDetectedMessage() {
        // When
        composeTestRule.setContent {
            EmuReadyTheme {
                DeviceManagementScreen(
                    onNavigateBack = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("No device detected. Tap refresh to scan.")
            .assertIsDisplayed()
    }
}