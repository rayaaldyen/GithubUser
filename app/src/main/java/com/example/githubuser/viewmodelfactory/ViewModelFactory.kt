package com.example.githubuser.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.preferences.SettingPreferences
import com.example.githubuser.viewmodel.MainViewModel
import com.example.githubuser.viewmodel.SettingViewModel

class ViewModelFactory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }

        throw IllegalAccessException("Unknown ViewModel class: " + modelClass.name)
    }
}