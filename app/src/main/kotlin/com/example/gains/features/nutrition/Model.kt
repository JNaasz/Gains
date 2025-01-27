package com.example.gains.features.nutrition

import kotlinx.serialization.Serializable

enum class SizeUnit(val symbol: String) {
    G("g"),
    OZ("oz"),
    SERVING("serving")
}

@Serializable
data class SourceItem(
    val name: String,
    val servingUnit: String,
    val servingSize: Float,
    val proteinPerServing: Float
)