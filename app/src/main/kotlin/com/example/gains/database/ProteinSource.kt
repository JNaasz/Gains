package com.example.gains.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Entity(tableName = "protein_sources")
data class ProteinSource(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "servingUnit") var servingUnit: String,
    @ColumnInfo(name = "servingSize") var servingSize: Float,
    @ColumnInfo(name = "proteinPerServing") var proteinPerServing: Float
) : Parcelable
