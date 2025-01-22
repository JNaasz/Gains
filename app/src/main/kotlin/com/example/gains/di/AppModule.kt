package com.example.gains.di

import android.content.Context
import androidx.room.Room
import com.example.gains.database.GainsRoomDB
import com.example.gains.database.NutritionDao
import com.example.gains.features.nutrition.NutritionRepository
import com.example.gains.features.nutrition.NutritionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGainsRoomDB(@ApplicationContext context: Context): GainsRoomDB {
        return Room.databaseBuilder(
            context,
            GainsRoomDB::class.java,
            "gains_database"
        ).build()
    }

    @Provides
    fun provideNutritionDao(database: GainsRoomDB): NutritionDao {
        return database.nutritionDao()
    }
}