package com.emuready.emuready.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val totalListings: Int,
    val totalLikes: Int,
    val memberSince: Long,
    val isVerified: Boolean,
    val lastActive: Long
)