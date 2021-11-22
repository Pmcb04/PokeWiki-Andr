package com.devgram.pokewiki.dao

import androidx.room.*
import com.devgram.pokewiki.model.Pokemon
import com.devgram.pokewiki.model.PokemonFavorite

@Dao
interface FavoritePokemonDao {

    @Query("SELECT id FROM pokemon_favorite WHERE id_pokemon = :id_pokemon and user = :user")
    fun isFavorite(id_pokemon : Int, user : String) : Int

    @Query("SELECT * FROM pokemon_favorite WHERE user = :user")
    fun getAll(user : String) : MutableList<PokemonFavorite>

    @Insert
    fun insert(pokemonFavorite : PokemonFavorite)

    @Query("DELETE FROM pokemon_favorite WHERE id = :id")
    fun delete(id : Int)

}