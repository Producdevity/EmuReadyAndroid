package com.emuready.emuready.domain.entities

data class TrustLevel(
    val id: String,
    val name: String,
    val minScore: Int,
    val maxScore: Int,
    val color: String,
    val description: String
)

data class UserTrustInfo(
    val trustScore: Int,
    val trustLevel: TrustLevel,
    val userName: String? = null
)

data class Verification(
    val id: String,
    val verifierId: String,
    val listingId: String,
    val verifiedAt: String,
    val notes: String? = null,
    val emulator: Emulator,
    val verifier: User
)

data class ListingReport(
    val id: String,
    val listingId: String,
    val reason: ReportReason,
    val description: String,
    val reporterId: String,
    val createdAt: String
)

enum class ReportReason {
    INAPPROPRIATE_CONTENT,
    SPAM,
    MISLEADING_INFORMATION,
    FAKE_LISTING,
    COPYRIGHT_VIOLATION,
    OTHER
}