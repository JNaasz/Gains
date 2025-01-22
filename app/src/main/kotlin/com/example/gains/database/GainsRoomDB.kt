package com.example.gains.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [(NutritionLog::class)], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GainsRoomDB: RoomDatabase() {
    abstract fun nutritionDao(): NutritionDao
}