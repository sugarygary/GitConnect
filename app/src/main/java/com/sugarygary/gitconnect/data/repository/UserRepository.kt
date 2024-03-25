package com.sugarygary.gitconnect.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.sugarygary.gitconnect.data.local.entity.FavoriteUser
import com.sugarygary.gitconnect.data.local.room.FavoriteUserDao
import com.sugarygary.gitconnect.data.remote.response.UserResponse
import com.sugarygary.gitconnect.data.remote.retrofit.GithubApiService
import com.sugarygary.gitconnect.data.repository.model.UserModel

class UserRepository private constructor(
    private val apiService: GithubApiService, private val favoriteUserDao: FavoriteUserDao
) {
    fun fetchFavoriteUsers(withLoading: Boolean = true): LiveData<Result<List<UserModel>>> =
        liveData {
            if (withLoading) emit(Result.Loading)
            try {
                val data = favoriteUserDao.getAll()
                if (data.isEmpty()) {
                    emit(Result.Empty)
                } else {
                    val result: List<UserModel> = data.map { favoriteUser ->
                        mapEntityToModel(favoriteUser)
                    }
                    emit(Result.Success(result))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun fetchUserProfile(username: String, withLoading: Boolean): LiveData<Result<UserModel>> =
        liveData {
            if (username.isEmpty()) {
                emit(Result.Error("Username cannot be empty"))
                return@liveData
            }
            if (withLoading) emit(Result.Loading)
            try {
                val response = apiService.getUserProfile(username)
                val isFavorite = favoriteUserDao.checkFavorite(response.login)
                emit(Result.Success(mapResponseToModel(response, isFavorite)))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun fetchUserFollowers(
        username: String, withLoading: Boolean = true
    ): LiveData<Result<List<UserModel>>> = liveData {
        if (username.isEmpty()) {
            emit(Result.Error("Username cannot be empty"))
            return@liveData
        }
        if (withLoading) emit(Result.Loading)
        try {
            val response = apiService.getUserFollowers(username)
            if (response.isEmpty()) {
                emit(Result.Empty)
            } else {
                val result: List<UserModel> = response.map { user ->
                    val isFavorite = favoriteUserDao.checkFavorite(user.login)
                    mapResponseToModel(user, isFavorite)
                }
                emit(Result.Success(result))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun fetchUserFollowing(
        username: String, withLoading: Boolean = true
    ): LiveData<Result<List<UserModel>>> = liveData {
        if (username.isEmpty()) {
            emit(Result.Error("Username cannot be empty"))
            return@liveData
        }
        if (withLoading) emit(Result.Loading)
        try {
            val response = apiService.getUserFollowing(username)
            if (response.isEmpty()) {
                emit(Result.Empty)
            } else {
                val result: List<UserModel> = response.map { user ->
                    val isFavorite = favoriteUserDao.checkFavorite(user.login)
                    mapResponseToModel(user, isFavorite)
                }
                emit(Result.Success(result))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun searchUsers(
        username: String, withLoading: Boolean = true
    ): LiveData<Result<List<UserModel>>> = liveData {
        if (withLoading) emit(Result.Loading)
        try {
            if (username.isEmpty()) {
                val response = apiService.getUsers()
                if (response.isEmpty()) {
                    emit(Result.Empty)
                } else {
                    val result: List<UserModel> = response.map { user ->
                        val isFavorite = favoriteUserDao.checkFavorite(user.login)
                        mapResponseToModel(user, isFavorite)
                    }
                    emit(Result.Success(result))
                }
            } else {
                val response = apiService.searchUsers(username)
                if (response.totalCount == 0) {
                    emit(Result.Empty)
                } else {
                    val result: List<UserModel> = response.items.map { user ->
                        val isFavorite = favoriteUserDao.checkFavorite(user.login)
                        mapResponseToModel(user, isFavorite)
                    }
                    emit(Result.Success(result))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun addFavoriteUser(user: UserModel) {
        favoriteUserDao.addFavorite(with(user) {
            FavoriteUser(login, id, followers, avatarUrl, htmlUrl, following, name)
        })
    }

    suspend fun removeFavoriteUser(user: UserModel) {
        favoriteUserDao.removeFavorite(user.login)
    }

    private fun mapResponseToModel(response: UserResponse, isFavorite: Boolean): UserModel {
        with(response) {
            return UserModel(
                login = login,
                avatarUrl = avatarUrl,
                htmlUrl = htmlUrl,
                followers = followers,
                following = following,
                id = id,
                name = name,
                isFavorite = isFavorite
            )
        }
    }

    private fun mapEntityToModel(response: FavoriteUser): UserModel {
        with(response) {
            return UserModel(
                login = login,
                avatarUrl = avatarUrl,
                htmlUrl = htmlUrl,
                followers = followers,
                following = following,
                id = id,
                name = name,
                isFavorite = true
            )
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: GithubApiService, favoriteUserDao: FavoriteUserDao
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(apiService, favoriteUserDao)
        }.also { instance = it }
    }
}