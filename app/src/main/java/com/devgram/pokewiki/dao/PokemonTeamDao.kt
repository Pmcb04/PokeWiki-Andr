package com.devgram.pokewiki.dao

import androidx.room.*
import com.devgram.pokewiki.model.PokemonTeam

@Dao
interface PokemonTeamDao {

    @Query("SELECT * FROM teams_pokemon")
    fun getAll() : MutableList<PokemonTeam>

    @Query("SELECT * FROM teams_pokemon WHERE id = :id ")
    fun get(id : Int) : PokemonTeam

    @Query("SELECT * FROM teams_pokemon WHERE user = :user")
    fun getAll(user : String) : MutableList<PokemonTeam>

    @Update
    fun update(pokemonTeam: PokemonTeam)

    @Insert
    fun insert(pokemonTeam : PokemonTeam)

    @Delete
    fun delete(pokemonTeam : PokemonTeam)
}