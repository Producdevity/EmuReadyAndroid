package com.emuready.emuready.domain.entities

data class User(
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

sealed class AuthState {
    object Unauthenticated : AuthState()
    data class Authenticated(val user: User) : AuthState()
    object Loading : AuthState()
}