package com.emuready.emuready.presentation.components.forms

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.presentation.components.GameCoverImage
import com.emuready.emuready.presentation.ui.theme.MicroAnimations
import com.emuready.emuready.presentation.ui.theme.SpringSpecs
import com.emuready.emuready.presentation.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSearchField(
    onSearchQueryChanged: (String) -> Unit,
    searchResults: List<Game>,
    onGameSelected: (Game) -> Unit,
    isLoading: Boolean = false,
    placeholder: String = "Search games...",
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    var hasFocus by remember { mutableStateOf(false) }
    
    // Debounced search
    LaunchedEffect(searchQuery) {
        if (searchQuery.length >= 2) {
            delay(300) // Debounce delay
            onSearchQueryChanged(searchQuery)
        } else {
            onSearchQueryChanged("")
        }
    }
    
    // Auto-expand when there are results
    LaunchedEffect(searchResults) {
        isExpanded = searchResults.isNotEmpty() && hasFocus
    }
    
    Column(
        modifier = modifier
    ) {
        // Search Input Field
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (hasFocus) 8.dp else 4.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { 
                    searchQuery = it
                    isExpanded = it.isNotEmpty()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                placeholder = {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                leadingIcon = {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = if (hasFocus) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { 
                                searchQuery = ""
                                onSearchQueryChanged("")
                                isExpanded = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }
        
        // Search Results Dropdown
        AnimatedVisibility(
            visible = isExpanded && (searchResults.isNotEmpty() || isLoading),
            enter = expandVertically(
                animationSpec = tween(AnimationDurations.NORMAL, easing = EasingCurves.Smooth)
            ) + fadeIn(
                animationSpec = tween(AnimationDurations.FAST)
            ),
            exit = shrinkVertically(
                animationSpec = tween(AnimationDurations.FAST, easing = EasingCurves.Smooth)
            ) + fadeOut(
                animationSpec = tween(AnimationDurations.FAST)
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (isLoading && searchResults.isEmpty()) {
                        item {
                            LoadingResultItem()
                        }
                    } else {
                        items(
                            items = searchResults,
                            key = { it.id }
                        ) { game ->
                            GameResultItem(
                                game = game,
                                onClick = {
                                    onGameSelected(game)
                                    searchQuery = game.title
                                    isExpanded = false
                                    hasFocus = false
                                }
                            )
                        }
                        
                        if (searchResults.isEmpty() && !isLoading && searchQuery.length >= 2) {
                            item {
                                NoResultsItem()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GameResultItem(
    game: Game,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) MicroAnimations.ButtonPressScale else 1f,
        animationSpec = SpringSpecs.Quick,
        label = "game_result_scale"
    )
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GameCoverImage(
                imageUrl = game.coverImageUrl,
                contentDescription = game.title,
                modifier = Modifier.size(48.dp)
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = game.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Text(
                    text = game.system?.name ?: "Unknown System",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (game.totalListings > 0) {
                    Text(
                        text = "${game.totalListings} listing${if (game.totalListings != 1) "s" else ""}",
                        style = CustomTextStyles.Caption,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
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
private fun LoadingResultItem() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "Searching games...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun NoResultsItem() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            
            Text(
                text = "No games found",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}