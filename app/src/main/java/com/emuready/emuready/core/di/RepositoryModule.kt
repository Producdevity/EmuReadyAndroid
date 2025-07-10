package com.emuready.emuready.core.di

import com.emuready.emuready.data.repositories.*
import com.emuready.emuready.data.services.EdenLaunchService
import com.emuready.emuready.domain.repositories.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindGameRepository(
        gameRepositoryImpl: GameRepositoryImpl
    ): GameRepository
    
    @Binds
    @Singleton
    abstract fun bindListingRepository(
        listingRepositoryImpl: ListingRepositoryImpl
    ): ListingRepository
    
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
    
    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        preferencesRepositoryImpl: PreferencesRepositoryImpl
    ): PreferencesRepository
    
    @Binds
    @Singleton
    abstract fun bindDeviceRepository(
        deviceRepositoryImpl: DeviceRepositoryImpl
    ): DeviceRepository
    
    @Binds
    @Singleton
    abstract fun bindStatsRepository(
        statsRepositoryImpl: StatsRepositoryImpl
    ): StatsRepository
    
    @Binds
    @Singleton
    abstract fun bindTrustRepository(
        trustRepositoryImpl: TrustRepositoryImpl
    ): TrustRepository
    
    @Binds
    @Singleton
    abstract fun bindReportRepository(
        reportRepositoryImpl: ReportRepositoryImpl
    ): ReportRepository
    
    @Binds
    @Singleton
    abstract fun bindCustomFieldRepository(
        customFieldRepositoryImpl: CustomFieldRepositoryImpl
    ): CustomFieldRepository
    
    @Binds
    @Singleton
    abstract fun bindEmulatorRepository(
        emulatorRepositoryImpl: EmulatorRepositoryImpl
    ): EmulatorRepository
    
    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository
}