package com.emuready.emuready.presentation.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.emuready.emuready.domain.entities.DeviceType

object DeviceIconProvider {
    
    fun getDeviceIcon(deviceType: DeviceType): ImageVector {
        return when (deviceType) {
            DeviceType.STEAM_DECK,
            DeviceType.ROG_ALLY,
            DeviceType.LENOVO_LEGION_GO,
            DeviceType.AYA_NEO,
            DeviceType.ONEXPLAYER -> Icons.Default.Star
            
            DeviceType.GPD_WIN,
            DeviceType.GPD_POCKET,
            DeviceType.HANDHELD_PC -> Icons.Default.Home
            
            DeviceType.PHONE_TABLET -> Icons.Default.Phone
            
            DeviceType.UNKNOWN -> Icons.Default.Settings
        }
    }
}