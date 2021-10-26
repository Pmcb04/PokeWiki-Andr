package com.devgram.pokewiki.dao

import androidx.room.*
import com.devgram.pokewiki.model.PokemonTeam

@Dao
interface PokemonTeamDao {

    @Query("SELECT * FROM achievements")
    fun getAll() : List<PokemonTeam>

    @Query("SELECT * FROM achievements WHERE id = :id ")
    fun get(id : Int) : PokemonTeam

    @Update
    fun update(achievement: PokemonTeam)

    @Insert
    fun insert(achievement : PokemonTeam)

    @Delete
    fun delete(achievement : PokemonTeam)

}