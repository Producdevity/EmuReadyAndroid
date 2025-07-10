package com.emuready.emuready.di

import android.content.Context
import com.emuready.emuready.data.services.DeviceDetectionService
import com.emuready.emuready.data.services.DeviceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeviceModule {

    @Provides
    @Singleton
    fun provideDeviceDetectionService(
        @ApplicationContext context: Context
    ): DeviceDetectionService {
        return DeviceDetectionService(context)
    }

    @Provides
    @Singleton
    fun provideDeviceManager(
        @ApplicationContext context: Context,
        deviceDetectionService: DeviceDetectionService
    ): DeviceManager {
        return DeviceManager(context, deviceDetectionService)
    }
}