package com.example.gains.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS protein_sources (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                servingUnit TEXT NOT NULL,
                proteinPerServing REAL NOT NULL,
                servingSize REAL NOT NULL
            )
            """.trimIndent()
            )
        }
    }
}
