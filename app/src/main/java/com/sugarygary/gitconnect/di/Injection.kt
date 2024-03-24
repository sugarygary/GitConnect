package com.sugarygary.gitconnect.di

import android.content.Context
import com.sugarygary.gitconnect.data.local.room.FavoriteUserDatabase
import com.sugarygary.gitconnect.data.remote.retrofit.GithubApiConfig
import com.sugarygary.gitconnect.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = GithubApiConfig.getApiService()
        val database = FavoriteUserDatabase.getInstance(context)
        val dao = database.favoriteUserDao()
        return UserRepository.getInstance(apiService, dao)
    }
}