package com.emuready.emuready.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * All tRPC Request DTOs as specified in the API documentation
 */

// Put everything in TrpcRequestDtos object to avoid conflicts
object TrpcRequestDtos {

// ========================================
// AUTHENTICATION REQUEST DTOS
// ========================================

@Serializable
data class ValidateTokenRequest(
    val token: String
)

@Serializable
data class MobileSignInRequest(
    val email: String,
    val password: String
)

@Serializable
data class MobileSignUpRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)

@Serializable
data class MobileOAuthSignInRequest(
    val provider: String, // "google", "apple", "github", "discord"
    val redirectUrl: String? = null
)

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)

@Serializable
data class VerifyEmailRequest(
    val code: String,
    val clerkUserId: String
)

@Serializable
data class ForgotPasswordRequest(
    val email: String
)

@Serializable
data class ResetPasswordRequest(
    val code: String,
    val password: String,
    val clerkUserId: String
)

@Serializable
data class UpdateMobileProfileRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val profileImageUrl: String? = null
)

@Serializable
data class ChangeMobilePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)

@Serializable
data class DeleteMobileAccountRequest(
    val confirmationText: String // Must be "DELETE"
)

@Serializable
data class UpdateProfileRequest(
    val name: String? = null,
    val bio: String? = null
)

// ========================================
// BASIC REQUEST DTOS
// ========================================

@Serializable
data class LimitRequest(
    val limit: Int? = 10
)

@Serializable
data class GameIdRequest(
    val gameId: String
)

@Serializable
data class IdRequest(
    val id: String
)

@Serializable
data class UserIdRequest(
    val userId: String
)

@Serializable
data class ListingIdRequest(
    val listingId: String
)

@Serializable
data class QueryRequest(
    val query: String
)

// ========================================
// LISTINGS REQUEST DTOS
// ========================================

@Serializable
data class GetListingsSchema(
    val offset: Int? = 0,
    val limit: Int? = 20,
    val gameId: String? = null,
    val systemId: String? = null,
    val deviceId: String? = null,
    val emulatorId: String? = null,
    val search: String? = null
)

@Serializable
data class GetGamesSchema(
    val offset: Int? = 0,
    val limit: Int? = 12,
    val hideGamesWithNoListings: Boolean? = false,
    val search: String? = null,
    val systemId: String? = null
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
data class VoteRequest(
    val listingId: String,
    val value: Boolean
)

@Serializable
data class DeleteListingSchema(
    val id: String
)

@Serializable
data class VoteListingSchema(
    val listingId: String,
    val value: Boolean
)

// ========================================
// COMMENTS REQUEST DTOS
// ========================================

@Serializable
data class CreateCommentRequest(
    val listingId: String,
    val content: String
)

@Serializable
data class CreateCommentSchema(
    val listingId: String,
    val content: String
)

@Serializable
data class UpdateCommentRequest(
    val commentId: String,
    val content: String
)

@Serializable
data class UpdateCommentSchema(
    val commentId: String,
    val content: String
)

@Serializable
data class DeleteCommentSchema(
    val commentId: String
)

@Serializable
data class CommentIdRequest(
    val commentId: String
)

// ========================================
// PC LISTINGS REQUEST DTOS
// ========================================

@Serializable
data class GetPcListingsSchema(
    val page: Int? = 1,
    val limit: Int? = 20,
    val gameId: String? = null,
    val systemId: String? = null,
    val cpuId: String? = null,
    val gpuId: String? = null,
    val emulatorId: String? = null,
    val os: String? = null, // WINDOWS, LINUX, MACOS
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
    val os: String, // WINDOWS, LINUX, MACOS
    val osVersion: String,
    val notes: String? = null,
    val customFieldValues: List<CustomFieldValueInput>? = null
)

@Serializable
data class UpdatePcListingSchema(
    val id: String,
    val performanceId: Int? = null,
    val memorySize: Int? = null,
    val os: String? = null,
    val osVersion: String? = null,
    val notes: String? = null,
    val customFieldValues: List<CustomFieldValueInput>? = null
)

@Serializable
data class SearchWithBrandRequest(
    val search: String? = null,
    val brandId: String? = null,
    val limit: Int? = 50
)

// ========================================
// PC PRESETS REQUEST DTOS
// ========================================

@Serializable
data class CreatePcPresetSchema(
    val name: String, // 1-50 chars
    val cpuId: String,
    val gpuId: String,
    val memorySize: Int, // 1-256 GB
    val os: String, // WINDOWS, LINUX, MACOS
    val osVersion: String
)

@Serializable
data class UpdatePcPresetSchema(
    val id: String,
    val name: String? = null,
    val cpuId: String? = null,
    val gpuId: String? = null,
    val memorySize: Int? = null,
    val os: String? = null,
    val osVersion: String? = null
)

@Serializable
data class DeletePcPresetSchema(
    val id: String
)

// ========================================
// GAMES REQUEST DTOS (additional params)
// ========================================

@Serializable
data class BasicGetGamesSchema(
    val search: String? = null,
    val systemId: String? = null,
    val limit: Int? = 20
)

// ========================================
// DEVICES REQUEST DTOS
// ========================================

@Serializable
data class GetDevicesSchema(
    val search: String? = null,
    val brandId: String? = null,
    val limit: Int? = 50
)

// ========================================
// EMULATORS REQUEST DTOS
// ========================================

@Serializable
data class GetEmulatorsSchema(
    val systemId: String? = null,
    val search: String? = null,
    val limit: Int? = 50
)

// ========================================
// NOTIFICATIONS REQUEST DTOS
// ========================================

@Serializable
data class GetNotificationsSchema(
    val page: Int? = 1,
    val limit: Int? = 20,
    val unreadOnly: Boolean? = false
)

@Serializable
data class SearchSuggestionsRequest(
    val query: String,
    val limit: Int? = 10
)

@Serializable
data class NotificationIdRequest(
    val notificationId: String
)

@Serializable
data class MarkNotificationReadSchema(
    val notificationId: String
)

// ========================================
// USER PREFERENCES REQUEST DTOS
// ========================================

@Serializable
data class UpdateUserPreferencesSchema(
    val defaultToUserDevices: Boolean? = null,
    val defaultToUserSocs: Boolean? = null,
    val notifyOnNewListings: Boolean? = null,
    val bio: String? = null
)

@Serializable
data class DeviceIdRequest(
    val deviceId: String
)

@Serializable
data class AddDevicePreferenceSchema(
    val deviceId: String
)

@Serializable
data class RemoveDevicePreferenceSchema(
    val deviceId: String
)

@Serializable
data class BulkUpdateDevicePreferencesSchema(
    val deviceIds: List<String>
)

@Serializable
data class BulkUpdateSocPreferencesSchema(
    val socIds: List<String>
)

@Serializable
data class DeviceIdsRequest(
    val deviceIds: List<String>
)

@Serializable
data class SocIdsRequest(
    val socIds: List<String>
)

@Serializable
data class UpdateProfileSchema(
    val name: String? = null,
    val bio: String? = null,
    val email: String? = null
)

// ========================================
// DEVELOPER VERIFICATION REQUEST DTOS
// ========================================

@Serializable
data class EmulatorIdRequest(
    val emulatorId: String
)

@Serializable
data class VerifyDeveloperRequest(
    val userId: String,
    val emulatorId: String
)

@Serializable
data class VerifyListingRequest(
    val listingId: String,
    val notes: String? = null
)

@Serializable
data class VerifyListingSchema(
    val listingId: String,
    val notes: String? = null
)

@Serializable
data class RemoveVerificationSchema(
    val verificationId: String
)

@Serializable
data class PaginationRequest(
    val limit: Int? = 20,
    val page: Int? = 1
)

// ========================================
// CONTENT REPORTING REQUEST DTOS
// ========================================

@Serializable
data class CreateListingReportSchema(
    val listingId: String,
    val reason: ReportReason,
    val description: String
)

@Serializable
enum class ReportReason {
    INAPPROPRIATE_CONTENT,
    SPAM,
    MISLEADING_INFORMATION,
    FAKE_LISTING,
    COPYRIGHT_VIOLATION,
    OTHER
}

// ========================================
// HARDWARE DATA REQUEST DTOS
// ========================================

@Serializable
data class GetCpusSchema(
    val search: String? = null,
    val brandId: String? = null,
    val limit: Int? = 20,
    val offset: Int? = null,
    val page: Int? = null,
    val sortField: String? = null, // brand, modelName
    val sortDirection: String? = null // asc, desc
)

@Serializable
data class GetGpusSchema(
    val search: String? = null,
    val brandId: String? = null,
    val limit: Int? = 20,
    val offset: Int? = null,
    val page: Int? = null,
    val sortField: String? = null, // brand, modelName
    val sortDirection: String? = null // asc, desc
)

@Serializable
data class GetSoCsSchema(
    val search: String? = null,
    val limit: Int? = 20,
    val offset: Int? = null,
    val page: Int? = null,
    val sortField: String? = null, // name, manufacturer
    val sortDirection: String? = null // asc, desc
)

@Serializable
data class GetDeviceBrandsSchema(
    val search: String? = null,
    val limit: Int? = null,
    val sortField: String? = null, // name, devicesCount
    val sortDirection: String? = null // asc, desc
)

// ========================================
// EXTERNAL GAME DATA REQUEST DTOS
// ========================================

@Serializable
data class SearchGameImagesSchema(
    val query: String,
    val includeScreenshots: Boolean? = false
)

@Serializable
data class SearchGamesSchema(
    val query: String,
    val page: Int? = 1,
    val pageSize: Int? = 10 // Max 20
)

@Serializable
data class GetGameImagesRequest(
    val gameId: Int,
    val gameName: String
)

@Serializable
data class SearchTgdbGameImagesRequest(
    val query: String,
    val tgdbPlatformId: Int
)

@Serializable
data class SearchTgdbGamesRequest(
    val query: String,
    val tgdbPlatformId: Int,
    val page: Int? = null
)

@Serializable
data class GetTgdbGameImageUrlsRequest(
    val gameId: Int
)

@Serializable
data class GetTgdbGameImagesRequest(
    val gameIds: List<Int>
)

// ========================================
// USER PROFILES REQUEST DTOS
// ========================================

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

} // End of TrpcRequestDtos object