package com.devgram.pokewiki.model

import androidx.room.*
import com.devgram.pokewiki.roomdb.MoveConverter
import com.devgram.pokewiki.roomdb.SpritesConverter
import com.devgram.pokewiki.roomdb.StatConverter
import com.devgram.pokewiki.roomdb.TypePokemonConverter
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable

@Entity(tableName = "pokemon_table")
data class Pokemon(
    @PrimaryKey @SerializedName("id") @Expose var id: Int? = null,
    @ColumnInfo(name = "name") @SerializedName("name") @Expose var name: String? = null,
    @SerializedName("moves") @Expose @TypeConverters(MoveConverter::class) var moves: List<Info>? = null,
    @SerializedName("sprites") @Expose @TypeConverters(SpritesConverter::class) var sprites: Sprites? = null,
    @SerializedName("stats") @Expose @TypeConverters(StatConverter::class) var stats: List<Stat>? = null,
    @SerializedName("types") @Expose @TypeConverters(TypePokemonConverter::class) var types: List<Type>? = null
) : Serializable