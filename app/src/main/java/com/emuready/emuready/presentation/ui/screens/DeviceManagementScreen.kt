package com.emuready.emuready.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.emuready.emuready.presentation.components.device.CompatibilityCard
import com.emuready.emuready.presentation.components.device.DeviceHistoryList
import com.emuready.emuready.presentation.components.device.DeviceInfoCard
import com.emuready.emuready.presentation.components.device.RecommendedSettingsCard
import com.emuready.emuready.presentation.ui.theme.Spacing
import com.emuready.emuready.presentation.viewmodels.DeviceManagementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceManagementScreen(
    onNavigateBack: () -> Unit,
    viewModel: DeviceManagementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        viewModel.loadDeviceInfo()
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        item {
            CurrentDeviceSection(
                currentDevice = uiState.currentDevice,
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    viewModel.refreshDeviceInfo()
                    isRefreshing = false
                }
            )
        }
        
        uiState.currentDevice?.let { device ->
            item {
                CompatibilityCard(
                    device = device,
                    compatibilityInfo = uiState.compatibilityInfo
                )
            }
            
            item {
                RecommendedSettingsCard(
                    settings = uiState.compatibilityInfo?.recommendedSettings ?: emptyMap()
                )
            }
        }
        
        item {
            DeviceHistoryList(devices = uiState.detectedDevices)
        }
        
        item {
            DeviceManagementActions(
                onRefresh = { viewModel.refreshDeviceInfo() },
                onClearData = { viewModel.clearDeviceData() }
            )
        }
        
        uiState.error?.let { error ->
            item {
                ErrorCard(
                    error = error,
                    onDismiss = { viewModel.clearError() }
                )
            }
        }
    }
}

@Composable
private fun CurrentDeviceSection(
    currentDevice: com.emuready.emuready.domain.entities.Device?,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Current Device",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                IconButton(
                    onClick = onRefresh,
                    enabled = !isRefreshing
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Refresh device info"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(Spacing.sm))
            
            currentDevice?.let { device ->
                DeviceInfoCard(device = device)
            } ?: run {
                Text(
                    text = "No device detected. Tap refresh to scan.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun DeviceManagementActions(
    onRefresh: () -> Unit,
    onClearData: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            Text(
                text = "Device Management",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Button(
                onClick = onRefresh,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(Spacing.xs))
                Text("Refresh Device Info")
            }
            
            OutlinedButton(
                onClick = onClearData,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Settings, contentDescription = null)
                Spacer(modifier = Modifier.width(Spacing.xs))
                Text("Clear Device Data")
            }
        }
    }
}

@Composable
private fun ErrorCard(
    error: String,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Error",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Bold
                )
                
                TextButton(onClick = onDismiss) {
                    Text("Dismiss")
                }
            }
            
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}