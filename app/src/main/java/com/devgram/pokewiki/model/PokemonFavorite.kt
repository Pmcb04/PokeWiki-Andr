package com.devgram.pokewiki.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

/*@Entity(tableName = "pokemon_favorite",
    foreignKeys =
        [
            ForeignKey(
                entity = User::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("id_user"),
                onDelete = CASCADE
            )
        ]
)*/

@Entity(tableName = "pokemon_favorite")
data class PokemonFavorite(
    @ColumnInfo(name = "user") val user : String,
    @ColumnInfo(name = "id_pokemon") val id_pokemon : Int
){
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}
