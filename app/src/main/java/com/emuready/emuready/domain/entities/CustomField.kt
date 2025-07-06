package com.emuready.emuready.domain.entities

data class CustomFieldDefinition(
    val id: String,
    val name: String,
    val label: String,
    val type: CustomFieldType,
    val options: String?, // JSON string for select options
    val isRequired: Boolean
)

data class CustomFieldValue(
    val id: String,
    val value: String,
    val customFieldDefinition: CustomFieldDefinition
)

enum class CustomFieldType {
    TEXT,
    TEXTAREA,
    URL,
    BOOLEAN,
    SELECT,
    RANGE
}

data class CustomFieldInput(
    val customFieldDefinitionId: String,
    val value: String
)