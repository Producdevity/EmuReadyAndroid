package com.emuready.emuready.domain.entities

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
            useSpeedLimit = true
        ),
        renderer = RendererConfig(
            backend = true,
            shaderBackend = true,
            useVsync = false,
            useAsyncShaders = true,
            gpuAccuracy = true,
            useReactiveFlushing = true,
            useFastGpuTime = true
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
            cpuAccuracy = 0 // Fastest
        ),
        renderer = RendererConfig(
            useVsync = false,
            useAsyncShaders = true,
            gpuAccuracy = false // High speed
        ),
        system = SystemConfig(
            useDockedMode = true // Better performance
        )
    )
    
    val BATTERY_OPTIMIZED = EmulatorConfiguration(
        core = CoreConfig(
            useSpeedLimit = true,
            speedLimit = 50 // Limit to 50% speed
        ),
        renderer = RendererConfig(
            useVsync = true,
            resolutionSetup = 2 // Lower resolution
        ),
        system = SystemConfig(
            useDockedMode = false // Handheld mode
        )
    )
    
    val BALANCED = EmulatorConfiguration(
        core = CoreConfig(
            useMultiCore = true,
            cpuAccuracy = 1
        ),
        renderer = RendererConfig(
            useVsync = false,
            useAsyncShaders = true,
            gpuAccuracy = true
        ),
        system = SystemConfig(
            useDockedMode = true
        )
    )
}