package com.devgram.pokewiki.roomdb

import androidx.room.TypeConverter
import com.devgram.pokewiki.model.Stat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StatConverter{

    @TypeConverter
    fun fromStatList(value: List<Stat>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Stat>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toStatList(value: String): List<Stat> {
        val gson = Gson()
        val type = object : TypeToken<List<Stat>>() {}.type
        return gson.fromJson(value, type)
    }
}
