package com.example.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubuser.DisplayUser
import com.example.githubuser.GithubResponse
import com.example.githubuser.config.ApiConfig
import com.example.githubuser.preferences.SettingPreferences
import retrofit2.Call
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _displayUser = MutableLiveData<ArrayList<DisplayUser>>()
    val displayUser: LiveData<ArrayList<DisplayUser>> = _displayUser

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error


    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        findUser("\"\"")

    }

    fun findUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUsername(query)
        client.enqueue(object : retrofit2.Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _displayUser.value = response.body()?.items
                } else {
                    Log.e(TAG, "\"onFailure: ${response.message()}\"")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }


}