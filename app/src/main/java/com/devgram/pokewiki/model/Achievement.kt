package com.devgram.pokewiki.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class Achievement(
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "description") val description : String,
    @ColumnInfo(name = "percentage") var percentage : Int,
    @ColumnInfo(name = "user") val user : String
){

    @PrimaryKey(autoGenerate = true) var id : Int = 0
}