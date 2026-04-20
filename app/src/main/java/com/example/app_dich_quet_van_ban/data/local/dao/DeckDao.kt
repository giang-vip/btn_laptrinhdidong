package com.example.app_dich_quet_van_ban.data.local.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.app_dich_quet_van_ban.data.local.entity.DeckEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface DeckDao {
    @Query("SELECT * FROM decks WHERE folderId = :folderId")
    fun getDecksByFolder(folderId: Int): Flow<List<DeckEntity>>

    @Insert suspend fun insert(deck: DeckEntity)
    @Update suspend fun update(deck: DeckEntity)
    @Delete
    suspend fun delete(deck: DeckEntity)
}