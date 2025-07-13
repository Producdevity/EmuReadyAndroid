package com.emuready.emuready.data.remote.api.trpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)

/**
 * tRPC Request wrapper exactly as specified in the API documentation
 * Format: { "0": { "json": T } }
 */
@Serializable
data class TrpcRequest<T>(
    val `0`: TrpcRequestBody<T>
)

@Serializable
data class TrpcRequestBody<T>(
    val json: T
)

/**
 * tRPC Response wrapper exactly as returned by the actual API
 * Format: { "result": { "data": T } } or { "error": TrpcError }
 */
@Serializable
data class TrpcResponseWrapper<T>(
    val result: TrpcResultData<T>? = null,
    val error: TrpcError? = null
)

@Serializable
data class TrpcResultData<T>(
    val data: T
)

@Serializable
data class TrpcError(
    val code: String,
    val message: String,
    val data: TrpcErrorData? = null
)

@Serializable
data class TrpcErrorData(
    val code: String,
    val httpStatus: Int,
    val path: String
)

/**
 * tRPC Request Builder utility class
 */
class TrpcRequestBuilder {
    
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        allowStructuredMapKeys = true
    }
    
    fun <T> buildRequest(input: T): TrpcRequest<T> {
        return TrpcRequest(
            `0` = TrpcRequestBody(json = input)
        )
    }
    
    fun buildEmptyRequest(): TrpcRequest<Unit> {
        return TrpcRequest(
            `0` = TrpcRequestBody(json = Unit)
        )
    }
    
    /**
     * Build query parameter string for GET requests (batch format)
     */
    fun buildQueryParam(input: String): String {
        return """{"0":{"json":$input}}"""
    }
    
    fun buildEmptyQueryParam(): String {
        return """{"0":{"json":null}}"""
    }
}

/**
 * Result wrapper for API responses
 */
sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>(val exception: Exception, val code: String? = null) : ApiResult<T>()
}

/**
 * tRPC Exception for API errors
 */
class TrpcException(
    message: String,
    val code: String? = null,
    val httpStatus: Int? = null,
    cause: Throwable? = null
) : Exception(message, cause)