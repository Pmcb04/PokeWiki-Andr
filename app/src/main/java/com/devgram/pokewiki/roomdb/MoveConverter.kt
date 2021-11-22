package com.devgram.pokewiki.roomdb

import androidx.room.TypeConverter
import com.devgram.pokewiki.model.Info
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MoveConverter{

    @TypeConverter
    fun fromMoveList(value: List<Info>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Info>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toMoveList(value: String): List<Info> {
        val gson = Gson()
        val type = object : TypeToken<List<Info>>() {}.type
        return gson.fromJson(value, type)
    }
}
