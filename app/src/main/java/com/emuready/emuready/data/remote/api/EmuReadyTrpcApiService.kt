package com.emuready.emuready.data.remote.api

import com.emuready.emuready.data.remote.api.trpc.TrpcRequest
import com.emuready.emuready.data.remote.api.trpc.TrpcResponse
import com.emuready.emuready.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

/**
 * EmuReady tRPC API Service
 * Based on the official API documentation: https://emuready.com/api/mobile/trpc
 */
interface EmuReadyTrpcApiService {
    
    // Authentication endpoints
    @POST("trpc/auth.validateToken")
    suspend fun validateToken(@Body request: TrpcRequest<ValidateTokenRequest>): TrpcResponse<ValidateTokenResponse>
    
    @POST("trpc/auth.getSession")
    suspend fun getSession(@Body request: TrpcRequest<Unit>): TrpcResponse<MobileUser>
    
    @POST("trpc/auth.signOut")
    suspend fun signOut(@Body request: TrpcRequest<Unit>): TrpcResponse<SuccessResponse>
    
    @POST("trpc/auth.updateProfile")
    suspend fun updateProfile(@Body request: TrpcRequest<UpdateProfileRequest>): TrpcResponse<MobileUser>
    
    @POST("trpc/auth.deleteAccount")
    suspend fun deleteAccount(@Body request: TrpcRequest<Unit>): TrpcResponse<SuccessResponse>
    
    // Listings endpoints
    @POST("trpc/listings.getListings")
    suspend fun getListings(@Body request: TrpcRequest<GetListingsSchema>): TrpcResponse<MobileListingsResponse>
    
    @POST("trpc/listings.getFeaturedListings")
    suspend fun getFeaturedListings(@Body request: TrpcRequest<LimitRequest>): TrpcResponse<List<MobileListing>>
    
    @POST("trpc/listings.getListingsByGame")
    suspend fun getListingsByGame(@Body request: TrpcRequest<GameIdRequest>): TrpcResponse<List<MobileListing>>
    
    @POST("trpc/listings.getListingById")
    suspend fun getListingById(@Body request: TrpcRequest<IdRequest>): TrpcResponse<MobileListing>
    
    @POST("trpc/listings.getUserListings")
    suspend fun getUserListings(@Body request: TrpcRequest<UserIdRequest>): TrpcResponse<List<MobileListing>>
    
    @POST("trpc/listings.createListing")
    suspend fun createListing(@Body request: TrpcRequest<CreateListingSchema>): TrpcResponse<MobileListing>
    
    @POST("trpc/listings.updateListing")
    suspend fun updateListing(@Body request: TrpcRequest<UpdateListingSchema>): TrpcResponse<MobileListing>
    
    @POST("trpc/listings.deleteListing")
    suspend fun deleteListing(@Body request: TrpcRequest<IdRequest>): TrpcResponse<SuccessResponse>
    
    @POST("trpc/listings.voteListing")
    suspend fun voteListing(@Body request: TrpcRequest<VoteRequest>): TrpcResponse<SuccessResponse>
    
    @POST("trpc/listings.getUserVote")
    suspend fun getUserVote(@Body request: TrpcRequest<ListingIdRequest>): TrpcResponse<UserVoteResponse>
    
    // Comments endpoints
    @POST("trpc/listings.getListingComments")
    suspend fun getListingComments(@Body request: TrpcRequest<ListingIdRequest>): TrpcResponse<MobileCommentsResponse>
    
    @POST("trpc/listings.createComment")
    suspend fun createComment(@Body request: TrpcRequest<CreateCommentRequest>): TrpcResponse<MobileComment>
    
    @POST("trpc/listings.updateComment")
    suspend fun updateComment(@Body request: TrpcRequest<UpdateCommentRequest>): TrpcResponse<MobileComment>
    
    @POST("trpc/listings.deleteComment")
    suspend fun deleteComment(@Body request: TrpcRequest<CommentIdRequest>): TrpcResponse<SuccessResponse>
    
    // PC Listings endpoints
    @POST("trpc/pcListings.getPcListings")
    suspend fun getPcListings(@Body request: TrpcRequest<GetPcListingsSchema>): TrpcResponse<MobilePcListingsResponse>
    
    @POST("trpc/pcListings.createPcListing")
    suspend fun createPcListing(@Body request: TrpcRequest<CreatePcListingSchema>): TrpcResponse<MobilePcListing>
    
    @POST("trpc/pcListings.updatePcListing")
    suspend fun updatePcListing(@Body request: TrpcRequest<UpdatePcListingSchema>): TrpcResponse<MobilePcListing>
    
    @POST("trpc/pcListings.getCpus")
    suspend fun getCpus(@Body request: TrpcRequest<SearchWithBrandRequest>): TrpcResponse<List<MobileCpu>>
    
    @POST("trpc/pcListings.getGpus")
    suspend fun getGpus(@Body request: TrpcRequest<SearchWithBrandRequest>): TrpcResponse<List<MobileGpu>>
    
    // PC Presets endpoints
    @POST("trpc/pcListings.getPcPresets")
    suspend fun getPcPresets(@Body request: TrpcRequest<LimitRequest>): TrpcResponse<List<MobilePcPreset>>
    
    @POST("trpc/pcListings.createPcPreset")
    suspend fun createPcPreset(@Body request: TrpcRequest<CreatePcPresetSchema>): TrpcResponse<MobilePcPreset>
    
    @POST("trpc/pcListings.updatePcPreset")
    suspend fun updatePcPreset(@Body request: TrpcRequest<UpdatePcPresetSchema>): TrpcResponse<MobilePcPreset>
    
    @POST("trpc/pcListings.deletePcPreset")
    suspend fun deletePcPreset(@Body request: TrpcRequest<IdRequest>): TrpcResponse<SuccessResponse>
    
    // Games endpoints
    @POST("trpc/games.getGames")
    suspend fun getGames(@Body request: TrpcRequest<GetGamesSchema>): TrpcResponse<List<MobileGame>>
    
    @POST("trpc/games.getPopularGames")
    suspend fun getPopularGames(@Body request: TrpcRequest<LimitRequest>): TrpcResponse<List<MobileGame>>
    
    @POST("trpc/games.searchGames")
    suspend fun searchGames(@Body request: TrpcRequest<QueryRequest>): TrpcResponse<List<MobileGame>>
    
    @POST("trpc/games.getGameById")
    suspend fun getGameById(@Body request: TrpcRequest<IdRequest>): TrpcResponse<MobileGame>
    
    // Devices endpoints
    @POST("trpc/devices.getDevices")
    suspend fun getDevices(@Body request: TrpcRequest<GetDevicesSchema>): TrpcResponse<List<MobileDevice>>
    
    @POST("trpc/devices.getDeviceBrands")
    suspend fun getDeviceBrands(@Body request: TrpcRequest<Unit>): TrpcResponse<List<MobileBrand>>
    
    @POST("trpc/devices.getSocs")
    suspend fun getSocs(@Body request: TrpcRequest<Unit>): TrpcResponse<List<MobileSoc>>
    
    // Emulators endpoints
    @POST("trpc/emulators.getEmulators")
    suspend fun getEmulators(@Body request: TrpcRequest<GetEmulatorsSchema>): TrpcResponse<List<MobileEmulator>>
    
    @POST("trpc/emulators.getEmulatorById")
    suspend fun getEmulatorById(@Body request: TrpcRequest<IdRequest>): TrpcResponse<MobileEmulator>
    
    // General endpoints
    @POST("trpc/general.getAppStats")
    suspend fun getAppStats(@Body request: TrpcRequest<Unit>): TrpcResponse<MobileStats>
    
    @POST("trpc/general.getSystems")
    suspend fun getSystems(@Body request: TrpcRequest<Unit>): TrpcResponse<List<MobileSystem>>
    
    @POST("trpc/general.getPerformanceScales")
    suspend fun getPerformanceScales(@Body request: TrpcRequest<Unit>): TrpcResponse<List<MobilePerformance>>
    
    @POST("trpc/general.getSearchSuggestions")
    suspend fun getSearchSuggestions(@Body request: TrpcRequest<SearchSuggestionsRequest>): TrpcResponse<List<MobileSearchSuggestion>>
    
    
    // Notifications endpoints
    @POST("trpc/notifications.getNotifications")
    suspend fun getNotifications(@Body request: TrpcRequest<GetNotificationsSchema>): TrpcResponse<List<MobileNotification>>
    
    @POST("trpc/notifications.getUnreadNotificationCount")
    suspend fun getUnreadNotificationCount(@Body request: TrpcRequest<Unit>): TrpcResponse<CountResponse>
    
    @POST("trpc/notifications.markNotificationAsRead")
    suspend fun markNotificationAsRead(@Body request: TrpcRequest<NotificationIdRequest>): TrpcResponse<SuccessResponse>
    
    @POST("trpc/notifications.markAllNotificationsAsRead")
    suspend fun markAllNotificationsAsRead(@Body request: TrpcRequest<Unit>): TrpcResponse<SuccessResponse>
    
    // User Preferences endpoints
    @POST("trpc/preferences.getUserPreferences")
    suspend fun getUserPreferences(@Body request: TrpcRequest<Unit>): TrpcResponse<MobileUserProfile>
    
    @POST("trpc/preferences.updateUserPreferences")
    suspend fun updateUserPreferences(@Body request: TrpcRequest<UpdateUserPreferencesSchema>): TrpcResponse<MobileUserProfile>
    
    @POST("trpc/preferences.addDevicePreference")
    suspend fun addDevicePreference(@Body request: TrpcRequest<DeviceIdRequest>): TrpcResponse<SuccessResponse>
    
    @POST("trpc/preferences.removeDevicePreference")
    suspend fun removeDevicePreference(@Body request: TrpcRequest<DeviceIdRequest>): TrpcResponse<SuccessResponse>
    
    @POST("trpc/preferences.bulkUpdateDevicePreferences")
    suspend fun bulkUpdateDevicePreferences(@Body request: TrpcRequest<DeviceIdsRequest>): TrpcResponse<SuccessResponse>
    
    @POST("trpc/preferences.bulkUpdateSocPreferences")
    suspend fun bulkUpdateSocPreferences(@Body request: TrpcRequest<SocIdsRequest>): TrpcResponse<SuccessResponse>
    
    @POST("trpc/preferences.getUserProfile")
    suspend fun getUserProfile(@Body request: TrpcRequest<UserIdRequest>): TrpcResponse<MobileUserProfile>
    
    @POST("trpc/preferences.updateProfile")
    suspend fun updateUserProfile(@Body request: TrpcRequest<UpdateProfileSchema>): TrpcResponse<MobileUserProfile>
    
    // Developer Verification endpoints
    @POST("trpc/developers.getMyVerifiedEmulators")
    suspend fun getMyVerifiedEmulators(@Body request: TrpcRequest<Unit>): TrpcResponse<List<MobileEmulator>>
    
    @POST("trpc/developers.isVerifiedDeveloper")
    suspend fun isVerifiedDeveloper(@Body request: TrpcRequest<EmulatorIdRequest>): TrpcResponse<SuccessResponse>
    
    @POST("trpc/developers.verifyListing")
    suspend fun verifyListing(@Body request: TrpcRequest<VerifyListingRequest>): TrpcResponse<MobileVerification>
    
    @POST("trpc/developers.removeVerification")
    suspend fun removeVerification(@Body request: TrpcRequest<IdRequest>): TrpcResponse<SuccessResponse>
    
    @POST("trpc/developers.getListingVerifications")
    suspend fun getListingVerifications(@Body request: TrpcRequest<ListingIdRequest>): TrpcResponse<MobileListingVerificationsResponse>
    
    @POST("trpc/developers.getMyVerifications")
    suspend fun getMyVerifications(@Body request: TrpcRequest<PaginationRequest>): TrpcResponse<List<MobileVerification>>
    
    // Trust System endpoints
    @POST("trpc/trust.getMyTrustInfo")
    suspend fun getMyTrustInfo(@Body request: TrpcRequest<Unit>): TrpcResponse<TrustInfo>
    
    @POST("trpc/trust.getUserTrustInfo")
    suspend fun getUserTrustInfo(@Body request: TrpcRequest<UserIdRequest>): TrpcResponse<TrustInfo>
    
    @POST("trpc/trust.getTrustLevels")
    suspend fun getTrustLevels(@Body request: TrpcRequest<Unit>): TrpcResponse<List<MobileTrustLevel>>
    
    // Content Reporting endpoints
    @POST("trpc/listingReports.create")
    suspend fun createListingReport(@Body request: TrpcRequest<CreateListingReportSchema>): TrpcResponse<ListingReportResponse>
    
    @POST("trpc/listingReports.checkUserHasReports")
    suspend fun checkUserHasReports(@Body request: TrpcRequest<UserIdRequest>): TrpcResponse<UserReportsInfo>
    
    // Custom Fields endpoints
    @POST("trpc/customFieldDefinitions.getByEmulator")
    suspend fun getCustomFieldsByEmulator(@Body request: TrpcRequest<EmulatorIdRequest>): TrpcResponse<List<MobileCustomFieldDefinition>>
    
    // Enhanced Hardware endpoints
    @POST("trpc/cpus.get")
    suspend fun getCpusEnhanced(@Body request: TrpcRequest<GetCpusSchema>): TrpcResponse<HardwarePaginationResponse<MobileCpu>>
    
    @POST("trpc/cpus.getById")
    suspend fun getCpuById(@Body request: TrpcRequest<IdRequest>): TrpcResponse<MobileCpu>
    
    @POST("trpc/gpus.get")
    suspend fun getGpusEnhanced(@Body request: TrpcRequest<GetGpusSchema>): TrpcResponse<HardwarePaginationResponse<MobileGpu>>
    
    @POST("trpc/gpus.getById")
    suspend fun getGpuById(@Body request: TrpcRequest<IdRequest>): TrpcResponse<MobileGpu>
    
    @POST("trpc/socs.get")
    suspend fun getSocsEnhanced(@Body request: TrpcRequest<GetSoCsSchema>): TrpcResponse<HardwarePaginationResponse<MobileSoc>>
    
    @POST("trpc/socs.getById")
    suspend fun getSocById(@Body request: TrpcRequest<IdRequest>): TrpcResponse<MobileSoc>
    
    @POST("trpc/deviceBrands.get")
    suspend fun getDeviceBrandsEnhanced(@Body request: TrpcRequest<GetDeviceBrandsSchema>): TrpcResponse<List<MobileDeviceBrand>>
    
    @POST("trpc/deviceBrands.getById")
    suspend fun getDeviceBrandById(@Body request: TrpcRequest<IdRequest>): TrpcResponse<MobileDeviceBrand>
    
    // External Game Data endpoints
    @POST("trpc/rawg.searchGameImages")
    suspend fun searchRawgGameImages(@Body request: TrpcRequest<SearchGameImagesSchema>): TrpcResponse<Map<String, List<GameImageOption>>>
    
    @POST("trpc/rawg.searchGames")
    suspend fun searchRawgGames(@Body request: TrpcRequest<SearchGamesSchema>): TrpcResponse<RawgGameResponse>
    
    @POST("trpc/rawg.getGameImages")
    suspend fun getRawgGameImages(@Body request: TrpcRequest<GetGameImagesRequest>): TrpcResponse<List<GameImageOption>>
    
    @POST("trpc/tgdb.searchGameImages")
    suspend fun searchTgdbGameImages(@Body request: TrpcRequest<SearchTgdbGameImagesRequest>): TrpcResponse<Map<String, List<GameImageOption>>>
    
    @POST("trpc/tgdb.searchGames")
    suspend fun searchTgdbGames(@Body request: TrpcRequest<SearchTgdbGamesRequest>): TrpcResponse<TgdbGameResponse>
    
    @POST("trpc/tgdb.getGameImageUrls")
    suspend fun getTgdbGameImageUrls(@Body request: TrpcRequest<GetTgdbGameImageUrlsRequest>): TrpcResponse<GameImageUrls>
    
    @POST("trpc/tgdb.getGameImages")
    suspend fun getTgdbGameImages(@Body request: TrpcRequest<GetTgdbGameImagesRequest>): TrpcResponse<Any>
    
    @POST("trpc/tgdb.getPlatforms")
    suspend fun getTgdbPlatforms(@Body request: TrpcRequest<Unit>): TrpcResponse<List<TgdbPlatform>>
    
    // Enhanced User Profile endpoints
    @POST("trpc/users.getUserById")
    suspend fun getUserById(@Body request: TrpcRequest<GetUserByIdSchema>): TrpcResponse<EnhancedMobileUserProfile>
}

// Request DTOs for specific endpoint parameters

@kotlinx.serialization.Serializable
data class ValidateTokenRequest(
    val token: String
)

@kotlinx.serialization.Serializable
data class UpdateProfileRequest(
    val name: String? = null,
    val bio: String? = null
)

@kotlinx.serialization.Serializable
data class LimitRequest(
    val limit: Int? = 10
)

@kotlinx.serialization.Serializable
data class GameIdRequest(
    val gameId: String
)

@kotlinx.serialization.Serializable
data class IdRequest(
    val id: String
)

@kotlinx.serialization.Serializable
data class UserIdRequest(
    val userId: String
)

@kotlinx.serialization.Serializable
data class ListingIdRequest(
    val listingId: String
)

@kotlinx.serialization.Serializable
data class VoteRequest(
    val listingId: String,
    val value: Boolean
)

@kotlinx.serialization.Serializable
data class CreateCommentRequest(
    val listingId: String,
    val content: String
)

@kotlinx.serialization.Serializable
data class UpdateCommentRequest(
    val commentId: String,
    val content: String
)

@kotlinx.serialization.Serializable
data class CommentIdRequest(
    val commentId: String
)

@kotlinx.serialization.Serializable
data class SearchWithBrandRequest(
    val search: String? = null,
    val brandId: String? = null,
    val limit: Int? = 50
)

@kotlinx.serialization.Serializable
data class UpdatePcListingSchema(
    val id: String,
    val performanceId: Int? = null,
    val memorySize: Int? = null,
    val os: String? = null,
    val osVersion: String? = null,
    val notes: String? = null,
    val customFieldValues: List<CustomFieldValueInput>? = null
)

@kotlinx.serialization.Serializable
data class UpdatePcPresetSchema(
    val id: String,
    val name: String? = null,
    val cpuId: String? = null,
    val gpuId: String? = null,
    val memorySize: Int? = null,
    val os: String? = null,
    val osVersion: String? = null
)

@kotlinx.serialization.Serializable
data class QueryRequest(
    val query: String
)

@kotlinx.serialization.Serializable
data class SearchSuggestionsRequest(
    val query: String,
    val limit: Int? = 10
)

@kotlinx.serialization.Serializable
data class NotificationIdRequest(
    val notificationId: String
)

@kotlinx.serialization.Serializable
data class DeviceIdRequest(
    val deviceId: String
)

@kotlinx.serialization.Serializable
data class DeviceIdsRequest(
    val deviceIds: List<String>
)

@kotlinx.serialization.Serializable
data class SocIdsRequest(
    val socIds: List<String>
)

@kotlinx.serialization.Serializable
data class UpdateProfileSchema(
    val name: String? = null,
    val bio: String? = null,
    val email: String? = null
)

// Additional Request DTOs for new endpoints

@kotlinx.serialization.Serializable
data class EmulatorIdRequest(
    val emulatorId: String
)

@kotlinx.serialization.Serializable
data class VerifyListingRequest(
    val listingId: String,
    val notes: String? = null
)

@kotlinx.serialization.Serializable
data class PaginationRequest(
    val limit: Int? = 20,
    val page: Int? = 1
)

@kotlinx.serialization.Serializable
data class GetGameImagesRequest(
    val gameId: Int,
    val gameName: String
)

@kotlinx.serialization.Serializable
data class SearchTgdbGameImagesRequest(
    val query: String,
    val tgdbPlatformId: Int
)

@kotlinx.serialization.Serializable
data class SearchTgdbGamesRequest(
    val query: String,
    val tgdbPlatformId: Int,
    val page: Int? = null
)

@kotlinx.serialization.Serializable
data class GetTgdbGameImageUrlsRequest(
    val gameId: Int
)

@kotlinx.serialization.Serializable
data class GetTgdbGameImagesRequest(
    val gameIds: List<Int>
)