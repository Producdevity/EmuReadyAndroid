package com.emuready.emuready.domain.entities

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

data class ControlsConfig(
    val vibrationEnabled: Boolean = true,
    val accurateVibrations: Boolean = true,
    val motionEnabled: Boolean = true
)

data class CoreConfig(
    val useMultiCore: Boolean = true,
    val memoryLayoutMode: Boolean = true,
    val useSpeedLimit: Boolean = true,
    val cpuAccuracy: Int = 1,
    val speedLimit: Int = 100
)

data class RendererConfig(
    val backend: Boolean = true,
    val shaderBackend: Boolean = true,
    val useVsync: Boolean = false,
    val useAsyncShaders: Boolean = true,
    val gpuAccuracy: Boolean = true,
    val useReactiveFlushing: Boolean = true,
    val useFastGpuTime: Boolean = true,
    val resolutionSetup: Int = 1
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