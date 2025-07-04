package com.emuready.emuready.data.local.dao

import androidx.room.*
import com.emuready.emuready.data.local.entities.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games WHERE title LIKE '%' || :search || '%' ORDER BY title ASC")
    fun searchGames(search: String): Flow<List<GameEntity>>
    
    @Query("SELECT * FROM games WHERE id = :gameId")
    suspend fun getGameById(gameId: String): GameEntity?
    
    @Query("SELECT * FROM games ORDER BY lastUpdated DESC LIMIT :limit")
    fun getRecentGames(limit: Int): Flow<List<GameEntity>>
    
    @Query("SELECT * FROM games WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavoriteGames(): Flow<List<GameEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameEntity)
    
    @Update
    suspend fun updateGame(game: GameEntity)
    
    @Query("UPDATE games SET isFavorite = :isFavorite WHERE id = :gameId")
    suspend fun updateFavoriteStatus(gameId: String, isFavorite: Boolean)
    
    @Delete
    suspend fun deleteGame(game: GameEntity)
    
    @Query("DELETE FROM games")
    suspend fun deleteAllGames()
}