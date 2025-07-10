package com.emuready.emuready.data.remote.api

import com.emuready.emuready.data.remote.api.trpc.TrpcRequest
import com.emuready.emuready.data.remote.api.trpc.TrpcResponseWrapper
import com.emuready.emuready.data.remote.dto.TrpcRequestDtos
import com.emuready.emuready.data.remote.dto.TrpcResponseDtos
import retrofit2.http.*

/**
 * EmuReady tRPC API Service
 * Implements ALL endpoints from the official API documentation
 * Using correct tRPC format: POST requests with proper body structure
 */
interface EmuReadyTrpcApiService {
    
    // ========================================
    // 1. AUTHENTICATION ENDPOINTS
    // ========================================
    
    @POST("auth.validateToken")
    suspend fun validateToken(@Body request: TrpcRequest<TrpcRequestDtos.ValidateTokenRequest>): TrpcResponseWrapper<TrpcResponseDtos.ValidateTokenResponse>
    
    @POST("auth.getSession")
    suspend fun getSession(@Body request: TrpcRequest<Unit>): TrpcResponseWrapper<TrpcResponseDtos.MobileUser>
    
    @POST("auth.signOut")
    suspend fun signOut(@Body request: TrpcRequest<Unit>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("auth.updateProfile")
    suspend fun updateProfile(@Body request: TrpcRequest<TrpcRequestDtos.UpdateProfileRequest>): TrpcResponseWrapper<TrpcResponseDtos.MobileUser>
    
    @POST("auth.deleteAccount")
    suspend fun deleteAccount(@Body request: TrpcRequest<Unit>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    // ========================================
    // 2. LISTINGS ENDPOINTS
    // ========================================
    
    @GET("listings.get")
    suspend fun getListings(
        @Query("batch") batch: Int = 1,
        @Query("input") input: String
    ): TrpcResponseWrapper<TrpcResponseDtos.MobileListingsResponse>
    
    @GET("listings.getFeatured")
    suspend fun getFeaturedListings(
        @Query("batch") batch: Int = 1,
        @Query("input") input: String? = null
    ): TrpcResponseWrapper<List<TrpcResponseDtos.MobileListing>>
    
    @GET("listings.getByGame")
    suspend fun getListingsByGame(
        @Query("batch") batch: Int = 1,
        @Query("input") input: String
    ): TrpcResponseWrapper<List<TrpcResponseDtos.MobileListing>>
    
    @GET("listings.getById")
    suspend fun getListingById(
        @Query("batch") batch: Int = 1,
        @Query("input") input: String
    ): TrpcResponseWrapper<TrpcResponseDtos.MobileListing>
    
    @GET("listings.getUserListings")
    suspend fun getUserListings(
        @Query("batch") batch: Int = 1,
        @Query("input") input: String
    ): TrpcResponseWrapper<List<TrpcResponseDtos.MobileListing>>
    
    @POST("listings.createListing")
    suspend fun createListing(@Body request: TrpcRequest<TrpcRequestDtos.CreateListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobileListing>
    
    @POST("listings.updateListing")
    suspend fun updateListing(@Body request: TrpcRequest<TrpcRequestDtos.UpdateListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobileListing>
    
    @POST("listings.deleteListing")
    suspend fun deleteListing(@Body request: TrpcRequest<TrpcRequestDtos.IdRequest>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("listings.voteListing")
    suspend fun voteListing(@Body request: TrpcRequest<TrpcRequestDtos.VoteRequest>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @GET("listings.getUserVote")
    suspend fun getUserVote(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.UserVoteResponse>
    
    // ========================================
    // 3. COMMENTS ENDPOINTS  
    // ========================================
    
    @GET("listings.getListingComments")
    suspend fun getListingComments(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.MobileCommentsResponse>
    
    @POST("listings.createComment")
    suspend fun createComment(@Body request: TrpcRequest<TrpcRequestDtos.CreateCommentRequest>): TrpcResponseWrapper<TrpcResponseDtos.MobileComment>
    
    @POST("listings.updateComment")
    suspend fun updateComment(@Body request: TrpcRequest<TrpcRequestDtos.UpdateCommentRequest>): TrpcResponseWrapper<TrpcResponseDtos.MobileComment>
    
    @POST("listings.deleteComment")
    suspend fun deleteComment(@Body request: TrpcRequest<TrpcRequestDtos.CommentIdRequest>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    // ========================================
    // 4. PC LISTINGS ENDPOINTS
    // ========================================
    
    @POST("pcListings.getPcListings")
    suspend fun getPcListings(@Body request: TrpcRequest<TrpcRequestDtos.GetPcListingsSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobilePcListingsResponse>
    
    @POST("pcListings.createPcListing")
    suspend fun createPcListing(@Body request: TrpcRequest<TrpcRequestDtos.CreatePcListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobilePcListing>
    
    @POST("pcListings.updatePcListing")
    suspend fun updatePcListing(@Body request: TrpcRequest<TrpcRequestDtos.UpdatePcListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobilePcListing>
    
    @POST("pcListings.getCpus")
    suspend fun getCpus(@Body request: TrpcRequest<TrpcRequestDtos.SearchWithBrandRequest>): TrpcResponseWrapper<List<TrpcResponseDtos.MobileCpu>>
    
    @POST("pcListings.getGpus")
    suspend fun getGpus(@Body request: TrpcRequest<TrpcRequestDtos.SearchWithBrandRequest>): TrpcResponseWrapper<List<TrpcResponseDtos.MobileGpu>>
    
    // ========================================
    // 5. PC PRESETS ENDPOINTS
    // ========================================
    
    @POST("pcListings.getPcPresets")
    suspend fun getPcPresets(@Body request: TrpcRequest<TrpcRequestDtos.LimitRequest>): TrpcResponseWrapper<List<TrpcResponseDtos.MobilePcPreset>>
    
    @POST("pcListings.createPcPreset")
    suspend fun createPcPreset(@Body request: TrpcRequest<TrpcRequestDtos.CreatePcPresetSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobilePcPreset>
    
    @POST("pcListings.updatePcPreset")
    suspend fun updatePcPreset(@Body request: TrpcRequest<TrpcRequestDtos.UpdatePcPresetSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobilePcPreset>
    
    @POST("pcListings.deletePcPreset")
    suspend fun deletePcPreset(@Body request: TrpcRequest<TrpcRequestDtos.IdRequest>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    // ========================================
    // 6. GAMES ENDPOINTS
    // ========================================
    
    @GET("games.get")
    suspend fun getGames(
        @Query("batch") batch: Int = 1,
        @Query("input") input: String
    ): TrpcResponseWrapper<TrpcResponseDtos.TrpcPaginatedGamesResponse>
    
    @GET("games.getPopular")
    suspend fun getPopularGames(
        @Query("batch") batch: Int = 1,
        @Query("input") input: String? = null
    ): TrpcResponseWrapper<TrpcResponseDtos.TrpcPaginatedGamesResponse>
    
    @GET("games.search")
    suspend fun searchGames(
        @Query("batch") batch: Int = 1,
        @Query("input") query: String
    ): TrpcResponseWrapper<TrpcResponseDtos.TrpcPaginatedGamesResponse>
    
    @GET("games.getById")
    suspend fun getGameById(
        @Query("batch") batch: Int = 1,
        @Query("input") gameId: String
    ): TrpcResponseWrapper<TrpcResponseDtos.MobileGame>
    
    // ========================================
    // 7. DEVICES ENDPOINTS
    // ========================================
    
    @POST("devices.getDevices")
    suspend fun getDevices(@Body request: TrpcRequest<TrpcRequestDtos.GetDevicesSchema>): TrpcResponseWrapper<List<TrpcResponseDtos.MobileDevice>>
    
    @POST("devices.getDeviceBrands")
    suspend fun getDeviceBrands(@Body request: TrpcRequest<Unit>): TrpcResponseWrapper<List<TrpcResponseDtos.MobileBrand>>
    
    @POST("devices.getSocs")
    suspend fun getSocs(@Body request: TrpcRequest<Unit>): TrpcResponseWrapper<List<TrpcResponseDtos.MobileSoc>>
    
    // ========================================
    // 8. EMULATORS ENDPOINTS
    // ========================================
    
    @POST("emulators.getEmulators")
    suspend fun getEmulators(@Body request: TrpcRequest<TrpcRequestDtos.GetEmulatorsSchema>): TrpcResponseWrapper<List<TrpcResponseDtos.MobileEmulator>>
    
    @POST("emulators.getEmulatorById")
    suspend fun getEmulatorById(@Body request: TrpcRequest<TrpcRequestDtos.IdRequest>): TrpcResponseWrapper<TrpcResponseDtos.MobileEmulator>
    
    // ========================================
    // 9. GENERAL DATA ENDPOINTS
    // ========================================
    
    @POST("general.getAppStats")
    suspend fun getAppStats(@Body request: TrpcRequest<Unit>): TrpcResponseWrapper<TrpcResponseDtos.MobileStats>
    
    @POST("general.getSystems")
    suspend fun getSystems(@Body request: TrpcRequest<Unit>): TrpcResponseWrapper<List<TrpcResponseDtos.MobileSystem>>
    
    @POST("general.getPerformanceScales")
    suspend fun getPerformanceScales(@Body request: TrpcRequest<Unit>): TrpcResponseWrapper<List<TrpcResponseDtos.MobilePerformance>>
    
    @POST("general.getSearchSuggestions")
    suspend fun getSearchSuggestions(@Body request: TrpcRequest<TrpcRequestDtos.SearchSuggestionsRequest>): TrpcResponseWrapper<List<TrpcResponseDtos.MobileSearchSuggestion>>
    
    @POST("general.getTrustLevels")
    suspend fun getTrustLevels(@Body request: TrpcRequest<Unit>): TrpcResponseWrapper<List<TrpcResponseDtos.MobileTrustLevel>>
    
    // ========================================
    // 10. NOTIFICATIONS ENDPOINTS
    // ========================================
    
    @POST("notifications.getNotifications")
    suspend fun getNotifications(@Body request: TrpcRequest<TrpcRequestDtos.GetNotificationsSchema>): TrpcResponseWrapper<List<TrpcResponseDtos.MobileNotification>>
    
    @POST("notifications.getUnreadNotificationCount")
    suspend fun getUnreadNotificationCount(@Body request: TrpcRequest<Unit>): TrpcResponseWrapper<TrpcResponseDtos.CountResponse>
    
    @POST("notifications.markNotificationAsRead")
    suspend fun markNotificationAsRead(@Body request: TrpcRequest<TrpcRequestDtos.NotificationIdRequest>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("notifications.markAllNotificationsAsRead")
    suspend fun markAllNotificationsAsRead(@Body request: TrpcRequest<Unit>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    // ========================================
    // 11. USER PREFERENCES ENDPOINTS
    // ========================================
    
    @POST("preferences.getUserPreferences")
    suspend fun getUserPreferences(@Body request: TrpcRequest<Unit>): TrpcResponseWrapper<TrpcResponseDtos.MobileUserProfile>
    
    @POST("preferences.updateUserPreferences")
    suspend fun updateUserPreferences(@Body request: TrpcRequest<TrpcRequestDtos.UpdateUserPreferencesSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobileUserProfile>
    
    @POST("preferences.addDevicePreference")
    suspend fun addDevicePreference(@Body request: TrpcRequest<TrpcRequestDtos.DeviceIdRequest>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("preferences.removeDevicePreference")
    suspend fun removeDevicePreference(@Body request: TrpcRequest<TrpcRequestDtos.DeviceIdRequest>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("preferences.bulkUpdateDevicePreferences")
    suspend fun bulkUpdateDevicePreferences(@Body request: TrpcRequest<TrpcRequestDtos.DeviceIdsRequest>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("preferences.bulkUpdateSocPreferences")
    suspend fun bulkUpdateSocPreferences(@Body request: TrpcRequest<TrpcRequestDtos.SocIdsRequest>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("preferences.getUserProfile")
    suspend fun getUserProfile(@Body request: TrpcRequest<TrpcRequestDtos.UserIdRequest>): TrpcResponseWrapper<TrpcResponseDtos.MobileUserProfile>
    
    @POST("preferences.updateProfile")
    suspend fun updateUserProfile(@Body request: TrpcRequest<TrpcRequestDtos.UpdateProfileSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobileUserProfile>
    
    // ========================================
    // 12. DEVELOPER VERIFICATION ENDPOINTS
    // ========================================
    
    @POST("developers.getMyVerifiedEmulators")
    suspend fun getMyVerifiedEmulators(@Body request: TrpcRequest<Unit>): TrpcResponseWrapper<List<TrpcResponseDtos.MobileEmulatorVerification>>
    
    @POST("developers.isVerifiedDeveloper")
    suspend fun isVerifiedDeveloper(@Body request: TrpcRequest<TrpcRequestDtos.VerifyDeveloperRequest>): TrpcResponseWrapper<TrpcResponseDtos.VerifyDeveloperResponse>
    
    @POST("developers.verifyListing")
    suspend fun verifyListing(@Body request: TrpcRequest<TrpcRequestDtos.VerifyListingRequest>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("developers.removeVerification")
    suspend fun removeVerification(@Body request: TrpcRequest<TrpcRequestDtos.IdRequest>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("developers.getListingVerifications")
    suspend fun getListingVerifications(@Body request: TrpcRequest<TrpcRequestDtos.ListingIdRequest>): TrpcResponseWrapper<TrpcResponseDtos.MobileListingVerificationsResponse>
    
    @POST("developers.getMyVerifications")
    suspend fun getMyVerifications(@Body request: TrpcRequest<TrpcRequestDtos.PaginationRequest>): TrpcResponseWrapper<List<TrpcResponseDtos.MobileVerification>>
    
    // ========================================
    // 13. CONTENT REPORTING ENDPOINTS
    // ========================================
    
    @POST("listingReports.create")
    suspend fun createListingReport(@Body request: TrpcRequest<TrpcRequestDtos.CreateListingReportSchema>): TrpcResponseWrapper<TrpcResponseDtos.ListingReportResponse>
    
    @POST("listingReports.checkUserHasReports")
    suspend fun checkUserHasReports(@Body request: TrpcRequest<TrpcRequestDtos.UserIdRequest>): TrpcResponseWrapper<TrpcResponseDtos.UserReportsInfo>
    
    // ========================================
    // 14. TRUST SYSTEM ENDPOINTS
    // ========================================
    
    @POST("trust.getMyTrustInfo")
    suspend fun getMyTrustInfo(@Body request: TrpcRequest<Unit>): TrpcResponseWrapper<TrpcResponseDtos.TrustInfo>
    
    @POST("trust.getUserTrustInfo")
    suspend fun getUserTrustInfo(@Body request: TrpcRequest<TrpcRequestDtos.UserIdRequest>): TrpcResponseWrapper<TrpcResponseDtos.TrustInfo>
    
    // ========================================
    // 15. CUSTOM FIELDS ENDPOINTS
    // ========================================
    
    @POST("customFieldDefinitions.getByEmulator")
    suspend fun getCustomFieldsByEmulator(@Body request: TrpcRequest<TrpcRequestDtos.EmulatorIdRequest>): TrpcResponseWrapper<List<TrpcResponseDtos.MobileCustomFieldDefinition>>
    
    // ========================================
    // 16. HARDWARE DATA ENDPOINTS
    // ========================================
    
    // CPUs
    @POST("cpus.get")
    suspend fun getCpusEnhanced(@Body request: TrpcRequest<TrpcRequestDtos.GetCpusSchema>): TrpcResponseWrapper<TrpcResponseDtos.HardwarePaginationResponse<TrpcResponseDtos.MobileCpu>>
    
    @POST("cpus.getById")
    suspend fun getCpuById(@Body request: TrpcRequest<TrpcRequestDtos.IdRequest>): TrpcResponseWrapper<TrpcResponseDtos.MobileCpu>
    
    // GPUs
    @POST("gpus.get")
    suspend fun getGpusEnhanced(@Body request: TrpcRequest<TrpcRequestDtos.GetGpusSchema>): TrpcResponseWrapper<TrpcResponseDtos.HardwarePaginationResponse<TrpcResponseDtos.MobileGpu>>
    
    @POST("gpus.getById")
    suspend fun getGpuById(@Body request: TrpcRequest<TrpcRequestDtos.IdRequest>): TrpcResponseWrapper<TrpcResponseDtos.MobileGpu>
    
    // SoCs
    @POST("socs.get")
    suspend fun getSocsEnhanced(@Body request: TrpcRequest<TrpcRequestDtos.GetSoCsSchema>): TrpcResponseWrapper<TrpcResponseDtos.HardwarePaginationResponse<TrpcResponseDtos.MobileSoc>>
    
    @POST("socs.getById")
    suspend fun getSocById(@Body request: TrpcRequest<TrpcRequestDtos.IdRequest>): TrpcResponseWrapper<TrpcResponseDtos.MobileSoc>
    
    // Device Brands
    @POST("deviceBrands.get")
    suspend fun getDeviceBrandsEnhanced(@Body request: TrpcRequest<TrpcRequestDtos.GetDeviceBrandsSchema>): TrpcResponseWrapper<List<TrpcResponseDtos.MobileDeviceBrand>>
    
    @POST("deviceBrands.getById")
    suspend fun getDeviceBrandById(@Body request: TrpcRequest<TrpcRequestDtos.IdRequest>): TrpcResponseWrapper<TrpcResponseDtos.MobileDeviceBrand>
    
    // ========================================
    // 17. EXTERNAL GAME DATA ENDPOINTS
    // ========================================
    
    // RAWG Integration
    @POST("rawg.searchGameImages")
    suspend fun searchRawgGameImages(@Body request: TrpcRequest<TrpcRequestDtos.SearchGameImagesSchema>): TrpcResponseWrapper<Map<String, List<TrpcResponseDtos.GameImageOption>>>
    
    @POST("rawg.searchGames")
    suspend fun searchRawgGames(@Body request: TrpcRequest<TrpcRequestDtos.SearchGamesSchema>): TrpcResponseWrapper<TrpcResponseDtos.RawgGameResponse>
    
    @POST("rawg.getGameImages")
    suspend fun getRawgGameImages(@Body request: TrpcRequest<TrpcRequestDtos.GetGameImagesRequest>): TrpcResponseWrapper<List<TrpcResponseDtos.GameImageOption>>
    
    // TGDB Integration
    @POST("tgdb.searchGameImages")
    suspend fun searchTgdbGameImages(@Body request: TrpcRequest<TrpcRequestDtos.SearchTgdbGameImagesRequest>): TrpcResponseWrapper<Map<String, List<TrpcResponseDtos.GameImageOption>>>
    
    @POST("tgdb.searchGames")
    suspend fun searchTgdbGames(@Body request: TrpcRequest<TrpcRequestDtos.SearchTgdbGamesRequest>): TrpcResponseWrapper<TrpcResponseDtos.TgdbGameResponse>
    
    @POST("tgdb.getGameImageUrls")
    suspend fun getTgdbGameImageUrls(@Body request: TrpcRequest<TrpcRequestDtos.GetTgdbGameImageUrlsRequest>): TrpcResponseWrapper<TrpcResponseDtos.GameImageUrls>
    
    @POST("tgdb.getGameImages")
    suspend fun getTgdbGameImages(@Body request: TrpcRequest<TrpcRequestDtos.GetTgdbGameImagesRequest>): TrpcResponseWrapper<Any>
    
    @POST("tgdb.getPlatforms")
    suspend fun getTgdbPlatforms(@Body request: TrpcRequest<Unit>): TrpcResponseWrapper<List<TrpcResponseDtos.TgdbPlatform>>
    
    // ========================================
    // 18. USER PROFILES ENDPOINTS
    // ========================================
    
    @POST("users.getUserById")
    suspend fun getUserById(@Body request: TrpcRequest<TrpcRequestDtos.GetUserByIdSchema>): TrpcResponseWrapper<TrpcResponseDtos.EnhancedMobileUserProfile>
}