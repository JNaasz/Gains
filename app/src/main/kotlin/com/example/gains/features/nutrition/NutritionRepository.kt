package com.example.gains.features.nutrition

import android.util.Log
import com.example.gains.database.NutritionDao
import com.example.gains.database.NutritionLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

interface NutritionRepository {
    fun getLogs(date: LocalDate): Flow<List<NutritionLog>>
    suspend fun addLog(newLog: NutritionLog)
    // getLogs by date range?
    // addLog
    // updateLog
    // deleteLog
    // calculate protein? or in util function
}

class NutritionRepositoryImpl(
    private val nutritionDao: NutritionDao
) : NutritionRepository {

    override fun getLogs(date: LocalDate): Flow<List<NutritionLog>> = nutritionDao.getLogs(LocalDate.now())

    override suspend fun addLog(newLog: NutritionLog) {
        nutritionDao.addLog(newLog)
    }
}
