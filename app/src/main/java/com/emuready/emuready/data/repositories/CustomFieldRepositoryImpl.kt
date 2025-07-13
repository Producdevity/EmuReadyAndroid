package com.emuready.emuready.data.repositories

import com.emuready.emuready.data.mappers.*
import com.emuready.emuready.data.remote.api.EmuReadyTrpcApiService
import com.emuready.emuready.data.remote.api.trpc.TrpcInputHelper
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
import com.emuready.emuready.domain.entities.CustomFieldDefinition
import com.emuready.emuready.domain.exceptions.ApiException
import com.emuready.emuready.domain.exceptions.NetworkException
import com.emuready.emuready.domain.repositories.CustomFieldRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomFieldRepositoryImpl @Inject constructor(
    private val trpcApiService: EmuReadyTrpcApiService
) : CustomFieldRepository {
    
    override suspend fun getCustomFieldsByEmulator(emulatorId: String): Result<List<CustomFieldDefinition>> = withContext(Dispatchers.IO) {
        try {
            val input = TrpcInputHelper.createInput(TrpcRequestDtos.EmulatorIdRequest(emulatorId = emulatorId))
            val responseWrapper = trpcApiService.getCustomFieldsByEmulator(input = input)
            val response = responseWrapper
            
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
}