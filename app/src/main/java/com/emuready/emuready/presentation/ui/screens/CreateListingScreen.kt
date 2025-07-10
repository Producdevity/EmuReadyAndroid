package com.emuready.emuready.presentation.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emuready.emuready.domain.entities.Device
import com.emuready.emuready.domain.entities.Emulator
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.domain.entities.ListingType
import com.emuready.emuready.domain.entities.PerformanceLevel
import com.emuready.emuready.presentation.components.GameCoverImage
import com.emuready.emuready.presentation.components.forms.GameSearchField
import com.emuready.emuready.presentation.ui.theme.*
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.emuready.emuready.presentation.viewmodels.CreateListingViewModel
import com.emuready.emuready.presentation.viewmodels.MobileDeviceFormData
import com.emuready.emuready.presentation.viewmodels.PCConfigurationFormData
import com.emuready.emuready.presentation.utils.AuthRequiredDialog
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateListingScreen(
    gameId: String? = null, // Pre-selected game ID
    onNavigateBack: () -> Unit,
    onListingCreated: () -> Unit,
    onNavigateToSignIn: () -> Unit = {},
    viewModel: CreateListingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(gameId) {
        if (gameId != null) {
            viewModel.selectGame(gameId)
        }
        delay(200)
        isVisible = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Header Section
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(AnimationDurations.NORMAL)) + slideInVertically(
                        animationSpec = tween(AnimationDurations.HERO, easing = EasingCurves.iOSEaseOut),
                        initialOffsetY = { -it / 3 }
                    )
                ) {
                    HeaderSection()
                }
            }
            
            // Game Selection
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = AnimationDurations.NORMAL,
                            delayMillis = StaggerAnimations.ItemDelay.toInt()
                        )
                    )
                ) {
                    GameSelectionSection(
                        selectedGame = uiState.selectedGame,
                        onGameSelected = viewModel::selectGame,
                        onGameSearch = viewModel::searchGames,
                        searchResults = uiState.gameSearchResults,
                        isSearching = uiState.isSearchingGames
                    )
                }
            }
            
            // Listing Type Selection
            if (uiState.selectedGame != null) {
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = AnimationDurations.NORMAL,
                                delayMillis = (StaggerAnimations.ItemDelay * 2).toInt()
                            )
                        )
                    ) {
                        ListingTypeSection(
                            selectedType = uiState.listingType,
                            onTypeSelected = viewModel::selectListingType
                        )
                    }
                }
            }
            
            // Device/PC Configuration Forms
            if (uiState.selectedGame != null && uiState.listingType != null) {
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = AnimationDurations.NORMAL,
                                delayMillis = (StaggerAnimations.ItemDelay * 3).toInt()
                            )
                        )
                    ) {
                        when (uiState.listingType) {
                            ListingType.MOBILE_DEVICE -> {
                                MobileDeviceForm(
                                    formData = uiState.mobileDeviceForm,
                                    onFormUpdate = { update -> viewModel.updateMobileDeviceForm { update } },
                                    availableDevices = uiState.availableDevices,
                                    availableEmulators = uiState.availableEmulators
                                )
                            }
                            ListingType.PC_CONFIGURATION -> {
                                PCConfigurationForm(
                                    formData = uiState.pcConfigurationForm,
                                    onFormUpdate = { update -> viewModel.updatePCConfigurationForm { update } }
                                )
                            }
                            else -> { /* Other types */ }
                        }
                    }
                }
            }
            
            // Performance & Configuration
            if (uiState.canShowPerformanceForm()) {
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = AnimationDurations.NORMAL,
                                delayMillis = (StaggerAnimations.ItemDelay * 4).toInt()
                            )
                        )
                    ) {
                        PerformanceConfigurationSection(
                            performanceLevel = uiState.performanceLevel,
                            onPerformanceLevelSelected = viewModel::selectPerformanceLevel,
                            emulatorConfig = uiState.emulatorConfig,
                            onEmulatorConfigChanged = viewModel::updateEmulatorConfig,
                            notes = uiState.notes,
                            onNotesChanged = viewModel::updateNotes
                        )
                    }
                }
            }
            
            // Submit Section
            if (uiState.canSubmit()) {
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = AnimationDurations.NORMAL,
                                delayMillis = (StaggerAnimations.ItemDelay * 5).toInt()
                            )
                        )
                    ) {
                        SubmitSection(
                            isLoading = uiState.isSubmitting,
                            onSubmit = {
                                viewModel.submitListing(
                                    onSuccess = onListingCreated,
                                    onError = { /* Handle error */ }
                                )
                            }
                        )
                    }
                }
            }
        }
        
        // Error Snackbar
        uiState.error?.let { error ->
            LaunchedEffect(error) {
                // Show snackbar
                delay(3000)
                viewModel.clearError()
            }
        }
        
        // Authentication required dialog
        if (uiState.showAuthRequired) {
            AuthRequiredDialog(
                isVisible = true,
                title = "Sign In Required",
                message = "You need to sign in to create listings. This helps maintain quality and prevents spam.",
                onSignIn = {
                    viewModel.dismissAuthRequired()
                    onNavigateToSignIn()
                },
                onDismiss = viewModel::dismissAuthRequired
            )
        }
    }
}

@Composable
private fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Box {
                // Gradient background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f),
                                    Color.Transparent
                                ),
                                radius = 400f
                            )
                        )
                )
                
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Create New Listing",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center
                    )
                    
                    Text(
                        text = "Share your game compatibility experience",
                        style = CustomTextStyles.HeroTagline,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun GameSelectionSection(
    selectedGame: Game?,
    onGameSelected: (String) -> Unit,
    onGameSearch: (String) -> Unit,
    searchResults: List<Game>,
    isSearching: Boolean
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Select Game",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (selectedGame != null) {
            // Show selected game
            SelectedGameCard(
                game = selectedGame,
                onClearSelection = { /* Reset selection */ }
            )
        } else {
            // Show game search
            GameSearchField(
                onSearchQueryChanged = onGameSearch,
                searchResults = searchResults,
                onGameSelected = { game -> onGameSelected(game.id) },
                isLoading = isSearching,
                placeholder = "Search for a game..."
            )
        }
    }
}

@Composable
private fun SelectedGameCard(
    game: Game,
    onClearSelection: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GameCoverImage(
                imageUrl = game.coverImageUrl,
                contentDescription = game.title,
                modifier = Modifier.size(80.dp)
            )
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = game.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = game.system?.name ?: "Unknown System",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(
                onClick = onClearSelection,
                modifier = Modifier.align(Alignment.Top)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Change game",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ListingTypeSection(
    selectedType: ListingType?,
    onTypeSelected: (ListingType) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Listing Type",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ListingTypeCard(
                title = "Mobile Device",
                description = "Gaming handheld or mobile device",
                icon = Icons.Default.PhoneAndroid,
                isSelected = selectedType == ListingType.MOBILE_DEVICE,
                onClick = { onTypeSelected(ListingType.MOBILE_DEVICE) },
                modifier = Modifier.weight(1f)
            )
            
            ListingTypeCard(
                title = "PC Configuration",
                description = "Desktop or laptop computer",
                icon = Icons.Default.Computer,
                isSelected = selectedType == ListingType.PC_CONFIGURATION,
                onClick = { onTypeSelected(ListingType.PC_CONFIGURATION) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ListingTypeCard(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) MicroAnimations.ButtonPressScale else 1f,
        animationSpec = SpringSpecs.Quick,
        label = "type_card_scale"
    )
    
    Card(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
            }
            .shadow(
                elevation = if (isSelected) 12.dp else 6.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.25f) else Color.Transparent
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = if (isSelected) 
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = title,
                style = CustomTextStyles.CardAccent.copy(
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = description,
                style = CustomTextStyles.Caption,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}

@Composable
private fun MobileDeviceForm(
    formData: MobileDeviceFormData,
    onFormUpdate: (MobileDeviceFormData) -> Unit,
    availableDevices: List<Device>,
    availableEmulators: List<Emulator>
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Device Information",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Device selection, emulator selection, etc.
                Text(
                    text = "Mobile device form will be implemented with:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "• Device selection dropdown\n• Emulator selection\n• Custom configuration fields\n• Performance testing results",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun PCConfigurationForm(
    formData: PCConfigurationFormData,
    onFormUpdate: (PCConfigurationFormData) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = "PC Configuration",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // PC specs form
                Text(
                    text = "PC configuration form will be implemented with:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "• CPU, GPU, RAM specifications\n• Operating system details\n• Emulator version and settings\n• Performance metrics",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun PerformanceConfigurationSection(
    performanceLevel: PerformanceLevel?,
    onPerformanceLevelSelected: (PerformanceLevel) -> Unit,
    emulatorConfig: String,
    onEmulatorConfigChanged: (String) -> Unit,
    notes: String,
    onNotesChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Performance & Configuration",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Performance Level Selection
                Text(
                    text = "Performance Level",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                PerformanceLevelGrid(
                    selectedLevel = performanceLevel,
                    onLevelSelected = onPerformanceLevelSelected
                )
                
                Divider()
                
                // Configuration Notes
                Text(
                    text = "Additional Notes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                OutlinedTextField(
                    value = notes,
                    onValueChange = onNotesChanged,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Add any additional notes about performance, settings, or issues...")
                    },
                    minLines = 3,
                    maxLines = 6
                )
            }
        }
    }
}

@Composable
private fun PerformanceLevelGrid(
    selectedLevel: PerformanceLevel?,
    onLevelSelected: (PerformanceLevel) -> Unit
) {
    val performanceLevels = listOf(
        Triple(PerformanceLevel.PERFECT, "Perfect", "60 FPS, no issues"),
        Triple(PerformanceLevel.GREAT, "Great", "50-60 FPS, minor issues"),
        Triple(PerformanceLevel.GOOD, "Good", "40-50 FPS, some issues"),
        Triple(PerformanceLevel.PLAYABLE, "Playable", "30-40 FPS, noticeable issues"),
        Triple(PerformanceLevel.POOR, "Poor", "20-30 FPS, major issues"),
        Triple(PerformanceLevel.UNPLAYABLE, "Unplayable", "<20 FPS, unplayable")
    )
    
    @OptIn(ExperimentalLayoutApi::class)
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        performanceLevels.forEach { (level, title, description) ->
            PerformanceLevelChip(
                level = level,
                title = title,
                description = description,
                isSelected = selectedLevel == level,
                onClick = { onLevelSelected(level) }
            )
        }
    }
}

@Composable
private fun PerformanceLevelChip(
    level: PerformanceLevel,
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = getPerformanceLevelColor(level)
    
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = color
                )
            }
        } else null,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = color.copy(alpha = 0.1f),
            selectedLabelColor = color
        )
    )
}

@Composable
private fun SubmitSection(
    isLoading: Boolean,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ready to Submit?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Your listing will help the community find the best emulation settings",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Button(
                    onClick = onSubmit,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    } else {
                        Icon(
                            imageVector = Icons.Default.Upload,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Text(
                        text = if (isLoading) "Creating Listing..." else "Create Listing",
                        style = CustomTextStyles.ButtonText
                    )
                }
            }
        }
    }
}

@Composable
private fun getPerformanceLevelColor(level: PerformanceLevel): Color {
    return when (level) {
        PerformanceLevel.PERFECT -> MaterialTheme.colorScheme.primary
        PerformanceLevel.GREAT -> MaterialTheme.colorScheme.secondary
        PerformanceLevel.GOOD -> MaterialTheme.colorScheme.tertiary
        PerformanceLevel.PLAYABLE -> MaterialTheme.colorScheme.outline
        PerformanceLevel.POOR -> MaterialTheme.colorScheme.error
        PerformanceLevel.UNPLAYABLE -> MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
    }
}