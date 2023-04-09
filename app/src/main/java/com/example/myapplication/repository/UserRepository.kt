package com.example.myapplication.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.myapplication.database.Favorite
import com.example.myapplication.database.FavoriteDao
import com.example.myapplication.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mFavDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavDao = db.favDao()
    }

    fun getFavoriteUserByUsername(username: String): LiveData<Favorite> =
        mFavDao.getFavoriteUserByUsername(username)

    fun insert(user: Favorite) {
        executorService.execute { mFavDao.insert(user) }
    }

    fun delete(user: Favorite) {
        executorService.execute { mFavDao.delete(user) }
    }

    fun update(user: Favorite) {
        executorService.execute { mFavDao.update(user) }
    }

    fun getAllFavoriteUsers(): List<Favorite> = mFavDao.getAllFavoriteUsers()
}