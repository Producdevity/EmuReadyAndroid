package com.emuready.emuready.presentation.components.cards

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.presentation.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedGameCard(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showRating: Boolean = true,
    showCompatibility: Boolean = true
) {
    var isPressed by remember { mutableStateOf(false) }
    var isHovered by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> MicroAnimations.ButtonPressScale
            isHovered -> MicroAnimations.CardHoverScale
            else -> 1f
        },
        animationSpec = SpringSpecs.Quick,
        label = "card_scale"
    )
    
    val elevation by animateDpAsState(
        targetValue = when {
            isPressed -> ElevationAnimations.CardPressed
            isHovered -> ElevationAnimations.CardHovered
            else -> ElevationAnimations.CardIdle
        },
        animationSpec = tween(AnimationDurations.FAST, easing = EasingCurves.Smooth),
        label = "card_elevation"
    )
    
    val interactionSource = remember { MutableInteractionSource() }
    
    Card(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(16.dp),
                spotColor = PrimaryPurple.copy(alpha = 0.25f)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceElevated
        )
    ) {
        Column {
            // Game Cover with Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                AsyncImage(
                    model = game.coverImageUrl,
                    contentDescription = game.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Gradient overlay for better text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                ),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )
                
                // Compatibility Badge
                if (showCompatibility && game.averageCompatibility > 0.7f) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SuccessGreen,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "✓ Compatible",
                            style = CustomTextStyles.Caption,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                
                // Rating Badge
                if (showRating && game.averageCompatibility > 0) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Black.copy(alpha = 0.7f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = AccentOrange,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = String.format("%.1f", game.averageCompatibility * 5),
                                style = CustomTextStyles.Caption,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            
            // Game Info Section
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Game Title
                Text(
                    text = game.title,
                    style = CustomTextStyles.GameTitle,
                    color = OnSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Developer/Publisher
                Text(
                    text = game.developer,
                    style = CustomTextStyles.GameSubtitle,
                    color = OnSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Genres Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(game.genres.take(3)) { genre ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PrimaryContainer.copy(alpha = 0.3f),
                            modifier = Modifier.animateItemPlacement()
                        ) {
                            Text(
                                text = genre,
                                style = CustomTextStyles.Caption,
                                color = OnPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Bottom Row with Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Listings Count
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${game.totalListings}",
                            style = CustomTextStyles.CardAccent,
                            color = Primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "listings",
                            style = CustomTextStyles.Caption,
                            color = OnSurfaceVariant
                        )
                    }
                    
                    // Favorite Button (animated)
                    var isFavorite by remember { mutableStateOf(game.isFavorite) }
                    val favoriteScale by animateFloatAsState(
                        targetValue = if (isFavorite) 1.2f else 1f,
                        animationSpec = SpringSpecs.Bouncy,
                        label = "favorite_scale"
                    )
                    
                    IconButton(
                        onClick = { 
                            isFavorite = !isFavorite 
                        },
                        modifier = Modifier.scale(favoriteScale)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) AccentOrange else OnSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}