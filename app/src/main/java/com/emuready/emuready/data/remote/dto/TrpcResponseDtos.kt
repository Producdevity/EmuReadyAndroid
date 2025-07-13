package com.emuready.emuready.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi

/**
 * All tRPC Response DTOs as specified in the API documentation
 */
@OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)

// Put everything in TrpcResponseDtos object to avoid conflicts
object TrpcResponseDtos {

// ========================================
// BASIC RESPONSE DTOS
// ========================================

@Serializable
data class SuccessResponse(
    val success: Boolean,
    val message: String? = null
)

@Serializable
data class CountResponse(
    val count: Int
)

@Serializable
data class ValidateTokenResponse(
    val valid: Boolean,
    val user: MobileUser? = null
)

@Serializable
data class AuthResponse(
    val success: Boolean,
    val user: MobileUser? = null,
    val token: String? = null,
    val refreshToken: String? = null,
    val message: String? = null
)

@Serializable
data class OAuthResponse(
    val authorizationUrl: String? = null,
    val success: Boolean = false,
    val message: String? = null
)

@Serializable
data class UserVoteResponse(
    val vote: Boolean? = null
)

@Serializable
data class VerifyDeveloperResponse(
    val isVerified: Boolean
)

@Serializable
data class ListingReportResponse(
    val id: String,
    val success: Boolean,
    val message: String
)

@Serializable
data class UserReportsInfo(
    val hasReports: Boolean,
    val reportCount: Int
)

@Serializable
data class TrustInfo(
    val trustScore: Float,
    val trustLevel: MobileTrustLevel,
    val userName: String? = null
)

// ========================================
// PAGINATION RESPONSE DTOS
// ========================================

@Serializable
data class MobileListingsResponse(
    val listings: List<MobileListing>,
    val pagination: PaginationInfo,
    val meta: TrpcMeta? = null
)

@Serializable
data class MobilePcListingsResponse(
    val listings: List<MobilePcListing>,
    val pagination: PaginationInfo,
    val meta: TrpcMeta? = null
)

@Serializable
data class MobileCommentsResponse(
    val comments: List<MobileComment>,
    val _count: CommentCount,
    val meta: TrpcMeta? = null
)

@Serializable
data class CommentCount(
    val comments: Int
)

@Serializable
data class HardwarePaginationResponse<T>(
    val items: List<T>, // cpus, gpus, socs
    val pagination: PaginationInfo,
    val meta: TrpcMeta? = null
)

@Serializable
data class MobileListingVerificationsResponse(
    val verifications: List<MobileVerification>
)

@Serializable
data class PaginationInfo(
    val total: Int,
    val pages: Int,
    val currentPage: Int,
    val limit: Int
)

// ========================================
// CORE ENTITY DTOS
// ========================================

@Serializable
data class MobileUser(
    val id: String,
    val name: String? = null,
    val role: String,
    val bio: String? = null,
    val createdAt: String // ISO8601 date
)

@Serializable
data class MobileListing(
    val id: String,
    val notes: String? = null,
    val status: ApprovalStatus,
    val createdAt: String,
    val updatedAt: String,
    val game: MobileGame,
    val device: MobileDevice,
    val emulator: MobileEmulator,
    val performance: MobilePerformance,
    val author: MobileAuthor,
    val customFieldValues: List<MobileCustomFieldValue>? = null,
    val _count: ListingCount,
    val successRate: Float,
    val userVote: Boolean? = null // Only present when authenticated
)

@Serializable
data class MobilePcListing(
    val id: String,
    val notes: String? = null,
    val status: ApprovalStatus,
    val memorySize: Int,
    val os: String,
    val osVersion: String,
    val createdAt: String,
    val updatedAt: String,
    val game: MobileGame,
    val cpu: MobileCpu,
    val gpu: MobileGpu,
    val emulator: MobileEmulator,
    val performance: MobilePerformance,
    val author: MobileAuthor,
    val customFieldValues: List<MobileCustomFieldValue>? = null,
    val _count: ListingCount,
    val successRate: Float,
    val userVote: Boolean? = null
)

@Serializable
data class ListingCount(
    val votes: Int,
    val comments: Int
)

@Serializable
data class MobileGame(
    val id: String,
    val title: String,
    val systemId: String? = null, // API includes this field
    val imageUrl: String? = null,
    val boxartUrl: String? = null,
    val bannerUrl: String? = null,
    val tgdbGameId: Int? = null, // API includes this field
    val status: String? = null, // API includes this field
    val submittedBy: String? = null, // API includes this field
    val submittedAt: String? = null, // API includes this field
    val approvedBy: String? = null, // API includes this field
    val approvedAt: String? = null, // API includes this field
    val createdAt: String? = null, // API includes this field
    val system: MobileSystem,
    val _count: GameCount
)

@Serializable
data class GameCount(
    val listings: Int
)

@Serializable
data class MobileDevice(
    val id: String,
    val modelName: String,
    val brand: MobileBrand,
    val soc: MobileSoc? = null,
    val _count: DeviceCount
)

@Serializable
data class DeviceCount(
    val listings: Int
)

@Serializable
data class MobileEmulator(
    val id: String,
    val name: String,
    val logo: String? = null,
    val systems: List<MobileSystem>? = null,
    val _count: EmulatorCount
)

@Serializable
data class EmulatorCount(
    val listings: Int
)

@Serializable
data class MobileComment(
    val id: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val author: MobileAuthor,
    val _count: VoteCount,
    val userVote: Boolean? = null
)

@Serializable
data class VoteCount(
    val votes: Int
)

@Serializable
data class MobilePcPreset(
    val id: String,
    val name: String,
    val memorySize: Int,
    val os: String,
    val osVersion: String,
    val createdAt: String,
    val cpu: MobileCpu,
    val gpu: MobileGpu
)

@Serializable
data class MobileNotification(
    val id: String,
    val title: String,
    val message: String,
    val type: String, // info, success, warning, error
    val read: Boolean,
    val createdAt: String
)

// ========================================
// SUPPORTING ENTITY DTOS
// ========================================

@Serializable
data class MobileSystem(
    val id: String,
    val name: String,
    val key: String? = null
)

@Serializable
data class MobileBrand(
    val id: String,
    val name: String
)

@Serializable
data class MobileSoc(
    val id: String,
    val name: String,
    val manufacturer: String
)

@Serializable
data class MobileCpu(
    val id: String,
    val name: String,
    val modelName: String,
    val brand: MobileBrand,
    val _count: CpuCount
)

@Serializable
data class CpuCount(
    val pcListings: Int
)

@Serializable
data class MobileGpu(
    val id: String,
    val name: String,
    val modelName: String,
    val brand: MobileBrand,
    val memorySize: Int? = null,
    val _count: GpuCount
)

@Serializable
data class GpuCount(
    val pcListings: Int
)

@Serializable
data class MobilePerformance(
    val id: String,
    val label: String,
    val rank: Int
)

@Serializable
data class MobileAuthor(
    val id: String,
    val name: String? = null
)

@Serializable
data class MobileCustomFieldValue(
    val id: String,
    val value: String,
    val customFieldDefinition: MobileCustomFieldDefinition
)

@Serializable
data class MobileCustomFieldDefinition(
    val id: String,
    val name: String,
    val label: String,
    val type: CustomFieldType,
    val options: String? = null, // JSON string for complex types
    val isRequired: Boolean
)

@Serializable
data class MobileStats(
    val totalListings: Int,
    val totalGames: Int,
    val totalDevices: Int,
    val totalEmulators: Int,
    val totalUsers: Int
)

@Serializable
data class MobileSearchSuggestion(
    val id: String,
    val title: String,
    val type: String, // game, device, emulator, system
    val subtitle: String? = null
)

@Serializable
data class MobileTrustLevel(
    val id: String,
    val name: String,
    val minScore: Float,
    val maxScore: Float,
    val color: String,
    val description: String
)

@Serializable
data class MobileDeviceBrand(
    val id: String,
    val name: String,
    val _count: DeviceBrandCount
)

@Serializable
data class DeviceBrandCount(
    val devices: Int
)

@Serializable
data class MobileEmulatorVerification(
    val id: String,
    val emulator: MobileEmulator,
    val verifiedAt: String
)

@Serializable
data class MobileVerification(
    val id: String,
    val listingId: String,
    val userId: String,
    val notes: String? = null,
    val createdAt: String
)

// ========================================
// USER PROFILE DTOS
// ========================================

@Serializable
data class MobileUserProfile(
    val id: String,
    val email: String? = null,
    val name: String? = null,
    val role: String,
    val bio: String? = null,
    val createdAt: String,
    val _count: UserProfileCount,
    val devicePreferences: List<DevicePreference>,
    val socPreferences: List<SocPreference>
)

@Serializable
data class UserProfileCount(
    val listings: Int,
    val votes: Int,
    val comments: Int
)

@Serializable
data class DevicePreference(
    val id: String,
    val device: MobileDevice
)

@Serializable
data class SocPreference(
    val id: String,
    val soc: MobileSoc
)

@Serializable
data class EnhancedMobileUserProfile(
    val id: String,
    val name: String? = null,
    val bio: String? = null,
    val profileImage: String? = null,
    val role: String,
    val trustScore: Float,
    val createdAt: String,
    val listings: List<MobileListing>,
    val votes: List<UserVote>,
    val _count: EnhancedUserProfileCount,
    val pagination: UserProfilePagination
)

@Serializable
data class UserVote(
    val id: String,
    val value: Boolean,
    val listing: MobileListing
)

@Serializable
data class EnhancedUserProfileCount(
    val listings: Int,
    val votes: Int,
    val submittedGames: Int
)

@Serializable
data class UserProfilePagination(
    val listings: PaginationInfo,
    val votes: PaginationInfo,
    val meta: TrpcMeta? = null
)

// ========================================
// EXTERNAL GAME DATA DTOS
// ========================================

@Serializable
data class GameImageOption(
    val id: String,
    val url: String,
    val type: String,
    val width: Int? = null,
    val height: Int? = null
)

@Serializable
data class RawgGameResponse(
    val results: List<RawgGame>
)

@Serializable
data class RawgGame(
    val id: Int,
    val name: String,
    val backgroundImage: String? = null
)

@Serializable
data class TgdbGameResponse(
    val games: List<TgdbGame>
)

@Serializable
data class TgdbGame(
    val id: Int,
    val gameName: String
)

@Serializable
data class GameImageUrls(
    val boxart: String? = null,
    val fanart: String? = null,
    val banner: String? = null
)

@Serializable
data class TgdbPlatform(
    val id: Int,
    val name: String
)

// ========================================
// ENUMS
// ========================================

@Serializable
enum class ApprovalStatus {
    PENDING,
    APPROVED,
    REJECTED
}

@Serializable
enum class CustomFieldType {
    TEXT,
    TEXTAREA,
    URL,
    BOOLEAN,
    SELECT,
    RANGE
}

@Serializable
enum class UserRole {
    USER,
    AUTHOR,
    DEVELOPER,
    MODERATOR,
    ADMIN,
    SUPER_ADMIN
}

// ========================================
// PAGINATED RESPONSE DTOS FOR TRPC
// ========================================

@Serializable
data class TrpcMeta(
    val values: Map<String, List<String>> = emptyMap()
)

@Serializable
data class TrpcPaginatedGamesResponse(
    val json: List<MobileGame>,
    val meta: TrpcMeta? = null
)

@Serializable
data class TrpcSingleGameResponse(
    val json: MobileGame,
    val meta: TrpcMeta? = null
)

@Serializable
data class TrpcStatsResponse(
    val json: MobileStats,
    val meta: TrpcMeta? = null
)

} // End of TrpcResponseDtos object
