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
    suspend fun validateToken(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.ValidateTokenResponse>
    
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
    suspend fun getListings(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.MobileListingsResponse>
    
    @GET("listings.getFeaturedListings")
    suspend fun getFeaturedListings(): TrpcResponseWrapper<List<TrpcResponseDtos.MobileListing>>
    
    @GET("listings.getListingsByGame")
    suspend fun getListingsByGame(@Query("input") input: String): TrpcResponseWrapper<List<TrpcResponseDtos.MobileListing>>
    
    @GET("listings.getListingById")
    suspend fun getListingById(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.MobileListing>
    
    @GET("listings.getUserListings")
    suspend fun getUserListings(@Query("input") input: String): TrpcResponseWrapper<List<TrpcResponseDtos.MobileListing>>
    
    @POST("listings.createListing")
    suspend fun createListing(@Body request: TrpcRequest<TrpcRequestDtos.CreateListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobileListing>
    
    @POST("listings.updateListing")
    suspend fun updateListing(@Body request: TrpcRequest<TrpcRequestDtos.UpdateListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobileListing>
    
    @POST("listings.deleteListing")
    suspend fun deleteListing(@Body request: TrpcRequest<TrpcRequestDtos.DeleteListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("listings.voteListing")
    suspend fun voteListing(@Body request: TrpcRequest<TrpcRequestDtos.VoteListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @GET("listings.getUserVote")
    suspend fun getUserVote(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.UserVoteResponse>
    
    // ========================================
    // 3. COMMENTS ENDPOINTS  
    // ========================================
    
    @GET("listings.getListingComments")
    suspend fun getListingComments(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.MobileCommentsResponse>
    
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
    suspend fun getPcListings(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.MobilePcListingsResponse>
    
    @POST("pcListings.createPcListing")
    suspend fun createPcListing(@Body request: TrpcRequest<TrpcRequestDtos.CreatePcListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobilePcListing>
    
    @POST("pcListings.updatePcListing")
    suspend fun updatePcListing(@Body request: TrpcRequest<TrpcRequestDtos.UpdatePcListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobilePcListing>
    
    @GET("pcListings.getCpus")
    suspend fun getCpus(@Query("input") input: String): TrpcResponseWrapper<List<TrpcResponseDtos.MobileCpu>>
    
    @GET("pcListings.getGpus")
    suspend fun getGpus(@Query("input") input: String): TrpcResponseWrapper<List<TrpcResponseDtos.MobileGpu>>
    
    // ========================================
    // 5. PC PRESETS ENDPOINTS
    // ========================================
    
    @GET("pcListings.get")
    suspend fun getPcPresets(@Query("input") input: String): TrpcResponseWrapper<List<TrpcResponseDtos.MobilePcPreset>>
    
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
    suspend fun getGames(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.TrpcPaginatedGamesResponse>
    
    @GET("games.getPopularGames")
    suspend fun getPopularGames(): TrpcResponseWrapper<TrpcResponseDtos.TrpcPaginatedGamesResponse>
    
    @GET("games.searchGames")
    suspend fun searchGames(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.TrpcPaginatedGamesResponse>
    
    @GET("games.getGameById")
    suspend fun getGameById(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.TrpcSingleGameResponse>
    
    // ========================================
    // 7. DEVICES ENDPOINTS
    // ========================================
    
    @GET("devices.getDevices")
    suspend fun getDevices(@Query("input") input: String): TrpcResponseWrapper<List<TrpcResponseDtos.MobileDevice>>
    
    @GET("devices.getDeviceBrands")
    suspend fun getDeviceBrands(): TrpcResponseWrapper<List<TrpcResponseDtos.MobileBrand>>
    
    @GET("devices.getSocs")
    suspend fun getSocs(): TrpcResponseWrapper<List<TrpcResponseDtos.MobileSoc>>
    
    // ========================================
    // 8. EMULATORS ENDPOINTS
    // ========================================
    
    @GET("emulators.getEmulators")
    suspend fun getEmulators(@Query("input") input: String): TrpcResponseWrapper<List<TrpcResponseDtos.MobileEmulator>>
    
    @GET("emulators.getEmulatorById")
    suspend fun getEmulatorById(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.MobileEmulator>
    
    // ========================================
    // 9. GENERAL DATA ENDPOINTS
    // ========================================
    
    @GET("general.getAppStats")
    suspend fun getAppStats(): TrpcResponseWrapper<TrpcResponseDtos.TrpcStatsResponse>
    
    @GET("general.getSystems")
    suspend fun getSystems(): TrpcResponseWrapper<List<TrpcResponseDtos.MobileSystem>>
    
    @GET("general.getPerformanceScales")
    suspend fun getPerformanceScales(): TrpcResponseWrapper<List<TrpcResponseDtos.MobilePerformance>>
    
    @GET("general.getSearchSuggestions")
    suspend fun getSearchSuggestions(@Query("input") input: String): TrpcResponseWrapper<List<TrpcResponseDtos.MobileSearchSuggestion>>
    
    
    // ========================================
    // 10. NOTIFICATIONS ENDPOINTS
    // ========================================
    
    @GET("notifications.getNotifications")
    suspend fun getNotifications(@Query("input") input: String): TrpcResponseWrapper<List<TrpcResponseDtos.MobileNotification>>
    
    @POST("notifications.getUnreadNotificationCount")
    suspend fun getUnreadNotificationCount(): TrpcResponseWrapper<TrpcResponseDtos.CountResponse>
    
    @POST("notifications.markNotificationAsRead")
    suspend fun markNotificationAsRead(@Body request: TrpcRequest<TrpcRequestDtos.MarkNotificationReadSchema>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("notifications.markAllNotificationsAsRead")
    suspend fun markAllNotificationsAsRead(): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    // ========================================
    // 11. USER PREFERENCES ENDPOINTS
    // ========================================
    
    @POST("preferences.getUserPreferences")
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
    suspend fun getUserProfile(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.MobileUserProfile>
    
    @POST("preferences.updateProfile")
    suspend fun updateUserProfile(@Body request: TrpcRequest<TrpcRequestDtos.UpdateProfileSchema>): TrpcResponseWrapper<TrpcResponseDtos.MobileUserProfile>
    
    // ========================================
    // 12. DEVELOPER VERIFICATION ENDPOINTS
    // ========================================
    
    @POST("developers.getMyVerifiedEmulators")
    suspend fun getMyVerifiedEmulators(): TrpcResponseWrapper<List<TrpcResponseDtos.MobileEmulatorVerification>>
    
    @GET("developers.isVerifiedDeveloper")
    suspend fun isVerifiedDeveloper(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.VerifyDeveloperResponse>
    
    @POST("developers.verifyListing")
    suspend fun verifyListing(@Body request: TrpcRequest<TrpcRequestDtos.VerifyListingSchema>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @POST("developers.removeVerification")
    suspend fun removeVerification(@Body request: TrpcRequest<TrpcRequestDtos.RemoveVerificationSchema>): TrpcResponseWrapper<TrpcResponseDtos.SuccessResponse>
    
    @GET("developers.getListingVerifications")
    suspend fun getListingVerifications(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.MobileListingVerificationsResponse>
    
    @GET("developers.getMyVerifications")
    suspend fun getMyVerifications(@Query("input") input: String): TrpcResponseWrapper<List<TrpcResponseDtos.MobileVerification>>
    
    // ========================================
    // 13. CONTENT REPORTING ENDPOINTS
    // ========================================
    
    @POST("listingReports.create")
    suspend fun createListingReport(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.ListingReportResponse>
    
    @GET("listingReports.checkUserHasReports")
    suspend fun checkUserHasReports(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.UserReportsInfo>
    
    // ========================================
    // 14. TRUST SYSTEM ENDPOINTS
    // ========================================
    
    @POST("trust.getMyTrustInfo")
    suspend fun getMyTrustInfo(): TrpcResponseWrapper<TrpcResponseDtos.TrustInfo>
    
    @GET("trust.getUserTrustInfo")
    suspend fun getUserTrustInfo(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.TrustInfo>
    
    @POST("trust.getTrustLevels")
    suspend fun getTrustLevels(): TrpcResponseWrapper<List<TrpcResponseDtos.MobileTrustLevel>>
    
    // ========================================
    // 15. CUSTOM FIELDS ENDPOINTS
    // ========================================
    
    @GET("customFieldDefinitions.getByEmulator")
    suspend fun getCustomFieldsByEmulator(@Query("input") input: String): TrpcResponseWrapper<List<TrpcResponseDtos.MobileCustomFieldDefinition>>
    
    // ========================================
    // 16. HARDWARE DATA ENDPOINTS
    // ========================================
    
    // CPUs
    @GET("cpus.get")
    suspend fun getCpusEnhanced(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.HardwarePaginationResponse<TrpcResponseDtos.MobileCpu>>
    
    @GET("cpus.getById")
    suspend fun getCpuById(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.MobileCpu>
    
    // GPUs
    @GET("gpus.get")
    suspend fun getGpusEnhanced(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.HardwarePaginationResponse<TrpcResponseDtos.MobileGpu>>
    
    @GET("gpus.getById")
    suspend fun getGpuById(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.MobileGpu>
    
    // SoCs
    @POST("socs.get")
    suspend fun getSocsEnhanced(): TrpcResponseWrapper<TrpcResponseDtos.HardwarePaginationResponse<TrpcResponseDtos.MobileSoc>>
    
    @GET("socs.getById")
    suspend fun getSocById(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.MobileSoc>
    
    // Device Brands
    @POST("deviceBrands.get")
    suspend fun getDeviceBrandsEnhanced(): TrpcResponseWrapper<List<TrpcResponseDtos.MobileDeviceBrand>>
    
    @GET("deviceBrands.getById")
    suspend fun getDeviceBrandById(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.MobileDeviceBrand>
    
    // ========================================
    // 17. EXTERNAL GAME DATA ENDPOINTS
    // ========================================
    
    // RAWG Integration
    @POST("rawg.searchGameImages")
    suspend fun searchRawgGameImages(): TrpcResponseWrapper<Map<String, List<TrpcResponseDtos.GameImageOption>>>
    
    @GET("rawg.searchGames")
    suspend fun searchRawgGames(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.RawgGameResponse>
    
    @POST("rawg.getGameImages")
    suspend fun getRawgGameImages(): TrpcResponseWrapper<List<TrpcResponseDtos.GameImageOption>>
    
    // TGDB Integration
    @POST("tgdb.searchGameImages")
    suspend fun searchTgdbGameImages(): TrpcResponseWrapper<Map<String, List<TrpcResponseDtos.GameImageOption>>>
    
    @GET("tgdb.searchGames")
    suspend fun searchTgdbGames(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.TgdbGameResponse>
    
    @POST("tgdb.getGameImageUrls")
    suspend fun getTgdbGameImageUrls(): TrpcResponseWrapper<TrpcResponseDtos.GameImageUrls>
    
    @POST("tgdb.getGameImages")
    suspend fun getTgdbGameImages(): TrpcResponseWrapper<Any>
    
    @POST("tgdb.getPlatforms")
    suspend fun getTgdbPlatforms(): TrpcResponseWrapper<List<TrpcResponseDtos.TgdbPlatform>>
    
    // ========================================
    // 18. USER PROFILES ENDPOINTS
    // ========================================
    
    @GET("users.getUserById")
    suspend fun getUserById(@Query("input") input: String): TrpcResponseWrapper<TrpcResponseDtos.EnhancedMobileUserProfile>
}