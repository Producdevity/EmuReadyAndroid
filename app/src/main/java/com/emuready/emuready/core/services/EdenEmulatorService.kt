package com.emuready.emuready.core.services

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat.startActivity
import com.emuready.emuready.domain.entities.EmulatorConfiguration
import com.emuready.emuready.domain.entities.EmulatorPresets
import com.emuready.emuready.domain.exceptions.EmulatorException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EdenEmulatorService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val EDEN_PACKAGE = "dev.eden.eden_emulator"
        private const val EDEN_LAUNCH_ACTION = "dev.eden.eden_emulator.LAUNCH_WITH_CUSTOM_CONFIG"
        private const val EMULATION_ACTIVITY = "org.yuzu.yuzu_emu.activities.EmulationActivity"
    }

    suspend fun isEdenInstalled(): Boolean = withContext(Dispatchers.IO) {
        try {
            context.packageManager.getPackageInfo(EDEN_PACKAGE, PackageManager.GET_ACTIVITIES)
            android.util.Log.d("EdenEmulatorService", "Eden emulator found: $EDEN_PACKAGE")
            true
        } catch (e: PackageManager.NameNotFoundException) {
            android.util.Log.e("EdenEmulatorService", "Eden emulator not found: $EDEN_PACKAGE", e)
            false
        } catch (e: Exception) {
            android.util.Log.e("EdenEmulatorService", "Error checking Eden emulator: ${e.message}", e)
            false
        }
    }

    suspend fun launchGame(
        titleId: String,
        configuration: EmulatorConfiguration
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (!isEdenInstalled()) {
                return@withContext Result.failure(
                    EmulatorException("Eden emulator is not installed")
                )
            }

            val intent = Intent().apply {
                action = EDEN_LAUNCH_ACTION
                setPackage(EDEN_PACKAGE)
                setClassName(EDEN_PACKAGE, EMULATION_ACTIVITY)
                putExtra("title_id", titleId)
                putExtra("custom_settings", configuration.toINIFormat())
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            // Verify intent can be resolved
            val resolveInfo = context.packageManager.resolveActivity(intent, 0)
            if (resolveInfo == null) {
                return@withContext Result.failure(
                    EmulatorException("Eden emulator does not support custom configuration")
                )
            }

            context.startActivity(intent)
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(EmulatorException("Failed to launch Eden emulator", e))
        }
    }

    suspend fun launchGameWithPreset(
        titleId: String,
        presetName: String
    ): Result<Unit> {
        return when (presetName) {
            "No Config" -> launchGameWithoutConfig(titleId)
            "Known Working Config" -> launchGame(titleId, EmulatorPresets.KNOWN_WORKING_CONFIG)
            "High Performance" -> launchGame(titleId, EmulatorPresets.HIGH_PERFORMANCE)
            "Battery Optimized" -> launchGame(titleId, EmulatorPresets.BATTERY_OPTIMIZED)
            "Balanced" -> launchGame(titleId, EmulatorPresets.BALANCED)
            else -> Result.failure(
                EmulatorException("Unknown preset: $presetName")
            )
        }
    }

    suspend fun getAvailablePresets(): List<String> {
        return listOf(
            "No Config",
            "Known Working Config",
            "High Performance",
            "Battery Optimized",
            "Balanced"
        )
    }

    suspend fun launchGameWithoutConfig(
        titleId: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (!isEdenInstalled()) {
                return@withContext Result.failure(
                    EmulatorException("Eden emulator is not installed")
                )
            }

            val intent = Intent().apply {
                action = EDEN_LAUNCH_ACTION
                setPackage(EDEN_PACKAGE)
                setClassName(EDEN_PACKAGE, EMULATION_ACTIVITY)
                putExtra("title_id", titleId)
                // No custom_settings for "No Config" option
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            // Verify intent can be resolved
            val resolveInfo = context.packageManager.resolveActivity(intent, 0)
            if (resolveInfo == null) {
                return@withContext Result.failure(
                    EmulatorException("Eden emulator is not available")
                )
            }

            context.startActivity(intent)
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(EmulatorException("Failed to launch Eden emulator", e))
        }
    }
}
