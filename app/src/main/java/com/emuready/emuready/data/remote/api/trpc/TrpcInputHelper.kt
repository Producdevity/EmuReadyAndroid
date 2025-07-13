package com.emuready.emuready.data.remote.api.trpc

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Helper class for creating tRPC input parameters in the correct SuperJSON format
 * 
 * tRPC expects input in the format: input={"json":{...}} (URL-encoded)
 * This matches the React Native implementation and the working curl command format.
 */
@OptIn(ExperimentalSerializationApi::class)
object TrpcInputHelper {
    
    val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = false  // Don't encode default values (including nulls)
        explicitNulls = false  // Don't include null values in serialized JSON
    }
    
    /**
     * Creates a tRPC input parameter for GET requests with input data
     * 
     * @param data The input data object to wrap in SuperJSON format
     * @return JSON string in format: {"json":{...}} (NOT URL-encoded - Retrofit will handle that)
     */
    inline fun <reified T> createInput(data: T): String {
        val superJsonWrapper = mapOf("json" to data)
        return json.encodeToString(superJsonWrapper)
    }
    
    /**
     * Creates a tRPC input parameter for GET requests without input data
     * Some endpoints don't require input parameters (e.g., getPopularGames, getFeaturedListings)
     * 
     * @return null since no input is needed
     */
    fun createEmptyInput(): String? = null
}