package com.devgram.pokewiki.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey() val id : Int,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "description") val description : String,
    @ColumnInfo(name = "percentage") val percentage : Int
    )