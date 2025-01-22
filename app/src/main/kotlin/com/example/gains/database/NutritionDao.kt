package com.example.gains.database

import androidx.room.*
import java.time.LocalDate

// what tables do we need
// nutrition (track daily protein intake)
// custom protein entries (store commonly used custom protein items)

@Dao
interface NutritionDao {
    @Query("Select * FROM nutrition")
    fun getAllLogs(): List<NutritionLog>

    @Query("Select * FROM nutrition WHERE date = :logDate")
    fun getLogByDate(logDate: LocalDate): List<NutritionLog>

    @Insert
    suspend fun addLog(log: NutritionLog)

    @Update
    suspend fun updateLog(log: NutritionLog)

    @Delete
    suspend fun deleteLog(log: NutritionLog)
}