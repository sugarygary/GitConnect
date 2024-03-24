package com.sugarygary.gitconnect.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_users")
data class FavoriteUser(
    @field:PrimaryKey(autoGenerate = false) @field:ColumnInfo(name = "login") val login: String,
    @field:ColumnInfo(name = "id") val id: Int,
    @field:ColumnInfo(name = "followers") val followers: Int,
    @field:ColumnInfo(name = "avatar_url") val avatarUrl: String? = null,
    @field:ColumnInfo(name = "html_url") val htmlUrl: String? = null,
    @field:ColumnInfo(name = "following") val following: Int,
    @field:ColumnInfo(name = "name") val name: String? = null,
)