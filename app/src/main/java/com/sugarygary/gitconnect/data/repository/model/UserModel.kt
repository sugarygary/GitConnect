package com.sugarygary.gitconnect.data.repository.model

data class UserModel(
    val login: String,

    val id: Int,

    val followers: Int,

    val avatarUrl: String? = null,

    val htmlUrl: String? = null,

    val following: Int,

    val name: String? = null,

    val isFavorite: Boolean = false
)