package com.devgram.pokewiki.dao

import androidx.room.*
import com.devgram.pokewiki.model.Achievement

@Dao
interface AchievementDao {

    @Query("SELECT * FROM achievements")
    fun getAll() : MutableList<Achievement>

    @Query("SELECT * FROM achievements WHERE id = :id")
    fun get(id : Int) : Achievement

    @Query("SELECT * FROM achievements WHERE name = :name and user = :user")
    fun get(name : String, user: String) : Achievement

    @Query("SELECT * FROM achievements WHERE user = :user")
    fun getAll(user : String) : MutableList<Achievement>

    @Update
    fun update(achievement: Achievement)

    @Insert
    fun insert(achievement : Achievement)

    @Delete
    fun delete(achievement : Achievement)

}