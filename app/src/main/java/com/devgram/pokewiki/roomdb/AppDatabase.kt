package com.devgram.pokewiki.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.devgram.pokewiki.dao.AchievementDao
import com.devgram.pokewiki.dao.FavoritePokemonDao
import com.devgram.pokewiki.dao.PokemonTeamDao
import com.devgram.pokewiki.model.*
import androidx.room.Room




@Database(entities = [PokemonTeam::class, Achievement::class, FavoritePokemon::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    private var instance: AppDatabase? = null

    open fun getInstance(context: Context?): AppDatabase? {
        if (instance == null) instance =
            Room.databaseBuilder(context!!, AppDatabase::class.java, "app.db").build()
        return instance
    }

    abstract fun getAchivementDao(): AchievementDao?
    abstract fun FavoritePokemonDao(): FavoritePokemonDao?
    abstract fun PokemonTeamDao(): PokemonTeamDao?
}