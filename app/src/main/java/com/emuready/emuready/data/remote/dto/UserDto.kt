package com.emuready.emuready.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val totalListings: Int,
    val totalLikes: Int,
    val memberSince: Long,
    val isVerified: Boolean,
    val lastActive: Long
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val user: UserDto,
    val token: String,
    val refreshToken: String
)