package com.example.githubuser.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.githubuser.data.entity.FavoriteUser

@Dao
interface Dao {

    @Insert
    suspend fun insertFav(favoriteUser: FavoriteUser)


    @Query("DELETE FROM fav_user WHERE fav_user.id = :id")
    suspend fun deleteFavorite(id: Int) : Int

    @Query("SELECT EXISTS (SELECT * FROM fav_user WHERE fav_user.id = :id)")
    fun checkFavorite(id: Int): Int

    @Query("SELECT * FROM fav_user")
    fun getFavoriteUser(): LiveData<List<FavoriteUser>>
}