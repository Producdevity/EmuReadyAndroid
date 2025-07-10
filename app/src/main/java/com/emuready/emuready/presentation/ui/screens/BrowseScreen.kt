package com.emuready.emuready.presentation.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.BorderStroke
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.presentation.components.GameCoverImage
import com.emuready.emuready.presentation.ui.theme.Spacing
import com.emuready.emuready.presentation.ui.theme.EmuAnimations
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.emuready.emuready.presentation.viewmodels.BrowseViewModel
import com.emuready.emuready.presentation.viewmodels.FilterType
import com.emuready.emuready.presentation.viewmodels.SortOption
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun BrowseScreen(
    onNavigateToGame: (String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: BrowseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val games = viewModel.games.collectAsLazyPagingItems()
    val scrollState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    
    var isSearchFocused by remember { mutableStateOf(false) }
    var showFilters by remember { mutableStateOf(false) }
    var showSortOptions by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Modern Search Header
            SearchHeader(
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = viewModel::updateSearchQuery,
                isSearchFocused = isSearchFocused,
                onSearchFocusChange = { isSearchFocused = it },
                showFilters = showFilters,
                onToggleFilters = { showFilters = !showFilters },
                activeFilterCount = uiState.activeFilters.size,
                sortOption = uiState.sortOption,
                onShowSortOptions = { showSortOptions = true }
            )
            
            // Animated Filter Section
            AnimatedVisibility(
                visible = showFilters,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                FilterSection(
                    activeFilters = uiState.activeFilters,
                    availableFilters = uiState.availableFilters,
                    onFilterToggle = viewModel::toggleFilter,
                    onClearFilters = viewModel::clearFilters
                )
            }
            
            // Games Grid with Loading States
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    games.loadState.refresh is LoadState.Loading && games.itemCount == 0 -> {
                        LoadingState()
                    }
                    games.loadState.refresh is LoadState.Error -> {
                        ErrorState(
                            error = (games.loadState.refresh as LoadState.Error).error,
                            onRetry = { games.retry() }
                        )
                    }
                    games.itemCount == 0 -> {
                        EmptyState(searchQuery = uiState.searchQuery)
                    }
                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 160.dp),
                            state = scrollState,
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            ),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(
                                count = games.itemCount,
                                key = games.itemKey { it.id },
                                contentType = games.itemContentType { "game" }
                            ) { index ->
                                val game = games[index]
                                game?.let {
                                    GameCard(
                                        game = it,
                                        onClick = { onNavigateToGame(it.id) },
                                        index = index
                                    )
                                }
                            }
                            
                            // Load more indicator
                            if (games.loadState.append is LoadState.Loading) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            strokeWidth = 2.dp
                                        )
                                    }
                                }
                            }
                        }
                        
                        // Premium Floating scroll to top button with animations
                        if (scrollState.firstVisibleItemIndex > 5) {
                            FloatingActionButton(
                                onClick = {
                                    coroutineScope.launch {
                                        scrollState.animateScrollToItem(
                                            index = 0,
                                            scrollOffset = 0
                                        )
                                    }
                                },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(24.dp)
                                    .size(56.dp)
                                    .shadow(
                                        elevation = 12.dp,
                                        shape = CircleShape,
                                        clip = false
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Scroll to top",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Sort Options Bottom Sheet
        if (showSortOptions) {
            ModalBottomSheet(
                onDismissRequest = { showSortOptions = false },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                SortOptionsSheet(
                    currentSort = uiState.sortOption,
                    onSortSelected = { sort ->
                        viewModel.updateSortOption(sort)
                        showSortOptions = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isSearchFocused: Boolean,
    onSearchFocusChange: (Boolean) -> Unit,
    showFilters: Boolean,
    onToggleFilters: () -> Unit,
    activeFilterCount: Int,
    sortOption: SortOption,
    onShowSortOptions: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = if (isSearchFocused) 8.dp else 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp)),
                placeholder = {
                    Text(
                        text = "Search games...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChange("") }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
            
            // Action Buttons Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Filter Button with Badge
                AssistChip(
                    onClick = onToggleFilters,
                    label = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (showFilters) Icons.Default.Done else Icons.Default.Menu,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text("Filters")
                            if (activeFilterCount > 0) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    Text(
                                        text = activeFilterCount.toString(),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (showFilters) 
                            MaterialTheme.colorScheme.primaryContainer 
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                )
                
                // Sort Button
                AssistChip(
                    onClick = onShowSortOptions,
                    label = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(sortOption.displayName)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun FilterSection(
    activeFilters: Map<FilterType, Set<String>>,
    availableFilters: Map<FilterType, List<FilterOption>>,
    onFilterToggle: (FilterType, String) -> Unit,
    onClearFilters: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter by",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (activeFilters.values.any { it.isNotEmpty() }) {
                    TextButton(onClick = onClearFilters) {
                        Text("Clear all")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Filter Categories
            availableFilters.forEach { (filterType, options) ->
                FilterCategory(
                    title = filterType.displayName,
                    options = options,
                    selectedOptions = activeFilters[filterType] ?: emptySet(),
                    onOptionToggle = { optionId ->
                        onFilterToggle(filterType, optionId)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterCategory(
    title: String,
    options: List<FilterOption>,
    selectedOptions: Set<String>,
    onOptionToggle: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEachIndexed { index, option ->
                val animationDelay = index * 30L
                var isVisible by remember { mutableStateOf(false) }
                val haptic = LocalHapticFeedback.current
                
                LaunchedEffect(Unit) {
                    delay(animationDelay)
                    isVisible = true
                }
                
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(
                        animationSpec = tween(
                            EmuAnimations.Duration.NORMAL,
                            delayMillis = animationDelay.toInt()
                        )
                    ) + scaleIn(
                        animationSpec = tween(
                            EmuAnimations.Duration.NORMAL,
                            delayMillis = animationDelay.toInt(),
                            easing = EmuAnimations.BounceEasing
                        ),
                        initialScale = 0.8f
                    )
                ) {
                    FilterChip(
                        selected = option.id in selectedOptions,
                        onClick = { 
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onOptionToggle(option.id) 
                        },
                        label = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(option.name)
                                if (option.count > 0) {
                                    Text(
                                        text = "(${option.count})",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        leadingIcon = if (option.id in selectedOptions) {
                            {
                                AnimatedVisibility(
                                    visible = true,
                                    enter = scaleIn(
                                        animationSpec = tween(
                                            EmuAnimations.Duration.QUICK,
                                            easing = EmuAnimations.BounceEasing
                                        )
                                    ) + fadeIn()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        } else null,
                        modifier = Modifier.animateContentSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun GameCard(
    game: Game,
    onClick: () -> Unit,
    index: Int
) {
    val haptic = LocalHapticFeedback.current
    val animationDelay = (index * EmuAnimations.Duration.STAGGER_DELAY).toInt()
    var isVisible by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }
    var isHovered by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(animationDelay.toLong())
        isVisible = true
    }
    
    // Premium animation states
    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.95f
            isHovered -> 1.02f
            else -> 1f
        },
        animationSpec = EmuAnimations.CardSpring,
        label = "card_scale"
    )
    
    val elevation by animateFloatAsState(
        targetValue = when {
            isPressed -> 2f
            isHovered -> 12f
            else -> 6f
        },
        animationSpec = EmuAnimations.NormalTween,
        label = "card_elevation"
    )
    
    val borderAlpha by animateFloatAsState(
        targetValue = if (isHovered) 0.8f else 0f,
        animationSpec = EmuAnimations.QuickTween,
        label = "border_alpha"
    )
    
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = tween(
                EmuAnimations.Duration.NORMAL,
                delayMillis = animationDelay,
                easing = EmuAnimations.PremiumEaseOut
            ),
            initialOffsetY = { it / 2 }
        ) + fadeIn(
            animationSpec = tween(
                EmuAnimations.Duration.NORMAL,
                delayMillis = animationDelay,
                easing = EmuAnimations.PremiumEaseOut
            )
        ) + scaleIn(
            animationSpec = tween(
                EmuAnimations.Duration.NORMAL,
                delayMillis = animationDelay,
                easing = EmuAnimations.PremiumEaseOut
            ),
            initialScale = 0.8f
        )
    ) {
        Card(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    shadowElevation = elevation
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClickLabel = "Open ${game.title}"
                ) {
                    isPressed = !isPressed
                },
            elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp),
            shape = RoundedCornerShape(16.dp),
            border = if (borderAlpha > 0) {
                androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = borderAlpha)
                )
            } else null,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column {
                // Game Cover with Gradient Overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.75f)
                ) {
                    GameCoverImage(
                        imageUrl = game.coverImageUrl,
                        contentDescription = game.title,
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    // Bottom gradient for better text readability
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )
                    
                    // Listing count badge
                    if (game.totalListings > 0) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.List,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = game.totalListings.toString(),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
                
                // Game Info
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = game.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = game.system?.name ?: "Unknown System",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (game.averageCompatibility > 0) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = String.format("%.1f", game.averageCompatibility * 5),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SortOptionsSheet(
    currentSort: SortOption,
    onSortSelected: (SortOption) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "Sort by",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        )
        
        SortOption.values().forEach { sortOption ->
            ListItem(
                headlineContent = {
                    Text(sortOption.displayName)
                },
                leadingContent = {
                    RadioButton(
                        selected = sortOption == currentSort,
                        onClick = null
                    )
                },
                modifier = Modifier.clickable {
                    onSortSelected(sortOption)
                }
            )
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
                text = "Loading games...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorState(
    error: Throwable,
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
                text = "Something went wrong",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = error.message ?: "An unexpected error occurred",
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
private fun EmptyState(searchQuery: String) {
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
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = if (searchQuery.isNotEmpty()) {
                    "No games found for \"$searchQuery\""
                } else {
                    "No games found"
                },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = if (searchQuery.isNotEmpty()) {
                    "Try adjusting your search or filters"
                } else {
                    "Try adjusting your filters"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Data classes for filters
data class FilterOption(
    val id: String,
    val name: String,
    val count: Int = 0
)