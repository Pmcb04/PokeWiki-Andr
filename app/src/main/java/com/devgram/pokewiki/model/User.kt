package com.devgram.pokewiki.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "email") val email : String,
    @ColumnInfo(name = "password") val password : String
){
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}
