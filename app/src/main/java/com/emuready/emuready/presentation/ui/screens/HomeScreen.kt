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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emuready.emuready.presentation.components.cards.EnhancedGameCard
import com.emuready.emuready.presentation.ui.theme.*
import com.emuready.emuready.presentation.viewmodels.HomeViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToGame: (String) -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEmulatorTest: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val density = LocalDensity.current
    
    // Staggered animation state for entry
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Background,
                        Background.copy(alpha = 0.95f),
                        SurfaceVariant.copy(alpha = 0.1f)
                    )
                )
            ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Hero Section
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(AnimationDurations.NORMAL, easing = EasingCurves.Smooth)) + slideInVertically(
                    animationSpec = tween(AnimationDurations.HERO, easing = EasingCurves.iOSEaseInOut),
                    initialOffsetY = { -it / 2 }
                )
            ) {
                HeroSection(
                    onNavigateToCreate = onNavigateToCreate,
                    onNavigateToEmulatorTest = onNavigateToEmulatorTest
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
                QuickActionsSection()
            }
        }
        
        // Featured Games
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
                FeaturedGamesSection(
                    games = uiState.featuredGames,
                    onGameClick = onNavigateToGame
                )
            }
        }
        
        // Stats Overview
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
                StatsOverviewSection()
            }
        }
        
        // Recent Activity
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
                RecentActivitySection()
            }
        }
        
        // Bottom padding for navigation
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun HeroSection(
    onNavigateToCreate: () -> Unit,
    onNavigateToEmulatorTest: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 32.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = PrimaryPurple.copy(alpha = 0.25f)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = SurfaceElevated
            )
        ) {
            Box {
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    PrimaryPurple.copy(alpha = 0.1f),
                                    SecondaryTeal.copy(alpha = 0.05f),
                                    Color.Transparent
                                ),
                                radius = 800f
                            )
                        )
                )
                
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome to EmuReady",
                        style = MaterialTheme.typography.displaySmall.copy(
                            color = Primary
                        ),
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = "Your premium companion for gaming handheld compatibility",
                        style = CustomTextStyles.HeroTagline,
                        color = OnSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PremiumButton(
                            text = "Create Listing",
                            onClick = onNavigateToCreate,
                            isPrimary = true,
                            modifier = Modifier.weight(1f)
                        )
                        
                        PremiumButton(
                            text = "Test Emulator",
                            onClick = onNavigateToEmulatorTest,
                            isPrimary = false,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionsSection() {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.headlineSmall,
            color = OnBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf(
                Triple("Browse Games", Icons.Default.Search, AccentOrange),
                Triple("My Listings", Icons.Default.List, SecondaryTeal),
                Triple("Favorites", Icons.Default.Favorite, AccentPink),
                Triple("Settings", Icons.Default.Settings, NeutralGray600)
            ).forEachIndexed { index, (title, icon, color) ->
                QuickActionCard(
                    title = title,
                    icon = icon,
                    color = color,
                    modifier = Modifier.weight(1f),
                    animationDelay = index * 50L
                )
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    animationDelay: Long = 0L
) {
    var isPressed by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(animationDelay)
        isVisible = true
    }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) MicroAnimations.ButtonPressScale else 1f,
        animationSpec = SpringSpecs.Quick,
        label = "quick_action_scale"
    )
    
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(AnimationDurations.FAST, easing = EasingCurves.Smooth))
    ) {
        Card(
            modifier = modifier
                .scale(scale)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    isPressed = !isPressed
                }
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = color.copy(alpha = 0.25f)
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = SurfaceElevated
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = color.copy(alpha = 0.1f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = color,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = title,
                    style = CustomTextStyles.Caption,
                    color = OnSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
private fun FeaturedGamesSection(
    games: List<com.emuready.emuready.domain.entities.Game>,
    onGameClick: (String) -> Unit
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
                text = "Featured Games",
                style = MaterialTheme.typography.headlineSmall,
                color = OnBackground
            )
            
            TextButton(
                onClick = { /* Navigate to all games */ }
            ) {
                Text(
                    text = "See All",
                    style = CustomTextStyles.ButtonText,
                    color = Primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (games.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                itemsIndexed(games.take(5)) { index, game ->
                    var isVisible by remember { mutableStateOf(false) }
                    
                    LaunchedEffect(Unit) {
                        delay(index * StaggerAnimations.ItemDelay)
                        isVisible = true
                    }
                    
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(AnimationDurations.FAST, easing = EasingCurves.Smooth)) + slideInHorizontally(
                            animationSpec = tween(AnimationDurations.NORMAL, easing = EasingCurves.iOSEaseOut),
                            initialOffsetX = { it / 2 }
                        )
                    ) {
                        EnhancedGameCard(
                            game = game,
                            onClick = { onGameClick(game.id) },
                            modifier = Modifier.width(280.dp)
                        )
                    }
                }
            }
        } else {
            EmptyStateCard(
                title = "No Featured Games",
                description = "Featured games will appear here when connected to the API",
                icon = Icons.Default.PlayArrow
            )
        }
    }
}

@Composable
private fun StatsOverviewSection() {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Overview",
            style = MaterialTheme.typography.headlineSmall,
            color = OnBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Games",
                value = "1,245",
                subtitle = "in database",
                color = AccentOrange,
                modifier = Modifier.weight(1f)
            )
            
            StatCard(
                title = "Devices",
                value = "18",
                subtitle = "supported",
                color = SecondaryTeal,
                modifier = Modifier.weight(1f)
            )
            
            StatCard(
                title = "Listings",
                value = "3,892",
                subtitle = "active",
                color = AccentPink,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(200)
        isVisible = true
    }
    
    val animatedValue by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = SpringSpecs.Bouncy,
        label = "stat_animation"
    )
    
    Card(
        modifier = modifier
            .scale(animatedValue)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = color.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceElevated
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = color
                )
            )
            
            Text(
                text = title,
                style = CustomTextStyles.CardAccent,
                color = OnSurface
            )
            
            Text(
                text = subtitle,
                style = CustomTextStyles.Caption,
                color = OnSurfaceVariant
            )
        }
    }
}

@Composable
private fun RecentActivitySection() {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Recent Activity",
            style = MaterialTheme.typography.headlineSmall,
            color = OnBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        EmptyStateCard(
            title = "No Recent Activity",
            description = "Your recent games and listings will appear here",
            icon = Icons.Default.Info
        )
    }
}

@Composable
private fun EmptyStateCard(
    title: String,
    description: String,
    icon: ImageVector
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = NeutralGray400.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceElevated
        )
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = NeutralGray200.copy(alpha = 0.5f),
                modifier = Modifier.size(64.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = NeutralGray500,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = title,
                style = CustomTextStyles.GameTitle,
                color = OnSurface,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PremiumButton(
    text: String,
    onClick: () -> Unit,
    isPrimary: Boolean,
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
                isPressed = true
                onClick()
            },
            modifier = modifier
                .scale(scale)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    spotColor = Primary.copy(alpha = 0.25f)
                ),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary
            ),
            interactionSource = interactionSource
        ) {
            Text(
                text = text,
                style = CustomTextStyles.ButtonText,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    } else {
        OutlinedButton(
            onClick = {
                isPressed = true
                onClick()
            },
            modifier = modifier
                .scale(scale),
            shape = RoundedCornerShape(12.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = Brush.linearGradient(
                    colors = listOf(Primary, SecondaryTeal)
                )
            ),
            interactionSource = interactionSource
        ) {
            Text(
                text = text,
                style = CustomTextStyles.ButtonText,
                color = Primary,
                modifier = Modifier.padding(vertical = 4.dp)
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