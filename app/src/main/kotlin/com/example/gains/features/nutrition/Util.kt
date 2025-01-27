package com.example.gains.features.nutrition

import android.content.Context
import android.util.Log
import com.example.gains.database.NutritionLog
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.time.LocalDate

object Util {
    const val CUSTOM = "Custom"
    private val gramsPerOz = 28.3495

    val sizeUnits: List<String> = listOf(
        SizeUnit.SERVING.symbol,
        SizeUnit.G.symbol,
        SizeUnit.OZ.symbol,
    )

    private var defaultSelections: List<SourceItem>? = null
    private var selections: List<SourceItem>? = null

    private fun setDefaultSelections(context: Context) {
        val json = context.assets.open("proteinLookup.json").bufferedReader().use(BufferedReader::readText)
        defaultSelections = Json.decodeFromString(json)
        Log.d("TAG", "setDefaultSelections: ${defaultSelections.toString()}")
    }

    private fun getSourceDataItems(context: Context): List<SourceItem>? {
        if (defaultSelections.isNullOrEmpty()) {
            setDefaultSelections(context)
        }

        // pull the user input selections from DB
        // add those + default selections to sourceSelections
        selections = defaultSelections
        return defaultSelections
    }

    fun getSourceList(context: Context): List<String> {
        val list = mutableListOf(CUSTOM)
        getSourceDataItems(context)?.forEach {
            list.add(it.name)
        }

        return list.toList()
    }

    fun getMatchedSelectionData(selection: String): SourceItem? {
        return if (selection == CUSTOM) {
            null
        } else {
            selections?.find {
                it.name == selection
            }
        }
    }

    private fun gramsToOz(grams: Float): Float {
        return (grams / gramsPerOz).toFloat()
    }

    private fun ozToGrams(grams: Float): Float {
        return (grams * gramsPerOz).toFloat()
    }

    fun calculateProtein(log: NutritionLog, source: SourceItem): Float {
        if (log.unit == SizeUnit.SERVING.symbol) return source.proteinPerServing

        val size = when (source.servingUnit) {
            log.unit -> log.size
            SizeUnit.OZ.symbol -> gramsToOz(log.size)
            else -> ozToGrams(log.size)
        }

        return (size * source.proteinPerServing) / source.servingSize
    }

    fun formatDate(date: LocalDate): String {
        if (date == LocalDate.now()) {
            return "Today"
        }

        val month = date.monthValue // month
        val day = date.dayOfMonth
        val year = date.year
        return "$month/$day/$year"
    }
}