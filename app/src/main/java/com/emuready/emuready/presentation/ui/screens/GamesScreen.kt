package com.emuready.emuready.presentation.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emuready.emuready.presentation.components.cards.EnhancedGameCard
import com.emuready.emuready.presentation.ui.theme.*
import com.emuready.emuready.presentation.viewmodels.BrowseViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(
    onNavigateToGame: (String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: BrowseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("All") }
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(50)
        isVisible = true
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Background,
                        SurfaceVariant.copy(alpha = 0.05f)
                    )
                )
            )
    ) {
        // Header with Search
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(AnimationDurations.NORMAL, easing = EasingCurves.Smooth)) + slideInVertically(
                initialOffsetY = { -it / 2 }
            )
        ) {
            GamesHeader(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onNavigateBack = onNavigateBack
            )
        }
        
        // Genre Filter
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = AnimationDurations.NORMAL,
                    delayMillis = StaggerAnimations.ItemDelay.toInt()
                )
            )
        ) {
            GenreFilter(
                selectedGenre = selectedGenre,
                onGenreSelected = { selectedGenre = it }
            )
        }
        
        // Games Grid
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = AnimationDurations.NORMAL,
                    delayMillis = (StaggerAnimations.ItemDelay * 2).toInt()
                )
            )
        ) {
            GamesGrid(
                games = uiState.games,
                onGameClick = onNavigateToGame
            )
        }
    }
}

@Composable
private fun GamesHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                spotColor = PrimaryPurple.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceElevated
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Primary
                    )
                }
                
                Text(
                    text = "Games Library",
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnSurface
                )
                
                IconButton(
                    onClick = { /* Filter options */ }
                ) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Filter",
                        tint = Primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Search games...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Primary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChange("") }
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = OnSurfaceVariant
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Outline.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
private fun GenreFilter(
    selectedGenre: String,
    onGenreSelected: (String) -> Unit
) {
    val genres = listOf(
        "All", "Action", "Adventure", "RPG", "Strategy", 
        "Puzzle", "Sports", "Racing", "Fighting", "Platformer"
    )
    
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Genres",
            style = MaterialTheme.typography.titleMedium,
            color = OnBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            itemsIndexed(genres) { index, genre ->
                var isVisible by remember { mutableStateOf(false) }
                
                LaunchedEffect(Unit) {
                    delay(index * 30L)
                    isVisible = true
                }
                
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(AnimationDurations.FAST, easing = EasingCurves.Smooth)) + slideInHorizontally(
                        initialOffsetX = { it / 3 }
                    )
                ) {
                    GenreChip(
                        text = genre,
                        isSelected = genre == selectedGenre,
                        onClick = { onGenreSelected(genre) }
                    )
                }
            }
        }
    }
}

@Composable
private fun GenreChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Primary else SurfaceElevated
    val textColor = if (isSelected) OnPrimary else OnSurface
    val borderColor = if (isSelected) Primary else Outline.copy(alpha = 0.5f)
    
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) MicroAnimations.ButtonPressScale else 1f,
        animationSpec = SpringSpecs.Quick,
        label = "chip_scale"
    )
    
    Surface(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier,
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = borderColor
        ),
        shadowElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Text(
            text = text,
            style = CustomTextStyles.ButtonText,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}

@Composable
private fun GamesGrid(
    games: List<com.emuready.emuready.domain.entities.Game>,
    onGameClick: (String) -> Unit
) {
    if (games.isNotEmpty()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(games) { game ->
                var isVisible by remember { mutableStateOf(false) }
                
                LaunchedEffect(Unit) {
                    delay(50L)
                    isVisible = true
                }
                
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(AnimationDurations.FAST, easing = EasingCurves.Smooth)) + scaleIn(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        initialScale = 0.8f
                    )
                ) {
                    EnhancedGameCard(
                        game = game,
                        onClick = { onGameClick(game.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    } else {
        // Empty State
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            contentAlignment = Alignment.Center
        ) {
            EmptyGamesState()
        }
    }
}

@Composable
private fun EmptyGamesState() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = NeutralGray400.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceElevated
        )
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated game controller icon
            var isAnimating by remember { mutableStateOf(false) }
            
            LaunchedEffect(Unit) {
                isAnimating = true
            }
            
            val rotation by animateFloatAsState(
                targetValue = if (isAnimating) 10f else -10f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = EasingCurves.Smooth),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "controller_rotation"
            )
            
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = NeutralGray200.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(80.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "No games",
                        tint = Primary,
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "No Games Found",
                style = MaterialTheme.typography.headlineSmall,
                color = OnSurface,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Try adjusting your search or filters to find games",
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { /* Refresh or browse all */ },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary
                )
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Refresh",
                    style = CustomTextStyles.ButtonText
                )
            }
        }
    }
}