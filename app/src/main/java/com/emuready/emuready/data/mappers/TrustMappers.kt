package com.emuready.emuready.data.mappers

import com.emuready.emuready.data.remote.dto.TrpcResponseDtos
import com.emuready.emuready.domain.entities.*

// Trust System Mappers

fun TrpcResponseDtos.MobileTrustLevel.toDomain(): TrustLevel {
    return TrustLevel(
        id = this.id,
        name = this.name,
        minScore = this.minScore.toInt(),
        maxScore = this.maxScore.toInt(),
        color = this.color,
        description = this.description
    )
}

fun TrpcResponseDtos.TrustInfo.toDomain(): UserTrustInfo {
    return UserTrustInfo(
        trustScore = this.trustScore.toInt(),
        trustLevel = this.trustLevel.toDomain(),
        userName = this.userName
    )
}

// Developer Verification Mappers

fun TrpcResponseDtos.MobileVerification.toDomain(): Verification {
    return Verification(
        id = this.id,
        verifierId = this.userId, // Using userId as verifierId 
        listingId = this.listingId,
        verifiedAt = this.createdAt, // Using createdAt as verifiedAt
        notes = this.notes,
        emulator = Emulator(
            id = "",
            name = "Unknown",
            logoUrl = null,
            supportedSystems = emptyList(),
            customFields = emptyList()
        ),
        verifier = User(
            id = "",
            username = "Unknown",
            email = "",
            avatarUrl = null,
            totalListings = 0,
            totalLikes = 0,
            memberSince = 0L,
            isVerified = false,
            lastActive = 0L
        )
    )
}

// Note: MobileEmulator, MobileAuthor, and MobileSystem mappers are already defined in GameMappers.kt