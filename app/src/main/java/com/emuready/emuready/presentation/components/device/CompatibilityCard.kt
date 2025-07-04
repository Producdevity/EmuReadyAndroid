package com.emuready.emuready.presentation.components.device

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.emuready.emuready.data.services.DeviceManager
import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.presentation.ui.theme.Spacing

@Composable
fun CompatibilityCard(
    device: Device,
    compatibilityInfo: DeviceManager.DeviceCompatibilityInfo?
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md)
        ) {
            Text(
                text = "Compatibility Assessment",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(Spacing.sm))
            
            compatibilityInfo?.let { info ->
                CompatibilityScore(info.compatibilityScore)
                
                Spacer(modifier = Modifier.height(Spacing.sm))
                
                Text(
                    text = info.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (info.limitations.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(Spacing.sm))
                    CompatibilityLimitations(info.limitations)
                }
            }
        }
    }
}

@Composable
private fun CompatibilityScore(score: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Compatibility Score",
            style = MaterialTheme.typography.bodyMedium
        )
        
        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier.width(100.dp)
        )
        
        Text(
            text = "$score%",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CompatibilityLimitations(limitations: List<String>) {
    Text(
        text = "Limitations:",
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Bold
    )
    limitations.forEach { limitation ->
        Text(
            text = "• $limitation",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}