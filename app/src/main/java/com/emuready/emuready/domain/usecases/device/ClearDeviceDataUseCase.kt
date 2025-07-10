package com.emuready.emuready.domain.usecases.device

import com.emuready.emuready.domain.repositories.DeviceRepository
import javax.inject.Inject

class ClearDeviceDataUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return deviceRepository.clearDeviceData()
    }
}