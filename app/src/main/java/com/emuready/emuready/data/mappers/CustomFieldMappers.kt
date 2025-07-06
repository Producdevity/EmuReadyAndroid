package com.emuready.emuready.data.mappers

import com.emuready.emuready.data.remote.dto.*
import com.emuready.emuready.domain.entities.*

// Custom Field Definition Mappers

fun MobileCustomFieldDefinition.toDomain(): CustomFieldDefinition {
    return CustomFieldDefinition(
        id = this.id,
        name = this.name,
        label = this.label,
        type = this.type.toDomain(),
        options = this.options,
        isRequired = this.isRequired
    )
}

fun com.emuready.emuready.data.remote.dto.CustomFieldType.toDomain(): com.emuready.emuready.domain.entities.CustomFieldType {
    return when (this) {
        com.emuready.emuready.data.remote.dto.CustomFieldType.TEXT -> com.emuready.emuready.domain.entities.CustomFieldType.TEXT
        com.emuready.emuready.data.remote.dto.CustomFieldType.TEXTAREA -> com.emuready.emuready.domain.entities.CustomFieldType.TEXTAREA
        com.emuready.emuready.data.remote.dto.CustomFieldType.URL -> com.emuready.emuready.domain.entities.CustomFieldType.URL
        com.emuready.emuready.data.remote.dto.CustomFieldType.BOOLEAN -> com.emuready.emuready.domain.entities.CustomFieldType.BOOLEAN
        com.emuready.emuready.data.remote.dto.CustomFieldType.SELECT -> com.emuready.emuready.domain.entities.CustomFieldType.SELECT
        com.emuready.emuready.data.remote.dto.CustomFieldType.RANGE -> com.emuready.emuready.domain.entities.CustomFieldType.RANGE
    }
}

// Custom Field Value Mappers

fun MobileCustomFieldValue.toDomain(): CustomFieldValue {
    return CustomFieldValue(
        id = this.id,
        value = this.value,
        customFieldDefinition = this.customFieldDefinition.toDomain()
    )
}

// Input Mappers

fun CustomFieldInput.toDto(): CustomFieldValueInput {
    return CustomFieldValueInput(
        customFieldDefinitionId = this.customFieldDefinitionId,
        value = this.value
    )
}

fun com.emuready.emuready.domain.entities.CustomFieldType.toDto(): com.emuready.emuready.data.remote.dto.CustomFieldType {
    return when (this) {
        com.emuready.emuready.domain.entities.CustomFieldType.TEXT -> com.emuready.emuready.data.remote.dto.CustomFieldType.TEXT
        com.emuready.emuready.domain.entities.CustomFieldType.TEXTAREA -> com.emuready.emuready.data.remote.dto.CustomFieldType.TEXTAREA
        com.emuready.emuready.domain.entities.CustomFieldType.URL -> com.emuready.emuready.data.remote.dto.CustomFieldType.URL
        com.emuready.emuready.domain.entities.CustomFieldType.BOOLEAN -> com.emuready.emuready.data.remote.dto.CustomFieldType.BOOLEAN
        com.emuready.emuready.domain.entities.CustomFieldType.SELECT -> com.emuready.emuready.data.remote.dto.CustomFieldType.SELECT
        com.emuready.emuready.domain.entities.CustomFieldType.RANGE -> com.emuready.emuready.data.remote.dto.CustomFieldType.RANGE
    }
}