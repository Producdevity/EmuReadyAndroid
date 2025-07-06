package com.emuready.emuready.data.remote.api.trpc

import kotlinx.serialization.Serializable

/**
 * tRPC Request wrapper as specified in the API documentation
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
 * tRPC Response wrapper as specified in the API documentation
 */
@Serializable
data class TrpcResponse<T>(
    val `0`: TrpcResult<T>
)

@Serializable
data class TrpcResult<T>(
    val result: TrpcData<T>? = null,
    val error: TrpcError? = null
)

@Serializable
data class TrpcData<T>(
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
    fun <T> buildQuery(input: T): TrpcRequest<T> {
        return TrpcRequest(
            `0` = TrpcRequestBody(json = input)
        )
    }
    
    fun buildEmptyQuery(): TrpcRequest<Unit> {
        return TrpcRequest(
            `0` = TrpcRequestBody(json = Unit)
        )
    }
}