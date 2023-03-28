package com.example.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githubuser.data.FavoriteRepository
import com.example.githubuser.data.entity.FavoriteUser
import com.example.githubuser.data.room.Dao
import com.example.githubuser.data.room.UserDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
//    private var dao: Dao?
//    private var dbUser: UserDatabase?
    private val repository = FavoriteRepository(UserDatabase.getInstance(application).dao())

//    init {
//        dbUser = UserDatabase.getInstance(application)
//        dao = dbUser?.dao()
//    }

    val getFavoriteUser: LiveData<List<FavoriteUser>> = repository.getFavoriteUser
}