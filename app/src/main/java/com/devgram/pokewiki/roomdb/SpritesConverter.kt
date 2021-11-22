package com.devgram.pokewiki.roomdb

import androidx.room.TypeConverter
import com.devgram.pokewiki.model.Sprites
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SpritesConverter {

    @TypeConverter
    fun fromSprites(value: Sprites): String {
        val gson = Gson()
        val type = object : TypeToken<Sprites>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toSprites(value: String): Sprites {
        val gson = Gson()
        val type = object : TypeToken<Sprites>() {}.type
        return gson.fromJson(value, type)
    }
}