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
import com.emuready.emuready.presentation.ui.theme.*
import com.emuready.emuready.presentation.viewmodels.HomeViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingsScreen(
    onNavigateToListing: (String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var sortBy by remember { mutableStateOf("Recent") }
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
                onSearchQueryChange = { searchQuery = it },
                onNavigateBack = onNavigateBack
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
                onFilterSelected = { selectedFilter = it },
                sortBy = sortBy,
                onSortByChanged = { sortBy = it }
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
                listings = emptyList(), // Replace with actual listings from uiState
                onListingClick = onNavigateToListing
            )
        }
    }
}

@Composable
private fun ListingsHeader(
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
                    text = "Marketplace",
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnSurface
                )
                
                IconButton(
                    onClick = { /* Create new listing */ }
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
    val filters = listOf("All", "Devices", "Games", "Accessories", "Bundles")
    val sortOptions = listOf("Recent", "Price Low", "Price High", "Popular")
    
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
    listings: List<Listing>,
    onListingClick: (String) -> Unit
) {
    if (listings.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            itemsIndexed(listings) { index, listing ->
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
    } else {
        // Empty State
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            contentAlignment = Alignment.Center
        ) {
            EmptyListingsState()
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
            // Image
            AsyncImage(
                model = listing.imageUrl,
                contentDescription = listing.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            
            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = listing.title,
                        style = CustomTextStyles.GameTitle,
                        color = OnSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = listing.seller,
                        style = CustomTextStyles.GameSubtitle,
                        color = OnSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Condition badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = when (listing.condition) {
                            "New" -> SuccessGreen.copy(alpha = 0.1f)
                            "Like New" -> SecondaryTeal.copy(alpha = 0.1f)
                            "Good" -> AccentOrange.copy(alpha = 0.1f)
                            else -> NeutralGray300.copy(alpha = 0.1f)
                        }
                    ) {
                        Text(
                            text = listing.condition,
                            style = CustomTextStyles.Caption,
                            color = when (listing.condition) {
                                "New" -> SuccessGreen
                                "Like New" -> SecondaryTeal
                                "Good" -> AccentOrange
                                else -> NeutralGray600
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                
                // Price and actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = NumberFormat.getCurrencyInstance(Locale.US).format(listing.price),
                        style = CustomTextStyles.CardAccent,
                        color = Secondary
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = listing.location,
                            style = CustomTextStyles.Caption,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }
            
            // Favorite button
            IconButton(
                onClick = { /* Toggle favorite */ }
            ) {
                Icon(
                    if (listing.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (listing.isFavorite) AccentPink else OnSurfaceVariant
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
private fun EmptyListingsState() {
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
                        Icons.Default.ShoppingCart,
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
                onClick = { /* Create listing */ },
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