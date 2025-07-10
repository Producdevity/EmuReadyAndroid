package com.emuready.emuready.data.repositories

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcRequestBuilder
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
import com.emuready.emuready.data.mappers.toDomain
import com.emuready.emuready.data.services.EdenIntegration
import com.emuready.emuready.domain.entities.*
import com.emuready.emuready.data.remote.dto.TrpcResponseDtos
import com.emuready.emuready.domain.exceptions.ApiException
import com.emuready.emuready.domain.exceptions.NetworkException
import com.emuready.emuready.domain.repositories.EmulatorRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmulatorRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val trpcApiService: EmuReadyTrpcApiService
) : EmulatorRepository {
    
    private val requestBuilder = TrpcRequestBuilder()
    
    override suspend fun getEmulators(systemId: String?, search: String?): Result<List<Emulator>> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildRequest(TrpcRequestDtos.GetEmulatorsSchema(systemId = systemId, search = search))
            val responseWrapper = trpcApiService.getEmulators(request)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val emulators = response.result.data.map { it.toDomain() }
                Result.success(emulators)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getEmulatorById(emulatorId: String): Result<Emulator> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildRequest(TrpcRequestDtos.IdRequest(emulatorId))
            val responseWrapper = trpcApiService.getEmulatorById(request)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val emulator = response.result.data.toDomain()
                Result.success(emulator)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getCustomFields(emulatorId: String): Result<List<CustomFieldDefinition>> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildRequest(TrpcRequestDtos.EmulatorIdRequest(emulatorId))
            val responseWrapper = trpcApiService.getCustomFieldsByEmulator(request)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val customFields = response.result.data.map { it.toDomain() }
                Result.success(customFields)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getPopularEmulators(limit: Int): Result<List<Emulator>> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildRequest(TrpcRequestDtos.GetEmulatorsSchema(limit = limit))
            val responseWrapper = trpcApiService.getEmulators(request)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val emulators = response.result.data.map { it.toDomain() }
                    .sortedByDescending { it.supportedSystems.size } // Sort by system support
                    .take(limit)
                Result.success(emulators)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getPresets(emulatorId: String): Result<List<EmulatorPreset>> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildRequest(TrpcRequestDtos.LimitRequest(50))
            val responseWrapper = trpcApiService.getPcPresets(request)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                // Convert PC presets to emulator presets
                val presets = response.result.data.map { pcPreset ->
                    EmulatorPreset(
                        id = pcPreset.id,
                        emulatorId = emulatorId,
                        name = pcPreset.name,
                        description = null,
                        configuration = mapOf(
                            "cpu" to pcPreset.cpu.modelName,
                            "gpu" to pcPreset.gpu.modelName,
                            "memory" to "${pcPreset.memorySize}GB",
                            "os" to pcPreset.os
                        ),
                        isOfficial = false,
                        isPublic = true,
                        createdBy = "system",
                        createdAt = java.lang.System.currentTimeMillis(),
                        usageCount = 0
                    )
                }
                Result.success(presets)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun createPreset(emulatorId: String, name: String, configuration: Map<String, String>): Result<EmulatorPreset> = withContext(Dispatchers.IO) {
        try {
            // For now, create a local preset (would need API support for full implementation)
            val preset = EmulatorPreset(
                id = generatePresetId(),
                emulatorId = emulatorId,
                name = name,
                description = null,
                configuration = configuration,
                isOfficial = false,
                isPublic = false,
                createdBy = "user",
                createdAt = java.lang.System.currentTimeMillis(),
                usageCount = 0
            )
            Result.success(preset)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updatePreset(presetId: String, name: String, configuration: Map<String, String>): Result<EmulatorPreset> = withContext(Dispatchers.IO) {
        try {
            // For now, create updated preset (would need API support for full implementation)
            val preset = EmulatorPreset(
                id = presetId,
                emulatorId = "",
                name = name,
                description = null,
                configuration = configuration,
                isOfficial = false,
                isPublic = false,
                createdBy = "user",
                createdAt = java.lang.System.currentTimeMillis(),
                usageCount = 0
            )
            Result.success(preset)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deletePreset(presetId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildRequest(TrpcRequestDtos.IdRequest(presetId))
            val responseWrapper = trpcApiService.deletePcPreset(request)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else {
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getSupportedSystems(): Result<List<System>> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildRequest(Unit)
            val responseWrapper = trpcApiService.getSystems(request)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val systems = response.result.data.map { it.toDomain() }
                Result.success(systems)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun launchGameWithEden(gameId: String, configuration: Map<String, String>): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (!isEdenInstalled().getOrDefault(false)) {
                return@withContext Result.failure(Exception("Eden emulator is not installed"))
            }
            
            val intent = Intent().apply {
                action = EdenIntegration.LAUNCH_ACTION
                setPackage(EdenIntegration.PACKAGE_NAME)
                putExtra("title_id", gameId)
                putExtra("custom_settings", EdenIntegration.configurationToINI(configuration))
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            
            context.startActivity(intent)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getEdenConfiguration(listingId: String): Result<Map<String, String>> = withContext(Dispatchers.IO) {
        try {
            // This would fetch the configuration from a specific listing
            // For now, return a default configuration
            val defaultConfig = mapOf(
                "renderer_backend" to "Vulkan",
                "use_fast_gpu_time" to "true",
                "resolution_factor" to "1",
                "anisotropic_filtering" to "Automatic"
            )
            Result.success(defaultConfig)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun isEdenInstalled(): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            context.packageManager.getPackageInfo(EdenIntegration.PACKAGE_NAME, PackageManager.GET_ACTIVITIES)
            Result.success(true)
        } catch (e: PackageManager.NameNotFoundException) {
            Result.success(false)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun isDeveloperVerified(emulatorId: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildRequest(TrpcRequestDtos.VerifyDeveloperRequest("", emulatorId))
            val responseWrapper = trpcApiService.isVerifiedDeveloper(request)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                Result.success(response.result.data.isVerified)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun verifyListing(listingId: String, notes: String?): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildRequest(TrpcRequestDtos.VerifyListingRequest(listingId, notes))
            val responseWrapper = trpcApiService.verifyListing(request)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else {
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    override suspend fun getVerifiedEmulators(): Result<List<Emulator>> = withContext(Dispatchers.IO) {
        try {
            val request = requestBuilder.buildRequest(Unit)
            val responseWrapper = trpcApiService.getMyVerifiedEmulators(request)
            val response = responseWrapper.`0`
            
            if (response.error != null) {
                Result.failure(ApiException(response.error.message))
            } else if (response.result?.data != null) {
                val emulators = response.result.data.map { it.emulator.toDomain() }
                Result.success(emulators)
            } else {
                Result.failure(ApiException("Invalid response format"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error: ${e.message}", e))
        }
    }
    
    private fun generatePresetId(): String {
        return "preset_${java.lang.System.currentTimeMillis()}_${(1000..9999).random()}"
    }
}