package com.emuready.emuready.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emuready.emuready.presentation.ui.theme.*
import com.emuready.emuready.presentation.viewmodels.EmulatorTestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmulatorTestScreen(
    onNavigateBack: () -> Unit,
    viewModel: EmulatorTestViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("Test Eden Emulator") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
        
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            item {
                EmulatorStatusCard(
                    isInstalled = state.isEdenInstalled,
                    onCheckInstallation = viewModel::checkInstallation
                )
            }
            
            item {
                GameConfigurationCard(
                    titleId = state.titleId,
                    onTitleIdChange = viewModel::updateTitleId,
                    selectedPreset = state.selectedPreset,
                    availablePresets = state.availablePresets,
                    onPresetChange = viewModel::selectPreset
                )
            }
            
            item {
                LaunchButtonsCard(
                    isLoading = state.isLaunching,
                    canLaunch = state.isEdenInstalled && state.titleId.isNotEmpty(),
                    onLaunch = { viewModel.launchGame() }
                )
            }
            
            if (state.testResults.isNotEmpty()) {
                item {
                    TestResultsCard(results = state.testResults)
                }
            }
        }
    }
    
    // Handle launch results
    LaunchedEffect(state.launchResult) {
        state.launchResult?.let { result ->
            result.onFailure { error ->
                // Show error snackbar or dialog
            }.onSuccess {
                // Show success message
            }
            viewModel.clearLaunchResult()
        }
    }
}

@Composable
private fun EmulatorStatusCard(
    isInstalled: Boolean,
    onCheckInstallation: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md)
        ) {
            Text(
                text = "Eden Emulator Status",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(Spacing.sm))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isInstalled) "✅ Eden Emulator Detected" else "❌ Eden Emulator Not Found",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (!isInstalled) {
                        Text(
                            text = "Please install Eden emulator to continue",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                OutlinedButton(onClick = onCheckInstallation) {
                    Text("Check Again")
                }
            }
        }
    }
}

@Composable
private fun GameConfigurationCard(
    titleId: String,
    onTitleIdChange: (String) -> Unit,
    selectedPreset: String,
    availablePresets: List<String>,
    onPresetChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md)
        ) {
            Text(
                text = "Game Configuration",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(Spacing.md))
            
            OutlinedTextField(
                value = titleId,
                onValueChange = onTitleIdChange,
                label = { Text("Title ID") },
                placeholder = { Text("e.g., 01007E3006188000") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(Spacing.md))
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedPreset,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Configuration Preset") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    availablePresets.forEach { preset ->
                        DropdownMenuItem(
                            text = { Text(preset) },
                            onClick = {
                                onPresetChange(preset)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LaunchButtonsCard(
    isLoading: Boolean,
    canLaunch: Boolean,
    onLaunch: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Launch Test",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(Spacing.md))
            
            Button(
                onClick = onLaunch,
                enabled = canLaunch && !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(Spacing.md),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(Spacing.sm))
                }
                Text(
                    text = if (isLoading) "Launching..." else "Launch Game"
                )
            }
        }
    }
}

@Composable
private fun TestResultsCard(
    results: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md)
        ) {
            Text(
                text = "Test Results",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(Spacing.sm))
            
            results.forEach { result ->
                Text(
                    text = "• $result",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}