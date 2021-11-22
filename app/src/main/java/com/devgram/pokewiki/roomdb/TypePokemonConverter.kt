package com.devgram.pokewiki.roomdb

import androidx.room.TypeConverter
import com.devgram.pokewiki.model.Type
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypePokemonConverter{

    @TypeConverter
    fun fromTypePokemonList(value: List<Type>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Type>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toTypePokemonList(value: String): List<Type> {
        val gson = Gson()
        val type = object : TypeToken<List<Type>>() {}.type
        return gson.fromJson(value, type)
    }
}
