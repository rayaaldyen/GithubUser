package com.example.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.DisplayUser
import com.example.githubuser.config.ApiConfig
import retrofit2.Call
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val following = MutableLiveData<ArrayList<DisplayUser>?>(null)

    companion object {
        private const val TAG = "FollowingViewModel"
    }

    fun setFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : retrofit2.Callback<ArrayList<DisplayUser>> {
            override fun onResponse(
                call: Call<ArrayList<DisplayUser>>,
                response: Response<ArrayList<DisplayUser>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    following.postValue(response.body())
                } else {
                    Log.e(TAG, "\"onFailure: ${response.message()}\"")

                }
            }

            override fun onFailure(call: Call<ArrayList<DisplayUser>>, t: Throwable) {
                _isLoading.value = false
                following.value = arrayListOf()
                Log.e(TAG, "onFailure: ${t.message}")
            }

        }
        )

    }

    fun getFollowing(): LiveData<ArrayList<DisplayUser>?> {
        return following
    }
}