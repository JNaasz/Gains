package com.example.gains.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
@Entity(tableName = "nutrition")
data class NutritionLog(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "date")
    var date: LocalDate = LocalDate.now(),
    @ColumnInfo(name = "foodName")
    var foodName: String,
    @ColumnInfo(name = "size")
    var size: String,
    @ColumnInfo(name = "protein")
    var protein: Int,
) : Parcelable