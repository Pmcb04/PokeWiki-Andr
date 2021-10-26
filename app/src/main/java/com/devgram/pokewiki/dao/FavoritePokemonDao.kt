package com.devgram.pokewiki.dao

import androidx.room.*
import com.devgram.pokewiki.model.FavoritePokemon

@Dao
interface FavoritePokemonDao {

    @Query("SELECT * FROM achievements")
    fun getAll() : List<FavoritePokemon>

    @Query("SELECT * FROM achievements WHERE id = :id ")
    fun get(id : Int) : FavoritePokemon

    @Update
    fun update(achievement: FavoritePokemon)

    @Insert
    fun insert(achievement : FavoritePokemon)

    @Delete
    fun delete(achievement : FavoritePokemon)

}