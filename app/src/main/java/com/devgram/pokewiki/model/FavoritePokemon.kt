package com.devgram.pokewiki.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_pokemon")
data class FavoritePokemon(
    @PrimaryKey val id : Int
    )
