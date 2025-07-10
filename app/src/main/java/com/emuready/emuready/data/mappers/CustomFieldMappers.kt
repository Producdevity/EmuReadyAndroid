package com.emuready.emuready.data.mappers

import com.emuready.emuready.data.remote.dto.TrpcResponseDtos
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
import com.emuready.emuready.domain.entities.*

// Custom Field Definition Mappers

// Function moved to GameMappers.kt to avoid conflicts

// TODO: CustomFieldType conversion disabled until proper DTOs are available
// fun TrpcResponseDtos.CustomFieldType.toDomain(): com.emuready.emuready.domain.entities.CustomFieldType {
//     return when (this) {
//         TrpcResponseDtos.CustomFieldType.TEXT -> com.emuready.emuready.domain.entities.CustomFieldType.TEXT
//         TrpcResponseDtos.CustomFieldType.TEXTAREA -> com.emuready.emuready.domain.entities.CustomFieldType.TEXTAREA
//         TrpcResponseDtos.CustomFieldType.URL -> com.emuready.emuready.domain.entities.CustomFieldType.URL
//         TrpcResponseDtos.CustomFieldType.BOOLEAN -> com.emuready.emuready.domain.entities.CustomFieldType.BOOLEAN
//         TrpcResponseDtos.CustomFieldType.SELECT -> com.emuready.emuready.domain.entities.CustomFieldType.SELECT
//         TrpcResponseDtos.CustomFieldType.RANGE -> com.emuready.emuready.domain.entities.CustomFieldType.RANGE
//     }
// }

// Custom Field Value Mappers

fun TrpcResponseDtos.MobileCustomFieldValue.toDomain(): CustomFieldValue {
    return CustomFieldValue(
        id = this.id,
        value = this.value,
        customFieldDefinition = this.customFieldDefinition.toDomain()
    )
}

// Input Mappers

fun CustomFieldInput.toDto(): TrpcRequestDtos.CustomFieldValueInput {
    return TrpcRequestDtos.CustomFieldValueInput(
        customFieldDefinitionId = this.customFieldDefinitionId,
        value = this.value
    )
}

// TODO: CustomFieldType conversion disabled until proper DTOs are available
// fun com.emuready.emuready.domain.entities.CustomFieldType.toDto(): TrpcRequestDtos.CustomFieldType {
//     return when (this) {
//         com.emuready.emuready.domain.entities.CustomFieldType.TEXT -> TrpcRequestDtos.CustomFieldType.TEXT
//         com.emuready.emuready.domain.entities.CustomFieldType.TEXTAREA -> TrpcRequestDtos.CustomFieldType.TEXTAREA
//         com.emuready.emuready.domain.entities.CustomFieldType.URL -> TrpcRequestDtos.CustomFieldType.URL
//         com.emuready.emuready.domain.entities.CustomFieldType.BOOLEAN -> TrpcRequestDtos.CustomFieldType.BOOLEAN
//         com.emuready.emuready.domain.entities.CustomFieldType.SELECT -> TrpcRequestDtos.CustomFieldType.SELECT
//         com.emuready.emuready.domain.entities.CustomFieldType.RANGE -> TrpcRequestDtos.CustomFieldType.RANGE
//     }
// }