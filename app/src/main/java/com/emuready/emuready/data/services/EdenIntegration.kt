package com.emuready.emuready.data.services

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.ActivityNotFoundException
import com.emuready.emuready.domain.exceptions.EmulatorException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Complete Eden Emulator Integration following the specification exactly
 */

data class ControlsConfig(
    val vibrationEnabled: Boolean = true,
    val accurateVibrations: Boolean = true,
    val motionEnabled: Boolean = true
)

data class CoreConfig(
    val useMultiCore: Boolean = true,
    val memoryLayoutMode: Boolean = true,
    val useSpeedLimit: Boolean = true,
    val cpuAccuracy: Int = 1, // 0=Fastest, 1=Auto, 2=Accurate
    val speedLimit: Int = 100 // Percentage
)

data class RendererConfig(
    val backend: Boolean = true,
    val shaderBackend: Boolean = true,
    val useVsync: Boolean = false,
    val useAsyncShaders: Boolean = true,
    val gpuAccuracy: Boolean = true,
    val useReactiveFlushing: Boolean = true,
    val useFastGpuTime: Boolean = true,
    val resolutionSetup: Int = 1 // 1=Native, 2=Half, 3=Double
)

data class AudioConfig(
    val outputEngine: Boolean = true,
    val volume: Boolean = true
)

data class SystemConfig(
    val useDockedMode: Boolean = true,
    val languageIndex: Boolean = true
)

data class GpuDriverConfig(
    val driverPath: String
)

data class EmulatorConfiguration(
    val controls: ControlsConfig = ControlsConfig(),
    val core: CoreConfig = CoreConfig(),
    val renderer: RendererConfig = RendererConfig(),
    val audio: AudioConfig = AudioConfig(),
    val system: SystemConfig = SystemConfig(),
    val gpuDriver: GpuDriverConfig? = null
) {
    fun toINIFormat(): String = buildString {
        appendLine("[Controls]")
        appendLine("vibration_enabled\\\\use_global=${controls.vibrationEnabled}")
        appendLine("enable_accurate_vibrations\\\\use_global=${controls.accurateVibrations}")
        appendLine("motion_enabled\\\\use_global=${controls.motionEnabled}")
        appendLine()
        
        appendLine("[Core]")
        appendLine("use_multi_core\\\\use_global=${core.useMultiCore}")
        appendLine("memory_layout_mode\\\\use_global=${core.memoryLayoutMode}")
        appendLine("use_speed_limit\\\\use_global=${core.useSpeedLimit}")
        appendLine("cpu_accuracy\\\\use_global=false")
        appendLine("cpu_accuracy=${core.cpuAccuracy}")
        appendLine("speed_limit\\\\use_global=false")
        appendLine("speed_limit=${core.speedLimit}")
        appendLine()
        
        appendLine("[Renderer]")
        appendLine("backend\\\\use_global=${renderer.backend}")
        appendLine("shader_backend\\\\use_global=${renderer.shaderBackend}")
        appendLine("use_vsync\\\\use_global=false")
        appendLine("use_vsync\\\\default=false")
        appendLine("use_vsync=${if (renderer.useVsync) 1 else 0}")
        appendLine("use_asynchronous_shaders\\\\use_global=false")
        appendLine("use_asynchronous_shaders\\\\default=false")
        appendLine("use_asynchronous_shaders=${renderer.useAsyncShaders}")
        appendLine("resolution_setup\\\\use_global=false")
        appendLine("resolution_setup=${renderer.resolutionSetup}")
        appendLine()
        
        appendLine("[Audio]")
        appendLine("output_engine\\\\use_global=${audio.outputEngine}")
        appendLine("volume\\\\use_global=${audio.volume}")
        appendLine()
        
        appendLine("[System]")
        appendLine("use_docked_mode\\\\use_global=${system.useDockedMode}")
        appendLine("language_index\\\\use_global=${system.languageIndex}")
        appendLine()
        
        gpuDriver?.let { driver ->
            appendLine("[GpuDriver]")
            appendLine("driver_path\\\\use_global=false")
            appendLine("driver_path\\\\default=false")
            appendLine("driver_path=${driver.driverPath}")
        }
    }
}

object EmulatorPresets {
    val KNOWN_WORKING_CONFIG = EmulatorConfiguration(
        controls = ControlsConfig(
            vibrationEnabled = true,
            accurateVibrations = true,
            motionEnabled = true
        ),
        core = CoreConfig(
            useMultiCore = true,
            memoryLayoutMode = true,
            useSpeedLimit = true,
            cpuAccuracy = 1 // Auto
        ),
        renderer = RendererConfig(
            backend = true,
            shaderBackend = true,
            useVsync = false,
            useAsyncShaders = true,
            gpuAccuracy = true,
            useReactiveFlushing = true,
            useFastGpuTime = true,
            resolutionSetup = 1 // Native
        ),
        audio = AudioConfig(
            outputEngine = true,
            volume = true
        ),
        system = SystemConfig(
            useDockedMode = true,
            languageIndex = true
        )
    )
    
    val HIGH_PERFORMANCE = EmulatorConfiguration(
        core = CoreConfig(
            useMultiCore = true,
            cpuAccuracy = 0, // Fastest
            useSpeedLimit = false
        ),
        renderer = RendererConfig(
            useVsync = false,
            useAsyncShaders = true,
            gpuAccuracy = false, // High speed
            useFastGpuTime = true,
            resolutionSetup = 1 // Native
        ),
        system = SystemConfig(
            useDockedMode = true // Better performance
        )
    )
    
    val BATTERY_OPTIMIZED = EmulatorConfiguration(
        core = CoreConfig(
            useSpeedLimit = true,
            speedLimit = 50, // Limit to 50% speed
            cpuAccuracy = 0 // Fastest to save battery
        ),
        renderer = RendererConfig(
            useVsync = true,
            resolutionSetup = 2, // Lower resolution
            useFastGpuTime = true,
            useAsyncShaders = false
        ),
        system = SystemConfig(
            useDockedMode = false // Handheld mode
        )
    )
    
    val BALANCED = EmulatorConfiguration(
        core = CoreConfig(
            useMultiCore = true,
            cpuAccuracy = 1, // Auto
            useSpeedLimit = true,
            speedLimit = 100
        ),
        renderer = RendererConfig(
            useVsync = false,
            useAsyncShaders = true,
            gpuAccuracy = true,
            resolutionSetup = 1 // Native
        ),
        system = SystemConfig(
            useDockedMode = true
        )
    )
}

@Singleton
class EdenLaunchService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val EDEN_PACKAGE = "dev.eden.eden_emulator"
        private const val EDEN_LAUNCH_ACTION = "dev.eden.eden_emulator.LAUNCH_WITH_CUSTOM_CONFIG"
        private const val EMULATION_ACTIVITY = "org.yuzu.yuzu_emu.activities.EmulationActivity"
    }
    
    private val packageManager: PackageManager = context.packageManager
    
    suspend fun isEdenInstalled(): Boolean = withContext(Dispatchers.IO) {
        try {
            packageManager.getPackageInfo(EDEN_PACKAGE, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
    
    suspend fun launchGame(
        titleId: String,
        configuration: String
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
                putExtra("custom_settings", configuration)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            
            // Verify intent can be resolved
            val resolveInfo = packageManager.resolveActivity(intent, 0)
            if (resolveInfo == null) {
                return@withContext Result.failure(
                    EmulatorException("Eden emulator does not support custom configuration")
                )
            }
            
            context.startActivity(intent)
            Result.success(Unit)
            
        } catch (e: ActivityNotFoundException) {
            Result.failure(EmulatorException("Failed to launch Eden emulator", e))
        } catch (e: Exception) {
            Result.failure(EmulatorException("Unexpected error launching emulator", e))
        }
    }
    
    suspend fun launchGameWithPreset(
        titleId: String,
        presetName: String
    ): Result<Unit> {
        val configuration = when (presetName) {
            "Known Working Config" -> EmulatorPresets.KNOWN_WORKING_CONFIG
            "High Performance" -> EmulatorPresets.HIGH_PERFORMANCE
            "Battery Optimized" -> EmulatorPresets.BATTERY_OPTIMIZED
            "Balanced" -> EmulatorPresets.BALANCED
            else -> return Result.failure(
                EmulatorException("Unknown preset: $presetName")
            )
        }
        
        return launchGame(titleId, configuration.toINIFormat())
    }
    
    fun getAvailablePresets(): List<String> = listOf(
        "Known Working Config",
        "High Performance", 
        "Battery Optimized",
        "Balanced"
    )
}

// Legacy interface for backward compatibility
object EdenIntegration {
    const val PACKAGE_NAME = "dev.eden.eden_emulator"
    const val LAUNCH_ACTION = "dev.eden.eden_emulator.LAUNCH_WITH_CUSTOM_CONFIG"
    
    fun configurationToINI(configuration: Map<String, String>): String {
        return configuration.entries.joinToString("\n") { (key, value) ->
            "$key=$value"
        }
    }
    
    fun parseINIConfiguration(iniContent: String): Map<String, String> {
        return iniContent.lines()
            .filter { it.isNotBlank() && it.contains("=") }
            .associate { line ->
                val parts = line.split("=", limit = 2)
                parts[0].trim() to parts[1].trim()
            }
    }
    
    fun getDefaultConfiguration(): Map<String, String> = 
        EmulatorPresets.KNOWN_WORKING_CONFIG.toINIFormat()
            .let(::parseINIConfiguration)
    
    fun getPerformanceConfiguration(): Map<String, String> = 
        EmulatorPresets.HIGH_PERFORMANCE.toINIFormat()
            .let(::parseINIConfiguration)
            
    fun getQualityConfiguration(): Map<String, String> = 
        EmulatorPresets.BALANCED.toINIFormat()
            .let(::parseINIConfiguration)
}