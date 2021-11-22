package com.devgram.pokewiki.dao

import androidx.room.*
import com.devgram.pokewiki.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAll() : List<User>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getById(id : Int) : User

    @Query("SELECT * FROM users WHERE name = :name")
    fun getByName(name : String) : User?

    @Query("SELECT * FROM users WHERE email = :email")
    fun getByEmail(email : String) : User

    @Update
    fun update(user: User)

    @Insert
    fun insert(user : User)

    @Delete
    fun delete(user : User)

}