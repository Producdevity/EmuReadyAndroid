package com.emuready.emuready.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)

@Serializable
data class PaginatedResponseDto<T>(
    val data: List<T>,
    val page: Int,
    val totalPages: Int,
    val totalItems: Int,
    val hasNext: Boolean
)

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val error: String? = null
)