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
    
    @POST("auth.signIn")
    suspend fun signIn(@Body request: TrpcRequest<TrpcRequestDtos.MobileSignInRequest>): TrpcResponseWrapper<TrpcResponseDtos.AuthResponse>
    
    @POST("auth.signUp")
    suspend fun signUp(@Body request: TrpcRequest<TrpcRequestDtos.MobileSignUpRequest>): TrpcResponseWrapper<TrpcResponseDtos.AuthResponse>
    
    @POST("auth.oauthSignIn")
    suspend fun oauthSignIn(@Body request: TrpcRequest<TrpcRequestDtos.MobileOAuthSignInRequest>): TrpcResponseWrapper<TrpcResponseDtos.OAuthResponse>
    
    @GET("auth.validateToken")
    suspend fun validateToken(@Query("token") token: String): TrpcResponseWrapper<TrpcResponseDtos.ValidateTokenResponse>
    
    @POST("auth.refreshToken")
    suspend fun refreshToken(@Body request: TrpcRequest<TrpcRequestDtos.RefreshTokenRequest>): TrpcResponseWrapper<TrpcResponseDtos.AuthResponse>
    
    @POST("auth.verifyEmail")
    suspend fun verifyEmail(@Body request: TrpcRequest<TrpcRequestDtos.VerifyEmailRequest>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("auth.forgotPassword")
    suspend fun forgotPassword(@Body request: TrpcRequest<TrpcRequestDtos.ForgotPasswordRequest>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("auth.resetPassword")
    suspend fun resetPassword(@Body request: TrpcRequest<TrpcRequestDtos.ResetPasswordRequest>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("auth.signOut")
    suspend fun signOut(): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("auth.updateProfile")
    suspend fun updateProfile(@Body request: TrpcRequest<TrpcRequestDtos.UpdateMobileProfileRequest>): TrpcResponseWrapper<TrpcResponseDtos.MobileUser>
    
    @POST("auth.changePassword")
    suspend fun changePassword(@Body request: TrpcRequest<TrpcRequestDtos.ChangeMobilePasswordRequest>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("auth.deleteAccount")
    suspend fun deleteAccount(@Body request: TrpcRequest<TrpcRequestDtos.DeleteMobileAccountRequest>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    // ========================================
    // 2. LISTINGS ENDPOINTS
    // ========================================
    
    @GET("listings.getListings")
    suspend fun getListings(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("gameId") gameId: String? = null,
        @Query("systemId") systemId: String? = null,
        @Query("deviceId") deviceId: String? = null,
        @Query("emulatorId") emulatorId: String? = null,
        @Query("search") search: String? = null
    ): TrpcResponseWrapper<TrpcResponseDtos.MobileListingsResponse>
    
    @GET("listings.getFeaturedListings")
    suspend fun getFeaturedListings(): TrpcResponseWrapper<List<TrpcResponseDtos.MobileListing>>
    
    @GET("listings.getListingsByGame")
    suspend fun getListingsByGame(
        @Query("gameId") gameId: String
    ): TrpcResponseWrapper<List<TrpcResponseDtos.MobileListing>>
    
    @GET("listings.getListingById")
    suspend fun getListingById(
        @Query("id") id: String
    ): TrpcResponseWrapper<TrpcResponseDtos.MobileListing>
    
    @GET("listings.getUserListings")
    suspend fun getUserListings(
        @Query("userId") userId: String
    ): TrpcResponseWrapper<List<TrpcResponseDtos.MobileListing>>
    
    @POST("listings.createListing")
    suspend fun createListing(@Body request: TrpcRequest<TrpcRequestDtos.CreateListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobileListing>
    
    @POST("listings.updateListing")
    suspend fun updateListing(@Body request: TrpcRequest<TrpcRequestDtos.UpdateListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobileListing>
    
    @POST("listings.deleteListing")
    suspend fun deleteListing(@Body request: TrpcRequest<TrpcRequestDtos.DeleteListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("listings.voteListing")
    suspend fun voteListing(@Body request: TrpcRequest<TrpcRequestDtos.VoteListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @GET("listings.getUserVote")
    suspend fun getUserVote(@Query("listingId") listingId: String): TrpcResponseWrapper<TrpcResponseDtos.UserVoteResponse>
    
    // ========================================
    // 3. COMMENTS ENDPOINTS  
    // ========================================
    
    @GET("listings.getListingComments")
    suspend fun getListingComments(@Query("listingId") listingId: String): TrpcResponseWrapper<TrpcResponseDtos.MobileCommentsResponse>
    
    @POST("listings.createComment")
    suspend fun createComment(@Body request: TrpcRequest<TrpcRequestDtos.CreateCommentSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobileComment>
    
    @POST("listings.updateComment")
    suspend fun updateComment(@Body request: TrpcRequest<TrpcRequestDtos.UpdateCommentSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobileComment>
    
    @POST("listings.deleteComment")
    suspend fun deleteComment(@Body request: TrpcRequest<TrpcRequestDtos.DeleteCommentSchema>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    // ========================================
    // 4. PC LISTINGS ENDPOINTS
    // ========================================
    
    @GET("pcListings.getPcListings")
    suspend fun getPcListings(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("gameId") gameId: String? = null,
        @Query("systemId") systemId: String? = null,
        @Query("cpuId") cpuId: String? = null,
        @Query("gpuId") gpuId: String? = null,
        @Query("emulatorId") emulatorId: String? = null,
        @Query("os") os: String? = null,
        @Query("search") search: String? = null
    ): TrpcResponseWrapper<TrpcResponseDtos.MobilePcListingsResponse>
    
    @POST("pcListings.createPcListing")
    suspend fun createPcListing(@Body request: TrpcRequest<TrpcRequestDtos.CreatePcListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobilePcListing>
    
    @POST("pcListings.updatePcListing")
    suspend fun updatePcListing(@Body request: TrpcRequest<TrpcRequestDtos.UpdatePcListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobilePcListing>
    
    @GET("pcListings.getCpus")
    suspend fun getCpus(
        @Query("search") search: String? = null,
        @Query("brandId") brandId: String? = null,
        @Query("limit") limit: Int? = null
    ): TrpcResponseWrapper<List<TrpcResponseDtos.MobileCpu>>
    
    @GET("pcListings.getGpus")
    suspend fun getGpus(
        @Query("search") search: String? = null,
        @Query("brandId") brandId: String? = null,
        @Query("limit") limit: Int? = null
    ): TrpcResponseWrapper<List<TrpcResponseDtos.MobileGpu>>
    
    // ========================================
    // 5. PC PRESETS ENDPOINTS
    // ========================================
    
    @GET("pcListings.get")
    suspend fun getPcPresets(
        @Query("limit") limit: Int? = null
    ): TrpcResponseWrapper<List<TrpcResponseDtos.MobilePcPreset>>
    
    @POST("pcListings.create")
    suspend fun createPcPreset(@Body request: TrpcRequest<TrpcRequestDtos.CreatePcPresetSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobilePcPreset>
    
    @POST("pcListings.update")
    suspend fun updatePcPreset(@Body request: TrpcRequest<TrpcRequestDtos.UpdatePcPresetSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobilePcPreset>
    
    @POST("pcListings.delete")
    suspend fun deletePcPreset(@Body request: TrpcRequest<TrpcRequestDtos.DeletePcPresetSchema>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    // ========================================
    // 6. GAMES ENDPOINTS
    // ========================================
    
    @GET("games.getGames")
    suspend fun getGames(
        @Query("search") search: String? = null,
        @Query("systemId") systemId: String? = null,
        @Query("limit") limit: Int? = null
    ): TrpcResponseWrapper<TrpcResponseDtos.TrpcPaginatedGamesResponse>
    
    @GET("games.getPopularGames")
    suspend fun getPopularGames(): TrpcResponseWrapper<TrpcResponseDtos.TrpcPaginatedGamesResponse>
    
    @GET("games.searchGames")
    suspend fun searchGames(
        @Query("query") query: String
    ): TrpcResponseWrapper<TrpcResponseDtos.TrpcPaginatedGamesResponse>
    
    @GET("games.getGameById")
    suspend fun getGameById(
        @Query("id") id: String
    ): TrpcResponseWrapper<TrpcResponseDtos.MobileGame>
    
    // ========================================
    // 7. DEVICES ENDPOINTS
    // ========================================
    
    @GET("devices.getDevices")
    suspend fun getDevices(
        @Query("search") search: String? = null,
        @Query("brandId") brandId: String? = null,
        @Query("limit") limit: Int? = null
    ): TrpcResponseWrapper<List<TrpcResponseDtos.MobileDevice>>
    
    @GET("devices.getDeviceBrands")
    suspend fun getDeviceBrands(): TrpcResponseWrapper<List<TrpcResponseDtos.MobileBrand>>
    
    @GET("devices.getSocs")
    suspend fun getSocs(): TrpcResponseWrapper<List<TrpcResponseDtos.MobileSoc>>
    
    // ========================================
    // 8. EMULATORS ENDPOINTS
    // ========================================
    
    @GET("emulators.getEmulators")
    suspend fun getEmulators(
        @Query("systemId") systemId: String? = null,
        @Query("search") search: String? = null,
        @Query("limit") limit: Int? = null
    ): TrpcResponseWrapper<List<TrpcResponseDtos.MobileEmulator>>
    
    @GET("emulators.getEmulatorById")
    suspend fun getEmulatorById(
        @Query("id") id: String
    ): TrpcResponseWrapper<TrpcResponseDtos.MobileEmulator>
    
    // ========================================
    // 9. GENERAL DATA ENDPOINTS
    // ========================================
    
    @GET("general.getAppStats")
    suspend fun getAppStats(): TrpcResponseWrapper<TrpcResponseDtos.MobileStats>
    
    @GET("general.getSystems")
    suspend fun getSystems(): TrpcResponseWrapper<List<TrpcResponseDtos.MobileSystem>>
    
    @GET("general.getPerformanceScales")
    suspend fun getPerformanceScales(): TrpcResponseWrapper<List<TrpcResponseDtos.MobilePerformance>>
    
    @GET("general.getSearchSuggestions")
    suspend fun getSearchSuggestions(
        @Query("query") query: String,
        @Query("limit") limit: Int? = null
    ): TrpcResponseWrapper<List<TrpcResponseDtos.MobileSearchSuggestion>>
    
    
    // ========================================
    // 10. NOTIFICATIONS ENDPOINTS
    // ========================================
    
    @GET("notifications.getNotifications")
    suspend fun getNotifications(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("unreadOnly") unreadOnly: Boolean? = null
    ): TrpcResponseWrapper<List<TrpcResponseDtos.MobileNotification>>
    
    @GET("notifications.getUnreadNotificationCount")
    suspend fun getUnreadNotificationCount(): TrpcResponseWrapper<TrpcResponseDtos.CountResponse>
    
    @POST("notifications.markNotificationAsRead")
    suspend fun markNotificationAsRead(@Body request: TrpcRequest<TrpcRequestDtos.MarkNotificationReadSchema>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("notifications.markAllNotificationsAsRead")
    suspend fun markAllNotificationsAsRead(): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    // ========================================
    // 11. USER PREFERENCES ENDPOINTS
    // ========================================
    
    @GET("preferences.getUserPreferences")
    suspend fun getUserPreferences(): TrpcResponseWrapper<TrpcResponseDtos.MobileUserProfile>
    
    @POST("preferences.updateUserPreferences")
    suspend fun updateUserPreferences(@Body request: TrpcRequest<TrpcRequestDtos.UpdateUserPreferencesSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobileUserProfile>
    
    @POST("preferences.addDevicePreference")
    suspend fun addDevicePreference(@Body request: TrpcRequest<TrpcRequestDtos.AddDevicePreferenceSchema>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("preferences.removeDevicePreference")
    suspend fun removeDevicePreference(@Body request: TrpcRequest<TrpcRequestDtos.RemoveDevicePreferenceSchema>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("preferences.bulkUpdateDevicePreferences")
    suspend fun bulkUpdateDevicePreferences(@Body request: TrpcRequest<TrpcRequestDtos.BulkUpdateDevicePreferencesSchema>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("preferences.bulkUpdateSocPreferences")
    suspend fun bulkUpdateSocPreferences(@Body request: TrpcRequest<TrpcRequestDtos.BulkUpdateSocPreferencesSchema>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @GET("preferences.getUserProfile")
    suspend fun getUserProfile(
        @Query("userId") userId: String
    ): TrpcResponseWrapper<TrpcResponseDtos.MobileUserProfile>
    
    @POST("preferences.updateProfile")
    suspend fun updateUserProfile(@Body request: TrpcRequest<TrpcRequestDtos.UpdateProfileSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobileUserProfile>
    
    // ========================================
    // 12. DEVELOPER VERIFICATION ENDPOINTS
    // ========================================
    
    @GET("developers.getMyVerifiedEmulators")
    suspend fun getMyVerifiedEmulators(): TrpcResponseWrapper<List<TrpcResponseDtos.MobileEmulatorVerification>>
    
    @GET("developers.isVerifiedDeveloper")
    suspend fun isVerifiedDeveloper(
        @Query("userId") userId: String,
        @Query("emulatorId") emulatorId: String
    ): TrpcResponseWrapper<TrpcResponseDtos.VerifyDeveloperResponse>
    
    @POST("developers.verifyListing")
    suspend fun verifyListing(@Body request: TrpcRequest<TrpcRequestDtos.VerifyListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("developers.removeVerification")
    suspend fun removeVerification(@Body request: TrpcRequest<TrpcRequestDtos.RemoveVerificationSchema>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @GET("developers.getListingVerifications")
    suspend fun getListingVerifications(
        @Query("listingId") listingId: String
    ): TrpcResponseWrapper<TrpcResponseDtos.MobileListingVerificationsResponse>
    
    @GET("developers.getMyVerifications")
    suspend fun getMyVerifications(
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null
    ): TrpcResponseWrapper<List<TrpcResponseDtos.MobileVerification>>
    
    // ========================================
    // 13. CONTENT REPORTING ENDPOINTS
    // ========================================
    
    @POST("listingReports.create")
    suspend fun createListingReport(@Body request: TrpcRequest<TrpcRequestDtos.CreateListingReportSchema>): TrpcResponseWrapper<TrpcResponseDtos.ListingReportResponse>
    
    @GET("listingReports.checkUserHasReports")
    suspend fun checkUserHasReports(@Query("userId") userId: String): TrpcResponseWrapper<TrpcResponseDtos.UserReportsInfo>
    
    // ========================================
    // 14. TRUST SYSTEM ENDPOINTS
    // ========================================
    
    @GET("trust.getMyTrustInfo")
    suspend fun getMyTrustInfo(): TrpcResponseWrapper<TrpcResponseDtos.TrustInfo>
    
    @GET("trust.getUserTrustInfo")
    suspend fun getUserTrustInfo(@Query("userId") userId: String): TrpcResponseWrapper<TrpcResponseDtos.TrustInfo>
    
    @GET("trust.getTrustLevels")
    suspend fun getTrustLevels(): TrpcResponseWrapper<List<TrpcResponseDtos.MobileTrustLevel>>
    
    // ========================================
    // 15. CUSTOM FIELDS ENDPOINTS
    // ========================================
    
    @GET("customFieldDefinitions.getByEmulator")
    suspend fun getCustomFieldsByEmulator(@Query("emulatorId") emulatorId: String): TrpcResponseWrapper<List<TrpcResponseDtos.MobileCustomFieldDefinition>>
    
    // ========================================
    // 16. HARDWARE DATA ENDPOINTS
    // ========================================
    
    // CPUs
    @GET("cpus.get")
    suspend fun getCpusEnhanced(
        @Query("search") search: String? = null,
        @Query("brandId") brandId: String? = null,
        @Query("limit") limit: Int? = null
    ): TrpcResponseWrapper<TrpcResponseDtos.HardwarePaginationResponse<TrpcResponseDtos.MobileCpu>>
    
    @GET("cpus.getById")
    suspend fun getCpuById(@Query("id") id: String): TrpcResponseWrapper<TrpcResponseDtos.MobileCpu>
    
    // GPUs
    @GET("gpus.get")
    suspend fun getGpusEnhanced(
        @Query("search") search: String? = null,
        @Query("brandId") brandId: String? = null,
        @Query("limit") limit: Int? = null
    ): TrpcResponseWrapper<TrpcResponseDtos.HardwarePaginationResponse<TrpcResponseDtos.MobileGpu>>
    
    @GET("gpus.getById")
    suspend fun getGpuById(@Query("id") id: String): TrpcResponseWrapper<TrpcResponseDtos.MobileGpu>
    
    // SoCs
    @GET("socs.get")
    suspend fun getSocsEnhanced(): TrpcResponseWrapper<TrpcResponseDtos.HardwarePaginationResponse<TrpcResponseDtos.MobileSoc>>
    
    @GET("socs.getById")
    suspend fun getSocById(@Query("id") id: String): TrpcResponseWrapper<TrpcResponseDtos.MobileSoc>
    
    // Device Brands
    @GET("deviceBrands.get")
    suspend fun getDeviceBrandsEnhanced(): TrpcResponseWrapper<List<TrpcResponseDtos.MobileDeviceBrand>>
    
    @GET("deviceBrands.getById")
    suspend fun getDeviceBrandById(@Query("id") id: String): TrpcResponseWrapper<TrpcResponseDtos.MobileDeviceBrand>
    
    // ========================================
    // 17. EXTERNAL GAME DATA ENDPOINTS
    // ========================================
    
    // RAWG Integration
    @GET("rawg.searchGameImages")
    suspend fun searchRawgGameImages(@Query("gameIds") gameIds: List<String>): TrpcResponseWrapper<Map<String, List<TrpcResponseDtos.GameImageOption>>>
    
    @GET("rawg.searchGames")
    suspend fun searchRawgGames(
        @Query("query") query: String
    ): TrpcResponseWrapper<TrpcResponseDtos.RawgGameResponse>
    
    @GET("rawg.getGameImages")
    suspend fun getRawgGameImages(@Query("gameId") gameId: String): TrpcResponseWrapper<List<TrpcResponseDtos.GameImageOption>>
    
    // TGDB Integration
    @GET("tgdb.searchGameImages")
    suspend fun searchTgdbGameImages(@Query("gameIds") gameIds: List<String>): TrpcResponseWrapper<Map<String, List<TrpcResponseDtos.GameImageOption>>>
    
    @GET("tgdb.searchGames")
    suspend fun searchTgdbGames(
        @Query("query") query: String
    ): TrpcResponseWrapper<TrpcResponseDtos.TgdbGameResponse>
    
    @GET("tgdb.getGameImageUrls")
    suspend fun getTgdbGameImageUrls(@Query("gameId") gameId: String): TrpcResponseWrapper<TrpcResponseDtos.GameImageUrls>
    
    @GET("tgdb.getGameImages")
    suspend fun getTgdbGameImages(@Query("gameIds") gameIds: List<String>): TrpcResponseWrapper<Any>
    
    @GET("tgdb.getPlatforms")
    suspend fun getTgdbPlatforms(): TrpcResponseWrapper<List<TrpcResponseDtos.TgdbPlatform>>
    
    // ========================================
    // 18. USER PROFILES ENDPOINTS
    // ========================================
    
    @GET("users.getUserById")
    suspend fun getUserById(@Query("userId") userId: String): TrpcResponseWrapper<TrpcResponseDtos.EnhancedMobileUserProfile>
}