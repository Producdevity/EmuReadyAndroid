package com.emuready.emuready.data.local.dao

import androidx.room.*
import com.emuready.emuready.data.local.entities.GameListingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameListingDao {
    @Query("SELECT * FROM game_listings WHERE gameId = :gameId ORDER BY createdAt DESC")
    fun getListingsByGameId(gameId: String): Flow<List<GameListingEntity>>
    
    @Query("SELECT * FROM game_listings WHERE userId = :userId ORDER BY createdAt DESC")
    fun getListingsByUserId(userId: String): Flow<List<GameListingEntity>>
    
    @Query("SELECT * FROM game_listings WHERE deviceId = :deviceId ORDER BY createdAt DESC")
    fun getListingsByDeviceId(deviceId: String): Flow<List<GameListingEntity>>
    
    @Query("SELECT * FROM game_listings WHERE id = :listingId")
    suspend fun getListingById(listingId: String): GameListingEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: GameListingEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListings(listings: List<GameListingEntity>)
    
    @Update
    suspend fun updateListing(listing: GameListingEntity)
    
    @Delete
    suspend fun deleteListing(listing: GameListingEntity)
    
    @Query("DELETE FROM game_listings WHERE id = :listingId")
    suspend fun deleteListingById(listingId: String)
}