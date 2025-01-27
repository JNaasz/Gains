package com.example.gains.features.nutrition

import com.example.gains.database.NutritionLog
import com.example.gains.database.ProteinSource
import java.time.LocalDate

object Util {
    const val CUSTOM = "Custom"
    private const val G_PER_OZ = 28.3495

    val sizeUnits: List<String> = listOf(
        SizeUnit.SERVING.symbol,
        SizeUnit.G.symbol,
        SizeUnit.OZ.symbol,
    )

    private var selections: List<ProteinSource>? = null

    fun getSourceList(
        customSources: List<ProteinSource>,
        defaultSelections: List<ProteinSource>?
    ): List<String> {
        // Merge custom and default sources into one list
        val allSources = customSources + (defaultSelections ?: emptyList())

        selections = allSources

        // Add "Custom" at the beginning and map source names
        return listOf(CUSTOM) + allSources.map { it.name }
    }

    fun getMatchedSelectionData(selection: String): ProteinSource? {
        return if (selection == CUSTOM) {
            null
        } else {
            selections?.find {
                it.name == selection
            }
        }
    }

    private fun gramsToOz(grams: Float): Float {
        return (grams / G_PER_OZ).toFloat()
    }

    private fun ozToGrams(grams: Float): Float {
        return (grams * G_PER_OZ).toFloat()
    }

    fun calculateProtein(log: NutritionLog, source: ProteinSource): Float {
        if (log.unit == SizeUnit.SERVING.symbol) return source.proteinPerServing * log.size

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