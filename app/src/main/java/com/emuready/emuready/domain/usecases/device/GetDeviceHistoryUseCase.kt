package com.emuready.emuready.domain.usecases.device

import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.domain.repositories.DeviceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDeviceHistoryUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    operator fun invoke(): Flow<List<Device>> {
        return deviceRepository.getDetectedDevices()
    }
}