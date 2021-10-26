package com.devgram.pokewiki.dao

import androidx.room.*
import com.devgram.pokewiki.model.Achievement

@Dao
interface AchievementDao {

    @Query("SELECT * FROM achievements")
    fun getAll() : List<Achievement>

    @Query("SELECT * FROM achievements WHERE id = :id")
    fun get(id : Int) : Achievement

    @Update
    fun update(achievement: Achievement)

    @Insert
    fun insert(achievement : Achievement)

    @Delete
    fun delete(achievement : Achievement)

}