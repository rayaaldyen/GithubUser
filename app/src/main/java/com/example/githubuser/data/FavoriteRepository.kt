package com.example.githubuser.data

import androidx.lifecycle.LiveData
import com.example.githubuser.data.entity.FavoriteUser
import com.example.githubuser.data.room.Dao

class FavoriteRepository(private val dao: Dao) {
    val getFavoriteUser: LiveData<List<FavoriteUser>> = dao.getFavoriteUser()

    suspend fun insertFav(favoriteUser: FavoriteUser) {
        dao.insertFav(favoriteUser)
    }
    fun checkFav(id: Int): Int = dao.checkFavorite(id)


    suspend fun deleteFavorite(id: Int) {
        dao.deleteFavorite(id)
    }
}