package com.sugarygary.gitconnect.ui.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sugarygary.gitconnect.data.repository.UserRepository
import com.sugarygary.gitconnect.di.Injection
import com.sugarygary.gitconnect.ui.favorite.FavoriteViewModel
import com.sugarygary.gitconnect.ui.follow.FollowViewModel
import com.sugarygary.gitconnect.ui.profile.ProfileViewModel
import com.sugarygary.gitconnect.ui.search.SearchViewModel

class ViewModelFactory private constructor(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            FollowViewModel::class.java -> FollowViewModel(userRepository) as T
            SearchViewModel::class.java -> SearchViewModel(userRepository) as T
            ProfileViewModel::class.java -> ProfileViewModel(userRepository) as T
            FavoriteViewModel::class.java -> FavoriteViewModel(userRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }

    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}