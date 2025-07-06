package com.emuready.emuready.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.emuready.emuready.domain.entities.CustomFieldDefinition
import com.emuready.emuready.domain.entities.CustomFieldInput
import com.emuready.emuready.domain.entities.CustomFieldType
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFieldComponent(
    field: CustomFieldDefinition,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Field Label
        Text(
            text = field.label + if (field.isRequired) " *" else "",
            style = MaterialTheme.typography.labelLarge,
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
        
        // Field Input based on type
        when (field.type) {
            CustomFieldType.TEXT -> {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }
            
            CustomFieldType.TEXTAREA -> {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError,
                    minLines = 3,
                    maxLines = 6,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }
            
            CustomFieldType.URL -> {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    placeholder = { Text("https://example.com") }
                )
            }
            
            CustomFieldType.BOOLEAN -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Switch(
                        checked = value.equals("true", ignoreCase = true),
                        onCheckedChange = { onValueChange(it.toString()) }
                    )
                    Text(
                        text = if (value.equals("true", ignoreCase = true)) "Enabled" else "Disabled",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            CustomFieldType.SELECT -> {
                val options = parseSelectOptions(field.options)
                var expanded by remember { mutableStateOf(false) }
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = options.find { it.first == value }?.second ?: value,
                        onValueChange = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = true,
                        isError = isError,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { (optionValue, optionLabel) ->
                            DropdownMenuItem(
                                text = { Text(optionLabel) },
                                onClick = {
                                    onValueChange(optionValue)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            CustomFieldType.RANGE -> {
                val range = parseRangeOptions(field.options)
                val currentValue = value.toFloatOrNull() ?: range.first
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Slider(
                        value = currentValue,
                        onValueChange = { onValueChange(it.toInt().toString()) },
                        valueRange = range.first..range.second,
                        steps = (range.second - range.first).toInt() - 1
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = range.first.toInt().toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Current: ${currentValue.toInt()}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = range.second.toInt().toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        // Error message
        if (isError) {
            Text(
                text = "This field is required",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun CustomFieldsSection(
    customFields: List<CustomFieldDefinition>,
    fieldValues: Map<String, String>,
    onFieldValueChange: (String, String) -> Unit,
    validationErrors: Set<String> = emptySet(),
    modifier: Modifier = Modifier
) {
    if (customFields.isEmpty()) return
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Additional Configuration",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        customFields.forEach { field ->
            CustomFieldComponent(
                field = field,
                value = fieldValues[field.id] ?: "",
                onValueChange = { newValue ->
                    onFieldValueChange(field.id, newValue)
                },
                isError = field.isRequired && field.id in validationErrors
            )
        }
    }
}

// Helper functions for parsing field options

private fun parseSelectOptions(optionsJson: String?): List<Pair<String, String>> {
    if (optionsJson.isNullOrBlank()) return emptyList()
    
    return try {
        val json = Json { ignoreUnknownKeys = true }
        val options = json.decodeFromString<List<Map<String, String>>>(optionsJson)
        options.mapNotNull { option ->
            val value = option["value"]
            val label = option["label"]
            if (value != null && label != null) {
                value to label
            } else null
        }
    } catch (e: Exception) {
        // Fallback: parse as simple comma-separated values
        optionsJson.split(",").map { it.trim() }.map { it to it }
    }
}

private fun parseRangeOptions(optionsJson: String?): Pair<Float, Float> {
    if (optionsJson.isNullOrBlank()) return 0f to 100f
    
    return try {
        val json = Json { ignoreUnknownKeys = true }
        val options = json.decodeFromString<Map<String, Float>>(optionsJson)
        val min = options["min"] ?: 0f
        val max = options["max"] ?: 100f
        min to max
    } catch (e: Exception) {
        // Fallback: parse as "min-max" format
        val parts = optionsJson.split("-").map { it.trim().toFloatOrNull() ?: 0f }
        if (parts.size >= 2) {
            parts[0] to parts[1]
        } else {
            0f to 100f
        }
    }
}