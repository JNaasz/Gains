package com.example.gains.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProteinSourcesDao {
    @Query("SELECT * FROM protein_sources")
    fun getSources(): Flow<List<ProteinSource>>

    @Insert
    suspend fun addSource(source: ProteinSource)

    @Update
    suspend fun updateSource(source: ProteinSource)

    @Delete
    suspend fun deleteSource(source: ProteinSource)
}