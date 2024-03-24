package com.sugarygary.gitconnect.ui.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.sugarygary.gitconnect.data.repository.Result
import com.sugarygary.gitconnect.data.repository.UserRepository
import com.sugarygary.gitconnect.data.repository.model.UserModel
import kotlinx.coroutines.launch

class FollowViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _follow = MutableLiveData<Result<List<UserModel>>>()
    val follow: LiveData<Result<List<UserModel>>> = _follow
    fun fetchUserFollowing(username: String, withLoading: Boolean = true) {
        viewModelScope.launch {
            userRepository.fetchUserFollowing(username, withLoading).asFlow().collect {
                _follow.value = it
            }
        }
    }

    fun fetchUserFollowers(username: String, withLoading: Boolean = true) {
        viewModelScope.launch {
            userRepository.fetchUserFollowers(username, withLoading).asFlow().collect {
                _follow.value = it
            }
        }
    }

    fun favoriteUser(isFavorite: Boolean, user: UserModel) {
        viewModelScope.launch {
            if (isFavorite) {
                userRepository.removeFavoriteUser(user)
            } else {
                userRepository.addFavoriteUser(user)
            }
        }
    }
}