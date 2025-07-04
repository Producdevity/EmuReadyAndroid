package com.emuready.emuready.domain.usecases.device

import com.emuready.emuready.core.utils.Result
import com.emuready.emuready.domain.repositories.DeviceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ClearDeviceDataUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            deviceRepository.clearDeviceData()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to clear device data"))
        }
    }
}