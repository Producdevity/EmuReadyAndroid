package com.emuready.emuready.domain.usecases.device

import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.domain.repositories.DeviceRepository
import javax.inject.Inject

class GetCurrentDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(): Result<Device> {
        return deviceRepository.getCurrentDevice()
    }
}