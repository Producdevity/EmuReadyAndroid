package com.emuready.emuready.presentation.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.emuready.emuready.domain.entities.Listing
import com.emuready.emuready.domain.entities.PerformanceLevel
import com.emuready.emuready.presentation.components.GameCoverImage
import com.emuready.emuready.presentation.components.GameScreenshotImage
import com.emuready.emuready.presentation.components.cards.ListingCard
import com.emuready.emuready.presentation.ui.theme.*
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.emuready.emuready.presentation.viewmodels.GameDetailViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    gameId: String,
    onNavigateBack: () -> Unit,
    onNavigateToListing: (String) -> Unit,
    onNavigateToCreate: () -> Unit,
    viewModel: GameDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isVisible by remember { mutableStateOf(false) }
    var showAllListings by remember { mutableStateOf(false) }
    
    LaunchedEffect(gameId) {
        viewModel.loadGameDetails(gameId)
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
        if (uiState.isLoading) {
            LoadingState()
        } else if (uiState.error != null) {
            ErrorState(
                error = uiState.error.toString(),
                onRetry = { viewModel.loadGameDetails(gameId) }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                // Hero Section with Game Cover and Info
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(EmuAnimations.Duration.NORMAL)) + slideInVertically(
                            animationSpec = tween(EmuAnimations.Duration.HERO, easing = EmuAnimations.PremiumEaseOut),
                            initialOffsetY = { -it / 3 }
                        )
                    ) {
                        HeroSection(
                            game = uiState.game,
                            onToggleFavorite = { viewModel.toggleFavorite() },
                            isFavorite = uiState.isFavorite
                        )
                    }
                }
                
                // Quick Actions
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
                        QuickActionsSection(
                            onCreateListing = onNavigateToCreate,
                            onOpenEden = { viewModel.launchInEden() },
                            hasListings = uiState.mobileListings != null || uiState.pcListings != null
                        )
                    }
                }
                
                // Compatibility Stats
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
                        CompatibilityStatsSection(uiState.game)
                    }
                }
                
                // Game Screenshots
                if (uiState.game?.screenshots?.isNotEmpty() == true) {
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
                            ScreenshotsSection(uiState.game!!.screenshots)
                        }
                    }
                }
                
                // Community Listings
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
                        ListingsSection(
                            listings = emptyList(), // TODO: Fix listings access
                            totalListings = 0, // TODO: Fix listings access
                            showAllListings = showAllListings,
                            onToggleShowAll = { showAllListings = !showAllListings },
                            onListingClick = onNavigateToListing
                        )
                    }
                }
                
                // Game Details and Metadata
                if (uiState.game != null) {
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
                            GameMetadataSection(uiState.game!!)
                        }
                    }
                }
            }
        }
        
        // Floating Action Button for Creating Listing
        if (uiState.game != null && isVisible) {
            ExtendedFloatingActionButton(
                onClick = onNavigateToCreate,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                    ),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Create Listing",
                    style = CustomTextStyles.ButtonText
                )
            }
        }
    }
}

@Composable
private fun HeroSection(
    game: com.emuready.emuready.domain.entities.Game?,
    onToggleFavorite: () -> Unit,
    isFavorite: Boolean
) {
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
                    elevation = 16.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column {
                // Cover Image with Gradient Overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    GameCoverImage(
                        imageUrl = game?.coverImageUrl,
                        contentDescription = game?.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    )
                    
                    // Gradient overlay for better text readability
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )
                    
                    // Favorite Button
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .size(48.dp)
                            .background(
                                Color.Black.copy(alpha = 0.3f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite) MaterialTheme.colorScheme.error else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    // Game title overlay
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        Text(
                            text = game?.title ?: "Loading...",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = game?.system?.name ?: "",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        )
                    }
                }
                
                // Game Info Cards
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoCard(
                        title = "Listings",
                        value = game?.totalListings?.toString() ?: "0",
                        subtitle = "community",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        title = "Compatibility",
                        value = "${((game?.averageCompatibility ?: 0f) * 100).toInt()}%",
                        subtitle = "average",
                        color = getCompatibilityColor(game?.averageCompatibility ?: 0f),
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        title = "Rating",
                        value = String.format("%.1f", (game?.averageCompatibility ?: 0f) * 5),
                        subtitle = "out of 5",
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = color.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = title,
                style = CustomTextStyles.CardAccent.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = subtitle,
                style = CustomTextStyles.Caption.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun QuickActionsSection(
    onCreateListing: () -> Unit,
    onOpenEden: () -> Unit,
    hasListings: Boolean
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PremiumButton(
                text = "Create Listing",
                icon = Icons.Default.Add,
                onClick = onCreateListing,
                isPrimary = true,
                modifier = Modifier.weight(1f)
            )
            
            PremiumButton(
                text = "Open in Eden",
                icon = Icons.Default.PlayArrow,
                onClick = onOpenEden,
                isPrimary = false,
                enabled = hasListings,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CompatibilityStatsSection(game: com.emuready.emuready.domain.entities.Game?) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Compatibility Overview",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Performance Level Distribution
                val performanceLevels = listOf(
                    Triple("Perfect", 40, MaterialTheme.colorScheme.primary),
                    Triple("Great", 30, MaterialTheme.colorScheme.secondary),
                    Triple("Good", 20, MaterialTheme.colorScheme.tertiary),
                    Triple("Playable", 10, MaterialTheme.colorScheme.outline)
                )
                
                performanceLevels.forEach { (level, percentage, color) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = level,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LinearProgressIndicator(
                                progress = { percentage / 100f },
                                modifier = Modifier.width(80.dp),
                                color = color,
                                trackColor = color.copy(alpha = 0.2f)
                            )
                            Text(
                                text = "$percentage%",
                                style = CustomTextStyles.Caption,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScreenshotsSection(screenshots: List<String>) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Screenshots",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            itemsIndexed(screenshots) { index, screenshot ->
                var isVisible by remember { mutableStateOf(false) }
                
                LaunchedEffect(Unit) {
                    delay(index * 100L)
                    isVisible = true
                }
                
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(300)) + scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(300)
                    )
                ) {
                    Card(
                        modifier = Modifier
                            .size(width = 200.dp, height = 120.dp)
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        AsyncImage(
                            model = screenshot,
                            contentDescription = "Game screenshot",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ListingsSection(
    listings: List<Listing>,
    totalListings: Int,
    showAllListings: Boolean,
    onToggleShowAll: () -> Unit,
    onListingClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Community Listings",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            if (totalListings > 3) {
                TextButton(
                    onClick = onToggleShowAll
                ) {
                    Text(
                        text = if (showAllListings) "Show Less" else "Show All ($totalListings)",
                        style = CustomTextStyles.ButtonText,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (listings.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listings.forEachIndexed { index, listing ->
                    var isVisible by remember { mutableStateOf(false) }
                    
                    LaunchedEffect(Unit) {
                        delay(index * StaggerAnimations.ItemDelay)
                        isVisible = true
                    }
                    
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                            animationSpec = tween(300),
                            initialOffsetX = { it / 4 }
                        )
                    ) {
                        ListingCard(
                            listing = listing,
                            onClick = { onListingClick(listing.id) },
                            showGameInfo = false
                        )
                    }
                }
            }
        } else {
            EmptyListingsCard()
        }
    }
}

@Composable
private fun GameMetadataSection(game: com.emuready.emuready.domain.entities.Game) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Game Information",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetadataRow(
                    label = "System",
                    value = game.system?.name ?: "Unknown"
                )
                MetadataRow(
                    label = "Release Date",
                    value = game.releaseDate?.toString() ?: "Unknown"
                )
                MetadataRow(
                    label = "Developer",
                    value = game.developer ?: "Unknown"
                )
                MetadataRow(
                    label = "Publisher",
                    value = game.publisher ?: "Unknown"
                )
                MetadataRow(
                    label = "Genre",
                    value = game.genre ?: "Unknown"
                )
                if (game.description?.isNotEmpty() == true) {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = game.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun MetadataRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f, false)
        )
    }
}

@Composable
private fun EmptyListingsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(64.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "No Listings Yet",
                style = CustomTextStyles.GameTitle,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Be the first to create a compatibility listing for this game!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PremiumButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isPrimary: Boolean,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) MicroAnimations.ButtonPressScale else 1f,
        animationSpec = SpringSpecs.Quick,
        label = "button_scale"
    )
    
    val interactionSource = remember { MutableInteractionSource() }
    
    if (isPrimary) {
        Button(
            onClick = {
                if (enabled) {
                    isPressed = true
                    onClick()
                }
            },
            enabled = enabled,
            modifier = modifier
                .scale(scale)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                ),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            interactionSource = interactionSource
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = text,
                    style = CustomTextStyles.ButtonText
                )
            }
        }
    } else {
        OutlinedButton(
            onClick = {
                if (enabled) {
                    isPressed = true
                    onClick()
                }
            },
            enabled = enabled,
            modifier = modifier.scale(scale),
            shape = RoundedCornerShape(12.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = if (enabled) 1f else 0.5f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = if (enabled) 1f else 0.5f)
                    )
                )
            ),
            interactionSource = interactionSource
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = text,
                    style = CustomTextStyles.ButtonText,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = if (enabled) 1f else 0.5f)
                )
            }
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
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                strokeWidth = 3.dp
            )
            Text(
                text = "Loading game details...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorState(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = "Failed to load game",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onRetry,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Try again")
            }
        }
    }
}

@Composable
private fun getCompatibilityColor(compatibility: Float): Color {
    return when {
        compatibility >= 0.8f -> MaterialTheme.colorScheme.primary // Perfect/Great
        compatibility >= 0.6f -> MaterialTheme.colorScheme.secondary // Good
        compatibility >= 0.4f -> MaterialTheme.colorScheme.tertiary // Playable
        else -> MaterialTheme.colorScheme.outline // Poor/Unplayable
    }
}