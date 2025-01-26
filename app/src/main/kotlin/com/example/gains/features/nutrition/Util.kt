package com.example.gains.features.nutrition

object Util {
    fun createOptionsList(): List<String> {
        // pull options form config file
        // eventually pull form spread sheet or DB
        // need to make a DB for this so it can be updated and modified from inside and outside the app
        return listOf("Custom", "Chicken", "Beef", "Ham")
    }

    val sizeUnits: List<String> = listOf(
        SizeUnit.G.symbol,
        SizeUnit.OZ.symbol,
        SizeUnit.SERVING.symbol
    )
}