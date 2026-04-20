package com.example.app_dich_quet_van_ban.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.app_dich_quet_van_ban.data.local.entity.FolderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Query("SELECT * FROM folders WHERE userId = :userId")
    fun getFoldersByUser(userId: Int): Flow<List<FolderEntity>>

    @Insert
    suspend fun insert(folder: FolderEntity)
    @Update
    suspend fun update(folder: FolderEntity)
    @Delete
    suspend fun delete(folder: FolderEntity)
}