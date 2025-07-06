package com.emuready.emuready.data.remote.dto

import kotlinx.serialization.Serializable

// Core Types as specified in the API documentation

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
    val _count: MobileListingCount,
    val successRate: Double,
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
    val _count: MobileListingCount,
    val successRate: Double,
    val userVote: Boolean? = null // Only present when authenticated
)

@Serializable
data class MobileGame(
    val id: String,
    val title: String,
    val imageUrl: String? = null,
    val boxartUrl: String? = null,
    val bannerUrl: String? = null,
    val system: MobileSystem,
    val _count: MobileGameCount
)

@Serializable
data class MobileDevice(
    val id: String,
    val modelName: String,
    val brand: MobileBrand,
    val soc: MobileSoc? = null,
    val _count: MobileDeviceCount
)

@Serializable
data class MobileEmulator(
    val id: String,
    val name: String,
    val logo: String? = null,
    val systems: List<MobileSystem>? = null,
    val _count: MobileEmulatorCount
)

@Serializable
data class MobileComment(
    val id: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val author: MobileAuthor,
    val _count: MobileCommentCount,
    val userVote: Boolean? = null // Only present when authenticated
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
    val type: String, // 'info' | 'success' | 'warning' | 'error'
    val read: Boolean,
    val createdAt: String
)

// Supporting Types

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
    val brand: MobileBrand,
    val _count: MobileCpuCount
)

@Serializable
data class MobileGpu(
    val id: String,
    val name: String,
    val brand: MobileBrand,
    val memorySize: Int? = null,
    val _count: MobileGpuCount
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
    val options: String? = null, // JSON string
    val isRequired: Boolean
)

@Serializable
data class MobileUserProfile(
    val id: String,
    val email: String? = null,
    val name: String? = null,
    val role: String,
    val bio: String? = null,
    val createdAt: String,
    val _count: MobileUserProfileCount,
    val devicePreferences: List<MobileDevicePreference>,
    val socPreferences: List<MobileSocPreference>
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
    val type: String, // 'game' | 'device' | 'emulator' | 'system'
    val subtitle: String? = null
)

@Serializable
data class MobileTrustLevel(
    val id: String,
    val name: String,
    val minScore: Int,
    val maxScore: Int,
    val color: String,
    val description: String
)

// Count Types

@Serializable
data class MobileListingCount(
    val votes: Int,
    val comments: Int
)

@Serializable
data class MobileGameCount(
    val listings: Int
)

@Serializable
data class MobileDeviceCount(
    val listings: Int
)

@Serializable
data class MobileEmulatorCount(
    val listings: Int
)

@Serializable
data class MobileCommentCount(
    val votes: Int
)

@Serializable
data class MobileCpuCount(
    val pcListings: Int
)

@Serializable
data class MobileGpuCount(
    val pcListings: Int
)

@Serializable
data class MobileUserProfileCount(
    val listings: Int,
    val votes: Int,
    val comments: Int
)

// Preference Types

@Serializable
data class MobileDevicePreference(
    val id: String,
    val device: MobileDevice
)

@Serializable
data class MobileSocPreference(
    val id: String,
    val soc: MobileSoc
)

// Response Wrapper Types

@Serializable
data class MobileListingsResponse(
    val listings: List<MobileListing>,
    val pagination: MobilePagination
)

@Serializable
data class MobilePcListingsResponse(
    val listings: List<MobilePcListing>,
    val pagination: MobilePagination
)

@Serializable
data class MobileCommentsResponse(
    val comments: List<MobileComment>,
    val _count: MobileCommentsCount
)

@Serializable
data class MobilePagination(
    val total: Int,
    val pages: Int,
    val currentPage: Int,
    val limit: Int
)

@Serializable
data class MobileCommentsCount(
    val comments: Int
)

// Enums

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
enum class Role {
    USER,
    AUTHOR,
    DEVELOPER,
    MODERATOR,
    ADMIN,
    SUPER_ADMIN
}

// Input Schemas for API calls

@Serializable
data class GetListingsSchema(
    val page: Int? = 1,
    val limit: Int? = 20,
    val gameId: String? = null,
    val systemId: String? = null,
    val deviceId: String? = null,
    val emulatorId: String? = null,
    val search: String? = null
)

@Serializable
data class CreateListingSchema(
    val gameId: String,
    val deviceId: String,
    val emulatorId: String,
    val performanceId: Int,
    val notes: String? = null,
    val customFieldValues: List<CustomFieldValueInput>? = null
)

@Serializable
data class UpdateListingSchema(
    val id: String,
    val performanceId: Int? = null,
    val notes: String? = null,
    val customFieldValues: List<CustomFieldValueInput>? = null
)

@Serializable
data class CustomFieldValueInput(
    val customFieldDefinitionId: String,
    val value: String
)

@Serializable
data class GetPcListingsSchema(
    val page: Int? = 1,
    val limit: Int? = 20,
    val gameId: String? = null,
    val systemId: String? = null,
    val cpuId: String? = null,
    val gpuId: String? = null,
    val emulatorId: String? = null,
    val os: String? = null, // 'WINDOWS' | 'LINUX' | 'MACOS'
    val search: String? = null
)

@Serializable
data class CreatePcListingSchema(
    val gameId: String,
    val cpuId: String,
    val gpuId: String,
    val emulatorId: String,
    val performanceId: Int,
    val memorySize: Int, // GB, 1-256
    val os: String, // 'WINDOWS' | 'LINUX' | 'MACOS'
    val osVersion: String,
    val notes: String? = null,
    val customFieldValues: List<CustomFieldValueInput>? = null
)

@Serializable
data class CreatePcPresetSchema(
    val name: String, // 1-50 chars
    val cpuId: String,
    val gpuId: String,
    val memorySize: Int, // 1-256 GB
    val os: String, // 'WINDOWS' | 'LINUX' | 'MACOS'
    val osVersion: String
)

@Serializable
data class GetGamesSchema(
    val search: String? = null,
    val systemId: String? = null,
    val limit: Int? = 20 // Max: 50
)


@Serializable
data class GetDevicesSchema(
    val search: String? = null,
    val brandId: String? = null,
    val limit: Int? = 50 // Max: 100
)

@Serializable
data class GetEmulatorsSchema(
    val systemId: String? = null,
    val search: String? = null,
    val limit: Int? = 50 // Max: 100
)

@Serializable
data class GetNotificationsSchema(
    val page: Int? = 1,
    val limit: Int? = 20,
    val unreadOnly: Boolean? = false
)

@Serializable
data class UpdateUserPreferencesSchema(
    val defaultToUserDevices: Boolean? = null,
    val defaultToUserSocs: Boolean? = null,
    val notifyOnNewListings: Boolean? = null,
    val bio: String? = null
)

// Trust System Types

@Serializable
data class TrustInfo(
    val trustScore: Int,
    val trustLevel: MobileTrustLevel,
    val userName: String? = null
)

// Developer Verification Types

@Serializable
data class MobileVerification(
    val id: String,
    val verifierId: String,
    val listingId: String,
    val verifiedAt: String,
    val notes: String? = null,
    val emulator: MobileEmulator,
    val verifier: MobileAuthor
)

@Serializable
data class MobileListingVerificationsResponse(
    val verifications: List<MobileVerification>,
    val _count: MobileVerificationCount
)

@Serializable
data class MobileVerificationCount(
    val verifications: Int
)

// Content Reporting Types

@Serializable
data class CreateListingReportSchema(
    val listingId: String,
    val reason: ReportReason,
    val description: String
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

// External Game Data Types

@Serializable
data class GameImageOption(
    val id: String,
    val url: String,
    val type: String,
    val width: Int? = null,
    val height: Int? = null
)

@Serializable
data class SearchGameImagesSchema(
    val query: String,
    val includeScreenshots: Boolean? = false
)

@Serializable
data class SearchGamesSchema(
    val query: String,
    val page: Int? = 1,
    val pageSize: Int? = 10
)

@Serializable
data class GameImageUrls(
    val boxart: String? = null,
    val fanart: String? = null,
    val banner: String? = null,
    val clearlogo: String? = null,
    val screenshot: String? = null
)

@Serializable
data class RawgGameResponse(
    val results: List<RawgGame>,
    val count: Int,
    val next: String? = null,
    val previous: String? = null
)

@Serializable
data class RawgGame(
    val id: Int,
    val name: String,
    val released: String? = null,
    val background_image: String? = null,
    val rating: Double? = null
)

@Serializable
data class TgdbGameResponse(
    val data: TgdbGameData
)

@Serializable
data class TgdbGameData(
    val games: List<TgdbGame>,
    val count: Int,
    val pages: TgdbPagination
)

@Serializable
data class TgdbGame(
    val id: Int,
    val game_title: String,
    val release_date: String? = null,
    val platform: Int
)

@Serializable
data class TgdbPagination(
    val previous: String? = null,
    val current: String,
    val next: String? = null
)

@Serializable
data class TgdbPlatform(
    val id: Int,
    val name: String,
    val alias: String
)

// Enhanced Hardware Types

@Serializable
data class GetCpusSchema(
    val search: String? = null,
    val brandId: String? = null,
    val limit: Int? = 20,
    val offset: Int? = null,
    val page: Int? = null,
    val sortField: String? = null, // 'brand' | 'modelName'
    val sortDirection: String? = null // 'asc' | 'desc'
)

@Serializable
data class GetGpusSchema(
    val search: String? = null,
    val brandId: String? = null,
    val limit: Int? = 20,
    val offset: Int? = null,
    val page: Int? = null,
    val sortField: String? = null, // 'brand' | 'modelName'
    val sortDirection: String? = null // 'asc' | 'desc'
)

@Serializable
data class GetSoCsSchema(
    val search: String? = null,
    val limit: Int? = 20,
    val offset: Int? = null,
    val page: Int? = null,
    val sortField: String? = null, // 'name' | 'manufacturer'
    val sortDirection: String? = null // 'asc' | 'desc'
)

@Serializable
data class GetDeviceBrandsSchema(
    val search: String? = null,
    val limit: Int? = null,
    val sortField: String? = null, // 'name' | 'devicesCount'
    val sortDirection: String? = null // 'asc' | 'desc'
)

@Serializable
data class MobileDeviceBrand(
    val id: String,
    val name: String,
    val _count: MobileDeviceBrandCount
)

@Serializable
data class MobileDeviceBrandCount(
    val devices: Int
)

@Serializable
data class HardwarePaginationResponse<T>(
    val data: List<T>,
    val pagination: PaginationInfo
)

@Serializable
data class PaginationInfo(
    val page: Int,
    val limit: Int,
    val total: Int,
    val pages: Int
)

// Enhanced User Profile Types

@Serializable
data class GetUserByIdSchema(
    val userId: String,
    val listingsPage: Int? = 1,
    val listingsLimit: Int? = 12,
    val listingsSearch: String? = null,
    val listingsSystem: String? = null,
    val listingsEmulator: String? = null,
    val votesPage: Int? = 1,
    val votesLimit: Int? = 12,
    val votesSearch: String? = null
)

@Serializable
data class EnhancedMobileUserProfile(
    val id: String,
    val name: String? = null,
    val bio: String? = null,
    val profileImage: String? = null,
    val role: String,
    val trustScore: Int,
    val createdAt: String,
    val listings: List<MobileListing>,
    val votes: List<MobileUserVote>,
    val _count: EnhancedMobileUserProfileCount,
    val pagination: UserProfilePagination
)

@Serializable
data class MobileUserVote(
    val id: String,
    val value: Boolean,
    val listing: MobileListing
)

@Serializable
data class EnhancedMobileUserProfileCount(
    val listings: Int,
    val votes: Int,
    val submittedGames: Int
)

@Serializable
data class UserProfilePagination(
    val listings: PaginationInfo,
    val votes: PaginationInfo
)

// Additional Enums

@Serializable
enum class ReportReason {
    INAPPROPRIATE_CONTENT,
    SPAM,
    MISLEADING_INFORMATION,
    FAKE_LISTING,
    COPYRIGHT_VIOLATION,
    OTHER
}

// Simple Response Types

@Serializable
data class SuccessResponse(
    val success: Boolean
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
data class UserVoteResponse(
    val vote: Boolean? = null
)