package com.devgram.pokewiki.roomdb

import androidx.room.TypeConverter
import com.devgram.pokewiki.model.*
import com.google.gson.reflect.TypeToken

import com.google.gson.Gson
import java.util.*


class PokemonConverter{

    @TypeConverter
    fun fromPokemonList(value: List<Pokemon>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Pokemon>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toPokemonList(value: String): List<Pokemon> {
        val gson = Gson()
        val type = object : TypeToken<List<Pokemon>>() {}.type
        return gson.fromJson(value, type)
    }
}



/*
class MoveConverter : TConverter<List<Move>>()

class StatConverter : TConverter<List<Stat>>()

class TypeConverter : TConverter<List<Type>>()*/