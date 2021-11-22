package com.devgram.pokewiki.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.devgram.pokewiki.model.*
import androidx.room.Room
import androidx.room.TypeConverters
import com.devgram.pokewiki.dao.*


@Database(entities = [PokemonTeam::class, Achievement::class, Pokemon::class, PokemonFavorite::class, User::class], version = 1)
@TypeConverters(PokemonConverter::class, MoveConverter::class, StatConverter::class, TypePokemonConverter::class, SpritesConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAchievementDao() : AchievementDao
    abstract fun getFavoritePokemonDao() : FavoritePokemonDao
    abstract fun getPokemonTeamDao() : PokemonTeamDao
    abstract fun getPokemonDao() : PokemonDao
    abstract fun getUserDao() : UserDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context.applicationContext,AppDatabase::class.java, "pokewiki.db")
                            .build()
                }
            }
            return INSTANCE!!
        }

    }
}
