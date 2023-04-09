package com.example.myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: Favorite)

    @Update
    fun update(user: Favorite)

    @Delete
    fun delete(user: Favorite)

    @Query("SELECT * FROM favorite WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<Favorite>

    @Query("SELECT * FROM favorite")
    fun getAllFavoriteUsers(): List<Favorite>
}