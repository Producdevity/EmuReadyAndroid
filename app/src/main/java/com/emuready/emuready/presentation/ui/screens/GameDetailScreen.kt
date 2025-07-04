package com.emuready.emuready.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emuready.emuready.presentation.components.GameCoverImage
import com.emuready.emuready.presentation.components.GameScreenshotImage
import com.emuready.emuready.presentation.ui.theme.Spacing
import com.emuready.emuready.presentation.viewmodels.GameDetailViewModel

@Composable
fun GameDetailScreen(
    gameId: String,
    onNavigateBack: () -> Unit,
    onNavigateToListing: (String) -> Unit,
    onNavigateToCreate: () -> Unit,
    viewModel: GameDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(gameId) {
        viewModel.loadGameDetails(gameId)
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(Spacing.md),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.md)
                ) {
                    GameCoverImage(
                        imageUrl = uiState.game?.coverImageUrl,
                        contentDescription = uiState.game?.title,
                        size = 120.dp
                    )
                    
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = uiState.game?.title ?: "Loading...",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = uiState.game?.developer ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(Spacing.sm))
                        Text(
                            text = "Publisher: ${uiState.game?.publisher ?: ""}",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 4
                        )
                    }
                }
            }
        }
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(Spacing.md)
                ) {
                    Text(
                        text = "Screenshots",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(Spacing.sm))
                    
                    // Screenshots section would be implemented when screenshots data is available
                    Text(
                        text = "Genres: ${uiState.game?.genres?.joinToString(", ") ?: ""}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    // Additional game information would be displayed here
                }
            }
        }
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(Spacing.md)
                ) {
                    Text(
                        text = "Compatibility Ratings",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(Spacing.sm))
                    Text(
                        text = "Community compatibility ratings will be displayed here",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        item {
            Button(
                onClick = onNavigateToCreate,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Listing for This Game")
            }
        }
    }
}