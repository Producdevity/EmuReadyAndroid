package com.emuready.emuready.presentation.components.device

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.presentation.ui.theme.Spacing
import com.emuready.emuready.presentation.utils.DeviceIconProvider

@Composable
fun DeviceInfoCard(device: Device) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            Icon(
                imageVector = DeviceIconProvider.getDeviceIcon(device.type),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            
            Column {
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = device.type.name.replace("_", " "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.xs))
        
        DeviceInfoRow("Manufacturer", device.manufacturer)
        DeviceInfoRow("Model", device.model)
        DeviceInfoRow("Operating System", device.operatingSystem)
        DeviceInfoRow("Chipset", device.chipset)
        DeviceInfoRow("GPU", device.gpu)
        DeviceInfoRow("Memory", device.memoryDescription)
        DeviceInfoRow("Screen", device.resolutionDescription)
        DeviceInfoRow("Emulator Compatible", if (device.isEmulatorCompatible) "Yes" else "No")
    }
}

@Composable
private fun DeviceInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}