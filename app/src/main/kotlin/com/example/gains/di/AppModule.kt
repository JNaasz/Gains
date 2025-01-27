package com.example.gains.di

import android.content.Context
import androidx.room.Room
import com.example.gains.database.GainsRoomDB
import com.example.gains.database.Migrations
import com.example.gains.database.NutritionDao
import com.example.gains.database.ProteinSourcesDao
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
        )
            .addMigrations(Migrations.MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideNutritionDao(database: GainsRoomDB): NutritionDao {
        return database.nutritionDao()
    }

    @Provides
    fun provideProteinSourcesDao(database: GainsRoomDB): ProteinSourcesDao {
        return database.proteinSourcesDao()
    }

    @Provides
    fun provideNutritionRepository(
        nutritionDao: NutritionDao,
        proteinSourcesDao: ProteinSourcesDao
    ): NutritionRepository {
        return NutritionRepositoryImpl(nutritionDao, proteinSourcesDao)
    }
}