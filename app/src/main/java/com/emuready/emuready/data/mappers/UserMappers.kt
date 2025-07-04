package com.emuready.emuready.data.mappers

import com.emuready.emuready.data.local.entities.UserEntity
import com.emuready.emuready.data.remote.dto.UserDto
import com.emuready.emuready.domain.entities.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        username = username,
        email = email,
        avatarUrl = avatarUrl,
        totalListings = totalListings,
        totalLikes = totalLikes,
        memberSince = memberSince,
        isVerified = isVerified,
        lastActive = lastActive
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        email = email,
        avatarUrl = avatarUrl,
        totalListings = totalListings,
        totalLikes = totalLikes,
        memberSince = memberSince,
        isVerified = isVerified,
        lastActive = lastActive
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        username = username,
        email = email,
        avatarUrl = avatarUrl,
        totalListings = totalListings,
        totalLikes = totalLikes,
        memberSince = memberSince,
        isVerified = isVerified,
        lastActive = lastActive
    )
}