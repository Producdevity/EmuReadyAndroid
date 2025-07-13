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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.emuready.emuready.domain.entities.Listing
import com.emuready.emuready.domain.entities.PerformanceLevel
import com.emuready.emuready.presentation.ui.theme.*
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.emuready.emuready.presentation.viewmodels.ListingsViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingsScreen(
    onNavigateToListing: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToCreate: () -> Unit = {},
    viewModel: ListingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listings = viewModel.listings.collectAsLazyPagingItems()

    // Use ViewModel state instead of local state
    var searchQuery by remember { mutableStateOf(uiState.searchQuery) }
    var selectedFilter by remember { mutableStateOf(uiState.selectedCategory) }
    var sortBy by remember { mutableStateOf(uiState.sortOption) }
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
        // Header with Search and Filters
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(AnimationDurations.NORMAL, easing = EasingCurves.Smooth)) + slideInVertically(
                initialOffsetY = { -it / 2 }
            )
        ) {
            ListingsHeader(
                searchQuery = searchQuery,
                onSearchQueryChange = {
                    searchQuery = it
                    viewModel.updateSearchQuery(it)
                },
                onNavigateBack = onNavigateBack,
                onNavigateToCreate = onNavigateToCreate
            )
        }

        // Filter and Sort Row
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = AnimationDurations.NORMAL,
                    delayMillis = StaggerAnimations.ItemDelay.toInt()
                )
            )
        ) {
            FilterSortRow(
                selectedFilter = selectedFilter,
                onFilterSelected = {
                    selectedFilter = it
                    viewModel.updateSelectedCategory(it)
                },
                sortBy = sortBy,
                onSortByChanged = {
                    sortBy = it
                    viewModel.updateSortOption(it)
                }
            )
        }

        // Listings List
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = AnimationDurations.NORMAL,
                    delayMillis = (StaggerAnimations.ItemDelay * 2).toInt()
                )
            )
        ) {
            ListingsList(
                listings = listings,
                onListingClick = onNavigateToListing,
                onNavigateToCreate = onNavigateToCreate
            )
        }
    }
}

@Composable
private fun ListingsHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToCreate: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                spotColor = SecondaryTeal.copy(alpha = 0.15f)
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
                        tint = Secondary
                    )
                }

                Text(
                    text = "Compatibility Listings",
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnSurface
                )

                IconButton(
                    onClick = { onNavigateToCreate() }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Create Listing",
                        tint = Secondary
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
                        text = "Search listings...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Secondary
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
                    focusedBorderColor = Secondary,
                    unfocusedBorderColor = Outline.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
private fun FilterSortRow(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    sortBy: String,
    onSortByChanged: (String) -> Unit
) {
    val filters = listOf("All", "Game-specific", "Device-specific", "Emulator-specific")
    val sortOptions = listOf("Default", "Recent")

    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleMedium,
                color = OnBackground
            )

            // Sort dropdown
            var showSortMenu by remember { mutableStateOf(false) }

            Box {
                TextButton(
                    onClick = { showSortMenu = true }
                ) {
                    Text(
                        text = "Sort: $sortBy",
                        style = CustomTextStyles.ButtonText,
                        color = Secondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Sort options",
                        tint = Secondary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false }
                ) {
                    sortOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onSortByChanged(option)
                                showSortMenu = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            itemsIndexed(filters) { index, filter ->
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
                    FilterChip(
                        text = filter,
                        isSelected = filter == selectedFilter,
                        onClick = { onFilterSelected(filter) },
                        color = SecondaryTeal
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    color: Color = Primary
) {
    val backgroundColor = if (isSelected) color else SurfaceElevated
    val textColor = if (isSelected) OnPrimary else OnSurface
    val borderColor = if (isSelected) color else Outline.copy(alpha = 0.5f)

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
        modifier = Modifier.scale(scale),
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
private fun ListingsList(
    listings: LazyPagingItems<Listing>,
    onListingClick: (String) -> Unit,
    onNavigateToCreate: () -> Unit
) {
    if (listings.itemCount > 0) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(listings.itemCount) { index ->
                val listing = listings[index]
                if (listing != null) {
                    var isVisible by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        delay(index * StaggerAnimations.ItemDelay)
                        isVisible = true
                    }

                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(AnimationDurations.FAST, easing = EasingCurves.Smooth)) + slideInHorizontally(
                            initialOffsetX = { it / 2 }
                        )
                    ) {
                        ListingCard(
                            listing = listing,
                            onClick = { onListingClick(listing.id) }
                        )
                    }
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
            EmptyListingsState(onNavigateToCreate)
        }
    }
}

@Composable
private fun ListingCard(
    listing: Listing,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) MicroAnimations.ButtonPressScale else 1f,
        animationSpec = SpringSpecs.Quick,
        label = "listing_card_scale"
    )

    val elevation by animateDpAsState(
        targetValue = if (isPressed) ElevationAnimations.CardPressed else ElevationAnimations.CardIdle,
        animationSpec = tween(AnimationDurations.FAST, easing = EasingCurves.Smooth),
        label = "listing_card_elevation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(20.dp),
                spotColor = SecondaryTeal.copy(alpha = 0.25f)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceElevated
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Performance Badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = when (listing.performanceLevel) {
                    PerformanceLevel.PERFECT -> SuccessGreen
                    PerformanceLevel.GREAT -> SecondaryTeal
                    PerformanceLevel.GOOD -> SecondaryTeal
                    PerformanceLevel.PLAYABLE -> AccentOrange
                    PerformanceLevel.POOR -> NeutralGray300
                    PerformanceLevel.UNPLAYABLE -> Color.Red
                },
                modifier = Modifier
                    .size(80.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = listing.performanceLevel.displayName,
                        style = CustomTextStyles.Caption,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Game ID: ${listing.gameId}",
                        style = CustomTextStyles.GameTitle,
                        color = OnSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Device: ${listing.deviceId}",
                        style = CustomTextStyles.GameSubtitle,
                        color = OnSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Verification badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (listing.isVerified) SuccessGreen.copy(alpha = 0.1f) else AccentOrange.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = if (listing.isVerified) "Verified" else "Community",
                            style = CustomTextStyles.Caption,
                            color = if (listing.isVerified) SuccessGreen else AccentOrange,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Score and votes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rating: ${String.format("%.1f", listing.overallRating)}",
                        style = CustomTextStyles.CardAccent,
                        color = Secondary
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.ThumbUp,
                            contentDescription = "Votes",
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${listing.likeCount + listing.dislikeCount} votes",
                            style = CustomTextStyles.Caption,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }

            // Verified badge
            if (listing.isVerified) {
                Icon(
                    Icons.Default.Verified,
                    contentDescription = "Verified",
                    tint = SuccessGreen,
                    modifier = Modifier.size(24.dp)
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
private fun EmptyListingsState(onNavigateToCreate: () -> Unit) {
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
            // Animated shopping icon
            var isAnimating by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                isAnimating = true
            }

            val scale by animateFloatAsState(
                targetValue = if (isAnimating) 1.1f else 0.9f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = EasingCurves.Smooth),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "shopping_scale"
            )

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SecondaryTeal.copy(alpha = 0.1f),
                modifier = Modifier
                    .size(80.dp)
                    .scale(scale)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Build,
                        contentDescription = "No listings",
                        tint = SecondaryTeal,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "No Listings Found",
                style = MaterialTheme.typography.headlineSmall,
                color = OnSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Be the first to create a listing in this category",
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onNavigateToCreate() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SecondaryTeal
                )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Create listing",
                    modifier = Modifier.size(18.dp)
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
