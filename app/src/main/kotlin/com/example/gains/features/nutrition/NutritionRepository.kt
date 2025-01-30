package com.example.gains.features.nutrition

import android.content.Context
import com.example.gains.database.NutritionDao
import com.example.gains.database.NutritionLog
import com.example.gains.database.ProteinSource
import com.example.gains.database.ProteinSourcesDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.time.LocalDate

interface NutritionRepository {
    fun getLogs(date: LocalDate): Flow<List<NutritionLog>>
    suspend fun addLog(newLog: NutritionLog)
    suspend fun deleteLog(log: NutritionLog)
    fun getProteinTotal(date: LocalDate): Flow<Float>
    fun getProteinSources(): Flow<List<ProteinSource>>
    suspend fun storeProteinSource(source: ProteinSource)
    fun getDefaultSelections(context: Context): List<ProteinSource>?
}

class NutritionRepositoryImpl(
    private val nutritionDao: NutritionDao,
    private val proteinSourcesDao: ProteinSourcesDao,
) : NutritionRepository {
    private var defaultProteinSelections: List<ProteinSource>? = null

    override fun getLogs(date: LocalDate): Flow<List<NutritionLog>> =
        nutritionDao.getLogs(date)

    override suspend fun addLog(newLog: NutritionLog) {
        nutritionDao.addLog(newLog)
    }

    override suspend fun deleteLog(log: NutritionLog) {
        nutritionDao.deleteLog(log)
    }

    override fun getProteinTotal(date: LocalDate): Flow<Float> {
        return getLogs(date)
            .map { logs ->
                logs.sumOf { it.protein.toBigDecimal() } // need to sum as BigDecimal
                    .toFloat()
            }
    }

    override fun getProteinSources(): Flow<List<ProteinSource>> = proteinSourcesDao.getSources()

    override suspend fun storeProteinSource(source: ProteinSource) {
        proteinSourcesDao.addSource(source)
    }

    override fun getDefaultSelections(context: Context): List<ProteinSource>? {
        if (defaultProteinSelections.isNullOrEmpty()) {
            val json = context.assets.open("proteinLookup.json").bufferedReader()
                .use(BufferedReader::readText)
            defaultProteinSelections = Json.decodeFromString(json)
        }

        return defaultProteinSelections
    }
}
