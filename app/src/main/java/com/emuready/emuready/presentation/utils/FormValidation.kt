package com.emuready.emuready.presentation.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.emuready.emuready.presentation.ui.theme.EmuAnimations
import kotlinx.coroutines.delay

// Form validation state
sealed class ValidationState {
    object Idle : ValidationState()
    object Validating : ValidationState()
    object Valid : ValidationState()
    data class Invalid(val message: String) : ValidationState()
}

// Validation rules
interface ValidationRule {
    fun validate(value: String): ValidationState
}

class RequiredRule(private val message: String = "This field is required") : ValidationRule {
    override fun validate(value: String): ValidationState {
        return if (value.isBlank()) ValidationState.Invalid(message) else ValidationState.Valid
    }
}

class EmailRule(private val message: String = "Please enter a valid email") : ValidationRule {
    override fun validate(value: String): ValidationState {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return if (value.matches(emailPattern.toRegex())) {
            ValidationState.Valid
        } else {
            ValidationState.Invalid(message)
        }
    }
}

class MinLengthRule(
    private val minLength: Int,
    private val message: String = "Must be at least $minLength characters"
) : ValidationRule {
    override fun validate(value: String): ValidationState {
        return if (value.length >= minLength) {
            ValidationState.Valid
        } else {
            ValidationState.Invalid(message.replace("$minLength", minLength.toString()))
        }
    }
}

class PasswordRule(
    private val message: String = "Password must contain at least 8 characters, one uppercase, one lowercase, and one number"
) : ValidationRule {
    override fun validate(value: String): ValidationState {
        val hasMinLength = value.length >= 8
        val hasUppercase = value.any { it.isUpperCase() }
        val hasLowercase = value.any { it.isLowerCase() }
        val hasNumber = value.any { it.isDigit() }
        
        return if (hasMinLength && hasUppercase && hasLowercase && hasNumber) {
            ValidationState.Valid
        } else {
            ValidationState.Invalid(message)
        }
    }
}

// Enhanced text field with sophisticated validation
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    rules: List<ValidationRule> = emptyList(),
    onValidationChange: (ValidationState) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var validationState by remember { mutableStateOf<ValidationState>(ValidationState.Idle) }
    var hasInteracted by remember { mutableStateOf(false) }
    
    // Validate on value change with debounce
    LaunchedEffect(value) {
        if (hasInteracted && value.isNotEmpty()) {
            validationState = ValidationState.Validating
            delay(300) // Debounce validation
            
            validationState = when {
                rules.isEmpty() -> ValidationState.Valid
                else -> {
                    rules.fold<ValidationRule, ValidationState>(ValidationState.Valid) { acc, rule ->
                        when (val result = rule.validate(value)) {
                            is ValidationState.Invalid -> return@fold result
                            else -> acc
                        }
                    }
                }
            }
            onValidationChange(validationState)
        }
    }
    
    // Animation states
    val borderColor by animateColorAsState(
        targetValue = when (validationState) {
            is ValidationState.Invalid -> MaterialTheme.colorScheme.error
            ValidationState.Valid -> MaterialTheme.colorScheme.primary
            ValidationState.Validating -> MaterialTheme.colorScheme.secondary
            else -> MaterialTheme.colorScheme.outline
        },
        animationSpec = tween(EmuAnimations.Duration.NORMAL),
        label = "border_color"
    )
    
    val iconScale by animateFloatAsState(
        targetValue = when (validationState) {
            ValidationState.Valid, is ValidationState.Invalid -> 1f
            else -> 0f
        },
        animationSpec = EmuAnimations.ButtonSpring,
        label = "icon_scale"
    )
    
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (!hasInteracted) hasInteracted = true
                onValueChange(newValue)
            },
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            },
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            },
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            trailingIcon = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Validation indicator
                    when (validationState) {
                        ValidationState.Validating -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        ValidationState.Valid -> {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Valid",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(20.dp)
                                    .scale(iconScale)
                            )
                        }
                        is ValidationState.Invalid -> {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Invalid",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .size(20.dp)
                                    .scale(iconScale)
                            )
                        }
                        else -> {}
                    }
                }
            },
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (validationState is ValidationState.Invalid) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor.copy(alpha = 0.6f),
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = borderColor,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )
        
        // Error message with smooth animation
        androidx.compose.animation.AnimatedVisibility(
            visible = validationState is ValidationState.Invalid,
            enter = androidx.compose.animation.slideInVertically(
                animationSpec = tween(EmuAnimations.Duration.QUICK)
            ) + androidx.compose.animation.fadeIn(),
            exit = androidx.compose.animation.slideOutVertically(
                animationSpec = tween(EmuAnimations.Duration.QUICK)
            ) + androidx.compose.animation.fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = (validationState as? ValidationState.Invalid)?.message ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Form validation composable for handling multiple fields
@Composable
fun rememberFormValidation(
    fields: Map<String, List<ValidationRule>>
): FormValidationState {
    var validationStates by remember {
        mutableStateOf(fields.mapValues { ValidationState.Idle as ValidationState })
    }
    
    val isValid = validationStates.values.all { it is ValidationState.Valid }
    val hasErrors = validationStates.values.any { it is ValidationState.Invalid }
    
    return FormValidationState(
        isValid = isValid,
        hasErrors = hasErrors,
        validationStates = validationStates,
        updateValidation = { fieldName, state ->
            validationStates = validationStates.toMutableMap().apply {
                put(fieldName, state)
            }
        }
    )
}

data class FormValidationState(
    val isValid: Boolean,
    val hasErrors: Boolean,
    val validationStates: Map<String, ValidationState>,
    val updateValidation: (String, ValidationState) -> Unit
)

// Enhanced submit button with validation feedback
@Composable
fun ValidatedSubmitButton(
    text: String,
    onClick: () -> Unit,
    isValid: Boolean,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    
    val buttonColor by animateColorAsState(
        targetValue = when {
            isLoading -> MaterialTheme.colorScheme.secondary
            isValid -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(EmuAnimations.Duration.NORMAL),
        label = "button_color"
    )
    
    val contentColor by animateColorAsState(
        targetValue = when {
            isLoading -> MaterialTheme.colorScheme.onSecondary
            isValid -> MaterialTheme.colorScheme.onPrimary
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(EmuAnimations.Duration.NORMAL),
        label = "content_color"
    )
    
    Button(
        onClick = {
            if (isValid && !isLoading) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            } else if (!isValid) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        },
        enabled = isValid && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = contentColor,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = contentColor
                )
            }
            
            Text(
                text = if (isLoading) "Processing..." else text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}