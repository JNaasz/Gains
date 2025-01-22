package com.example.gains.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

// what tables do we need
// nutrition (track daily protein intake)
// custom protein entries (store commonly used custom protein items)

@Dao
interface NutritionDao {
    @Query("SELECT * FROM nutrition")
    fun getAllLogs(): Flow<List<NutritionLog>>

    @Query("SELECT * FROM nutrition WHERE date = :logDate")
    fun getLogs(logDate: LocalDate): Flow<List<NutritionLog>>

    @Insert
    suspend fun addLog(log: NutritionLog)

    @Update
    suspend fun updateLog(log: NutritionLog)

    @Delete
    suspend fun deleteLog(log: NutritionLog)
}