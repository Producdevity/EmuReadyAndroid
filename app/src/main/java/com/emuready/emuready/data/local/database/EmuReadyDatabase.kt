package com.emuready.emuready.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import com.emuready.emuready.data.local.dao.*
import com.emuready.emuready.data.local.entities.*

@Database(
    entities = [
        GameEntity::class,
        GameListingEntity::class,
        UserEntity::class,
        DeviceEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class EmuReadyDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun listingDao(): GameListingDao
    abstract fun userDao(): UserDao
    abstract fun deviceDao(): DeviceDao
    
    companion object {
        @Volatile
        private var INSTANCE: EmuReadyDatabase? = null
        
        fun getDatabase(context: Context): EmuReadyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmuReadyDatabase::class.java,
                    "emuready_database"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Create indexes for better query performance
                        db.execSQL("CREATE INDEX IF NOT EXISTS index_games_title ON games(title)")
                        db.execSQL("CREATE INDEX IF NOT EXISTS index_listings_game_id ON game_listings(gameId)")
                        db.execSQL("CREATE INDEX IF NOT EXISTS index_listings_device_id ON game_listings(deviceId)")
                        db.execSQL("CREATE INDEX IF NOT EXISTS index_listings_user_id ON game_listings(userId)")
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}