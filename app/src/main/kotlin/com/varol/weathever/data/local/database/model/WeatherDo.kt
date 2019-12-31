package com.varol.weathever.data.local.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "tb_weathers")
data class WeatherDo(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "city")
    val city: String,
    @ColumnInfo(name = "degree")
    val degreeInCelsius: Double
) : Parcelable