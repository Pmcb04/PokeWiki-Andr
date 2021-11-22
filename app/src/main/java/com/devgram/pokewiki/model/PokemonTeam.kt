package com.devgram.pokewiki.model

import androidx.room.*
import com.devgram.pokewiki.roomdb.PokemonConverter
import java.io.Serializable

@Entity(tableName = "teams_pokemon")

data class PokemonTeam(
    @TypeConverters(PokemonConverter::class) var pokemon_team: MutableList<Pokemon>,
    @ColumnInfo(name = "user") val user : String,
    @ColumnInfo(name = "team_name") var team_name : String
) : Serializable {
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}