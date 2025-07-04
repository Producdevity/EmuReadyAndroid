package com.emuready.emuready.domain.usecases.device

import com.emuready.emuready.core.utils.Result
import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.domain.repositories.DeviceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DetectDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(): Flow<Result<Device>> = flow {
        try {
            emit(Result.Loading)
            val device = deviceRepository.detectCurrentDevice()
            emit(Result.Success(device))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error occurred"))
        }
    }
}