package com.emuready.emuready.presentation.components.device

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.presentation.ui.theme.Spacing
import com.emuready.emuready.presentation.utils.DeviceIconProvider
import com.emuready.emuready.presentation.utils.DateUtils

@Composable
fun DeviceHistoryList(devices: List<Device>) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md)
        ) {
            Text(
                text = "Device History",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(Spacing.sm))
            
            if (devices.isEmpty()) {
                Text(
                    text = "No devices detected yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                devices.forEach { device ->
                    DeviceHistoryItem(device = device)
                    if (device != devices.last()) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.xs))
                    }
                }
            }
        }
    }
}

@Composable
private fun DeviceHistoryItem(device: Device) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = device.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Registered: ${DateUtils.formatDateTime(device.registeredAt.epochSecond * 1000)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Icon(
            imageVector = DeviceIconProvider.getDeviceIcon(device.type),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}