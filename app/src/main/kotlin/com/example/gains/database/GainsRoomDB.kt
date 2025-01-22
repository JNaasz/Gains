package com.example.gains.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [(NutritionLog::class)], version = 1, exportSchema = false)
abstract class GainsRoomDB: RoomDatabase() {
    abstract fun nutritionDao(): NutritionDao
}