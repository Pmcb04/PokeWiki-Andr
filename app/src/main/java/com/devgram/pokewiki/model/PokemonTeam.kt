package com.devgram.pokewiki.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams_pokemon")
data class PokemonTeam(
    @PrimaryKey(autoGenerate = true) val id : Int,
    val pokemon_ids : ArrayList<Int> // TODO ¿como almacenar en bd?
    )