package com.emuready.emuready.presentation.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.hilt.navigation.compose.hiltViewModel
import com.emuready.emuready.presentation.ui.theme.*
import com.emuready.emuready.presentation.viewmodels.ProfileViewModel
import com.emuready.emuready.presentation.viewmodels.ThemeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit = {},
    onNavigateToDevices: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val themePreferences by themeViewModel.themePreferences.collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    
    // Entry animations
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
                    )
                )
            )
    ) {
        // Header with gradient background and animations
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                animationSpec = tween(EmuAnimations.Duration.NORMAL, easing = EmuAnimations.PremiumEaseOut),
                initialOffsetY = { -it / 3 }
            ) + fadeIn(animationSpec = tween(EmuAnimations.Duration.NORMAL))
        ) {
            ProfileHeader(
                isAuthenticated = uiState.isAuthenticated,
                user = uiState.user,
                onSignInClick = onNavigateToLogin,
                onSignOutClick = viewModel::signOut
            )
        }
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            // Theme Settings Section with staggered animation
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(
                        animationSpec = tween(
                            EmuAnimations.Duration.NORMAL,
                            delayMillis = EmuAnimations.Duration.STAGGER_DELAY.toInt(),
                            easing = EmuAnimations.PremiumEaseOut
                        ),
                        initialOffsetY = { it / 2 }
                    ) + fadeIn(
                        animationSpec = tween(
                            EmuAnimations.Duration.NORMAL,
                            delayMillis = EmuAnimations.Duration.STAGGER_DELAY.toInt()
                        )
                    )
                ) {
                    SettingsSection(title = "Appearance") {
                    SettingsItem(
                        icon = Icons.Outlined.Palette,
                        title = "Theme",
                        subtitle = when (themePreferences.themeMode) {
                            ThemeMode.LIGHT -> "Light"
                            ThemeMode.DARK -> "Dark"
                            ThemeMode.SYSTEM -> "System default"
                        },
                        onClick = { showThemeDialog = true }
                    )
                    
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                        SettingsItem(
                            icon = Icons.Outlined.ColorLens,
                            title = "Dynamic Colors",
                            subtitle = "Use system accent colors",
                            trailing = {
                                Switch(
                                    checked = themePreferences.useDynamicColors,
                                    onCheckedChange = { enabled ->
                                        scope.launch {
                                            themeViewModel.setDynamicColors(enabled)
                                        }
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
            
            // Account Section (only show if authenticated)
            if (uiState.isAuthenticated) {
                item {
                    SettingsSection(title = "Account") {
                        SettingsItem(
                            icon = Icons.Outlined.Person,
                            title = "Profile Settings",
                            subtitle = "Edit your profile information",
                            onClick = onNavigateToSettings
                        )
                        
                        SettingsItem(
                            icon = Icons.Outlined.Security,
                            title = "Privacy & Security",
                            subtitle = "Manage your account security",
                            onClick = { /* TODO: Navigate to security settings */ }
                        )
                    }
                }
            }
            
            // Device Management Section
            item {
                SettingsSection(title = "Devices") {
                    SettingsItem(
                        icon = Icons.Outlined.DevicesOther,
                        title = "My Devices",
                        subtitle = "Manage registered gaming devices",
                        onClick = onNavigateToDevices
                    )
                    
                    SettingsItem(
                        icon = Icons.Outlined.Gamepad,
                        title = "Emulator Test",
                        subtitle = "Test Eden emulator integration",
                        onClick = { /* TODO: Navigate to emulator test */ }
                    )
                }
            }
            
            // App Settings Section
            item {
                SettingsSection(title = "App Settings") {
                    SettingsItem(
                        icon = Icons.Outlined.Notifications,
                        title = "Notifications",
                        subtitle = "Configure notification preferences",
                        onClick = { /* TODO: Navigate to notification settings */ }
                    )
                    
                    SettingsItem(
                        icon = Icons.Outlined.Download,
                        title = "Data & Storage",
                        subtitle = "Manage app data and cache",
                        onClick = { /* TODO: Navigate to storage settings */ }
                    )
                    
                    SettingsItem(
                        icon = Icons.Outlined.Info,
                        title = "About",
                        subtitle = "App version and legal information",
                        onClick = { /* TODO: Navigate to about screen */ }
                    )
                }
            }
        }
    }
    
    // Theme Selection Dialog with smooth animations
    if (showThemeDialog) {
        EnhancedThemeSelectionDialog(
            currentTheme = themePreferences.themeMode,
            onThemeSelected = { theme ->
                scope.launch {
                    themeViewModel.setThemeMode(theme)
                }
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }
}

@Composable
private fun ProfileHeader(
    isAuthenticated: Boolean,
    user: com.emuready.emuready.domain.entities.User?,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.md),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    )
                )
                .padding(Spacing.lg)
        ) {
            if (isAuthenticated && user != null) {
                // Authenticated User Profile
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(36.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    
                    // User Info
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = user.username,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                        
                        if (user.isVerified) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = "Verified",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                    
                    // Sign Out Button
                    FilledTonalButton(
                        onClick = onSignOutClick,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Sign Out")
                    }
                }
            } else {
                // Guest/Unauthenticated Profile
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Guest",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    
                    Spacer(modifier = Modifier.height(Spacing.md))
                    
                    Text(
                        text = "Welcome to EmuReady!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    
                    Text(
                        text = "Sign in to create listings, vote, and comment",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                    
                    Spacer(modifier = Modifier.height(Spacing.md))
                    
                    Button(
                        onClick = onSignInClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Login,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sign In")
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = Spacing.sm)
            )
            
            content()
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    val haptic = LocalHapticFeedback.current
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = EmuAnimations.ButtonSpring,
        label = "settings_item_scale"
    )
    
    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 2f else 4f,
        animationSpec = EmuAnimations.QuickTween,
        label = "settings_item_elevation"
    )
    
    Card(
        onClick = { 
            onClick?.let {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                it.invoke()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = !isPressed
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.extendedColors().surfaceElevated.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.md),
            horizontalArrangement = Arrangement.spacedBy(Spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (trailing != null) {
                trailing()
            } else if (onClick != null) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Navigate",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun EnhancedThemeSelectionDialog(
    currentTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Palette,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Choose Theme",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf(
                    Triple(ThemeMode.LIGHT, Icons.Outlined.LightMode, "Light"),
                    Triple(ThemeMode.DARK, Icons.Outlined.DarkMode, "Dark"),
                    Triple(ThemeMode.SYSTEM, Icons.Outlined.Brightness6, "System")
                ).forEach { (theme, icon, name) ->
                    val isSelected = currentTheme == theme
                    
                    val cardColor by animateColorAsState(
                        targetValue = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        },
                        animationSpec = tween(EmuAnimations.Duration.NORMAL),
                        label = "theme_card_color"
                    )
                    
                    val textColor by animateColorAsState(
                        targetValue = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        animationSpec = tween(EmuAnimations.Duration.NORMAL),
                        label = "theme_text_color"
                    )
                    
                    Card(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onThemeSelected(theme)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .premiumPressAnimation(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isSelected) 8.dp else 2.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                                modifier = Modifier.size(24.dp)
                            )
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textColor
                                )
                                Text(
                                    text = when (theme) {
                                        ThemeMode.LIGHT -> "Always use light theme"
                                        ThemeMode.DARK -> "Always use dark theme"
                                        ThemeMode.SYSTEM -> "Follow system setting"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = textColor.copy(alpha = 0.7f)
                                )
                            }
                            
                            AnimatedVisibility(
                                visible = isSelected,
                                enter = scaleIn(
                                    animationSpec = tween(
                                        EmuAnimations.Duration.NORMAL,
                                        easing = EmuAnimations.BounceEasing
                                    )
                                ) + fadeIn()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.premiumPressAnimation()
            ) {
                Text(
                    "Done",
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}