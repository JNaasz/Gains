package com.example.gains.features.nutrition

import android.content.Context
import android.content.SharedPreferences
import com.example.gains.database.NutritionDao
import com.example.gains.database.NutritionLog
import com.example.gains.database.ProteinSource
import com.example.gains.database.ProteinSourcesDao
import com.example.gains.features.nutrition.Util.mergeSourceList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.time.LocalDate

interface NutritionRepository {
    fun getLogs(date: LocalDate): Flow<List<NutritionLog>>
    suspend fun addLog(newLog: NutritionLog)
    suspend fun deleteLog(log: NutritionLog)
    fun getProteinTotal(date: LocalDate): Flow<Float>
    fun getCustomProteinSources(scope: CoroutineScope): StateFlow<List<ProteinSource>>
    fun getProteinSourceList(scope: CoroutineScope, context: Context): StateFlow<List<String>>
    suspend fun storeProteinSource(source: ProteinSource)
    fun getDefaultSelections(context: Context): List<ProteinSource>?
    fun getProteinTarget(): Int
    fun setProteinTarget(target: Int)
}

class NutritionRepositoryImpl(
    private val nutritionDao: NutritionDao,
    private val proteinSourcesDao: ProteinSourcesDao,
    private val sharedPreferences: SharedPreferences,
) : NutritionRepository {

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

    override suspend fun storeProteinSource(source: ProteinSource) {
        proteinSourcesDao.addSource(source)
    }

    private var defaultProteinSelections: List<ProteinSource>? = null
    private val _proteinSources = MutableStateFlow<List<ProteinSource>>(emptyList())
    private val _mergedSources = MutableStateFlow<List<String>>(emptyList())

    override fun getCustomProteinSources(scope: CoroutineScope): StateFlow<List<ProteinSource>> {
        scope.launch {
            proteinSourcesDao.getSources()
                .collect { sources ->
                    _proteinSources.value = sources
                }
        }
        return _proteinSources
    }

    override fun getDefaultSelections(context: Context): List<ProteinSource>? {
        if (defaultProteinSelections.isNullOrEmpty()) {
            val json = context.assets.open("proteinLookup.json").bufferedReader()
                .use(BufferedReader::readText)
            defaultProteinSelections = Json.decodeFromString(json)
        }

        return defaultProteinSelections
    }

    override fun getProteinSourceList(scope: CoroutineScope, context: Context): StateFlow<List<String>> {
        scope.launch {
            val customSources = _proteinSources.value
            val defaultSelections = getDefaultSelections(context)

            if (customSources.isEmpty()) {
                // need collect as first() returns an empty list
                getCustomProteinSources(scope).collect { sources ->
                    val mergedList = mergeSourceList(sources, defaultSelections)
                    _mergedSources.value = mergedList
                }
            } else {
                val mergedList = mergeSourceList(customSources, defaultSelections)
                _mergedSources.value = mergedList
            }
        }
        return _mergedSources
    }

    override fun getProteinTarget(): Int {
        return sharedPreferences.getInt(PROTEIN_TARGET_KEY, DEFAULT_PROTEIN_GOAL)
    }

    override fun setProteinTarget(target: Int) {
        sharedPreferences.edit().putInt(PROTEIN_TARGET_KEY, target).apply()
    }

    companion object {
        private const val PROTEIN_TARGET_KEY = "protein_target"
        private const val DEFAULT_PROTEIN_GOAL = 130
    }
}
