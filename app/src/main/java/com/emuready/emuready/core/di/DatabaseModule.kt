package com.emuready.emuready.core.di

import android.content.Context
import androidx.room.Room
import com.emuready.emuready.data.local.dao.*
import com.emuready.emuready.data.local.database.EmuReadyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideEmuReadyDatabase(@ApplicationContext context: Context): EmuReadyDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            EmuReadyDatabase::class.java,
            "emuready_database"
        )
        .fallbackToDestructiveMigration()
        .build()
    }
    
    @Provides
    fun provideGameDao(database: EmuReadyDatabase): GameDao {
        return database.gameDao()
    }
    
    @Provides
    fun provideListingDao(database: EmuReadyDatabase): GameListingDao {
        return database.listingDao()
    }
    
    @Provides
    fun provideUserDao(database: EmuReadyDatabase): UserDao {
        return database.userDao()
    }
    
    @Provides
    fun provideDeviceDao(database: EmuReadyDatabase): DeviceDao {
        return database.deviceDao()
    }
}