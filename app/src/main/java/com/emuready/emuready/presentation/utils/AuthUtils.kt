package com.emuready.emuready.presentation.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextAlign
import com.emuready.emuready.domain.entities.AuthState
import kotlinx.coroutines.flow.StateFlow

/**
 * Utility for handling authentication requirements in UI actions
 */
object AuthUtils {
    
    /**
     * Execute an action if authenticated, otherwise show sign-in prompt
     */
    fun requireAuth(
        authState: AuthState,
        onAuthenticated: () -> Unit,
        onSignInRequired: () -> Unit
    ) {
        when (authState) {
            is AuthState.Authenticated -> onAuthenticated()
            is AuthState.Unauthenticated -> onSignInRequired()
            is AuthState.Loading -> {
                // Wait for auth state to resolve
            }
        }
    }
}

/**
 * Composable that shows authentication requirement dialog
 */
@Composable
fun AuthRequiredDialog(
    isVisible: Boolean,
    title: String,
    message: String,
    onSignIn: () -> Unit,
    onDismiss: () -> Unit
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = title,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = message,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(onClick = onSignIn) {
                    Text("Sign In")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}

/**
 * Composable hook for handling authentication requirements
 */
@Composable
fun rememberAuthHandler(
    authStateFlow: StateFlow<AuthState>,
    onNavigateToSignIn: () -> Unit
): (action: String, onAuthenticated: () -> Unit) -> Unit {
    val authState by authStateFlow.collectAsState()
    var showAuthDialog by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }
    var actionName by remember { mutableStateOf("") }
    
    // Auth required dialog
    AuthRequiredDialog(
        isVisible = showAuthDialog,
        title = "Sign In Required",
        message = "You need to sign in to $actionName. Sign in now?",
        onSignIn = {
            showAuthDialog = false
            onNavigateToSignIn()
        },
        onDismiss = {
            showAuthDialog = false
            pendingAction = null
        }
    )
    
    return { action, onAuthenticated ->
        AuthUtils.requireAuth(
            authState = authState,
            onAuthenticated = onAuthenticated,
            onSignInRequired = {
                actionName = action
                pendingAction = onAuthenticated
                showAuthDialog = true
            }
        )
    }
}