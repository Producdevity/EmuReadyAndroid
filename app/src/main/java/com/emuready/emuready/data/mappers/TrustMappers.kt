package com.emuready.emuready.data.mappers

import com.emuready.emuready.data.remote.dto.*
import com.emuready.emuready.domain.entities.*

// Trust System Mappers

fun MobileTrustLevel.toDomain(): TrustLevel {
    return TrustLevel(
        id = this.id,
        name = this.name,
        minScore = this.minScore,
        maxScore = this.maxScore,
        color = this.color,
        description = this.description
    )
}

fun TrustInfo.toDomain(): UserTrustInfo {
    return UserTrustInfo(
        trustScore = this.trustScore,
        trustLevel = this.trustLevel.toDomain(),
        userName = this.userName
    )
}

// Developer Verification Mappers

fun MobileVerification.toDomain(): Verification {
    return Verification(
        id = this.id,
        verifierId = this.verifierId,
        listingId = this.listingId,
        verifiedAt = this.verifiedAt,
        notes = this.notes,
        emulator = this.emulator.toDomain(),
        verifier = this.verifier.toDomain()
    )
}

// Helper mappers for nested entities

fun MobileEmulator.toDomain(): Emulator {
    return Emulator(
        id = this.id,
        name = this.name,
        logoUrl = this.logo,
        supportedSystems = this.systems?.map { it.toGameSystem() } ?: emptyList()
    )
}

fun MobileAuthor.toDomain(): User {
    return User(
        id = this.id,
        username = this.name ?: "Anonymous",
        email = "", // Not provided in MobileAuthor
        avatarUrl = null,
        totalListings = 0, // Not provided
        totalLikes = 0, // Not provided  
        memberSince = java.lang.System.currentTimeMillis(), // Not provided
        isVerified = false, // Not provided
        lastActive = java.lang.System.currentTimeMillis() // Not provided
    )
}

fun MobileSystem.toGameSystem(): GameSystem {
    return GameSystem(
        id = this.id,
        name = this.name,
        key = this.key ?: this.name.lowercase().replace(" ", "_")
    )
}