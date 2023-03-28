package com.example.githubuser.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "fav_user")
@Parcelize
data class FavoriteUser(
    @PrimaryKey
    val id: Int,
    val username: String,
    var avatarUrl: String? = null

) : Parcelable
