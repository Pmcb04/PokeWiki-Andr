package com.devgram.pokewiki.dao

import androidx.room.*
import com.devgram.pokewiki.model.Pokemon

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon_table")
    fun getAll() : List<Pokemon>

    @Query("SELECT * FROM pokemon_table WHERE id = :id ")
    fun get(id : Int) : Pokemon

    @Query("SELECT id FROM pokemon_table WHERE id = :id ")
    fun exist(id : Int) : Int

    @Update
    fun update(pokemon: Pokemon)

    @Insert
    fun insert(pokemon : Pokemon)

    @Delete
    fun delete(pokemon : Pokemon)

}