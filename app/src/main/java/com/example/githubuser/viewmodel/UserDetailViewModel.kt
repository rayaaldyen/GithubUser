package com.example.githubuser.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubuser.DetailUser
import com.example.githubuser.config.ApiConfig
import com.example.githubuser.data.FavoriteRepository
import com.example.githubuser.data.entity.FavoriteUser
import com.example.githubuser.data.room.Dao
import com.example.githubuser.data.room.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application) : AndroidViewModel(application) {
    val users = MutableLiveData<DetailUser>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val repository = FavoriteRepository(UserDatabase.getInstance(application).dao())


    companion object {
        private const val TAG = "UserDetailViewModel"
    }

    fun setDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.apply {
            enqueue(object : Callback<DetailUser> {
                override fun onResponse(
                    call: Call<DetailUser>,
                    response: Response<DetailUser>
                ) {
                    _isLoading.value = false
                    _error.value = false
                    if (response.isSuccessful) {
                        users.value = response.body()
                    } else {
                        Log.e(TAG, "\"onFailure: ${response.message()}\"")
                    }

                }

                override fun onFailure(call: Call<DetailUser>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = true
                    Log.e(TAG, "onFailure: ${t.message}")
                }

            })
        }
    }

    fun getDetailUser(): LiveData<DetailUser> {
        return users
    }

    //    fun insertFavorite(id: Int, username: String, avatarUrl: String){
//        CoroutineScope(Dispatchers.IO).launch {
//            val favUser = FavoriteUser(
//                id,
//                username,
//                avatarUrl
//            )
//            dao?.insertFav(favUser)
//        }
//    }
    fun insertFavorite(id: Int, username: String, avatarUrl: String) = viewModelScope.launch {
        val favUser = FavoriteUser(
            id,
            username,
            avatarUrl
        )
        repository.insertFav(favUser)
    }

    fun checkFav(id: Int):Int = repository.checkFav(id)


    fun deleteFavorite(id: Int) = viewModelScope.launch {
        repository.deleteFavorite(id)
    }
}
