package com.example.gains.features.nutrition

import java.time.LocalDate

object Util {
    fun createOptionsList(): List<String> {
        // pull options form config file
        // eventually pull form spread sheet or DB
        // need to make a DB for this so it can be updated and modified from inside and outside the app
        return listOf(CUSTOM, "Chicken", "Beef", "Ham")
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

    val sizeUnits: List<String> = listOf(
        SizeUnit.SERVING.symbol,
        SizeUnit.G.symbol,
        SizeUnit.OZ.symbol,
    )

    val CUSTOM = "Custom"
}