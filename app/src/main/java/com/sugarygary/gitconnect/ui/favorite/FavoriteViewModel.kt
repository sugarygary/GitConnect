package com.sugarygary.gitconnect.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.sugarygary.gitconnect.data.repository.Result
import com.sugarygary.gitconnect.data.repository.UserRepository
import com.sugarygary.gitconnect.data.repository.model.UserModel
import kotlinx.coroutines.launch

class FavoriteViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _favorite = MutableLiveData<Result<List<UserModel>>>()
    val favorite: LiveData<Result<List<UserModel>>> = _favorite

    fun fetchFavoriteUsers(withLoading: Boolean = true) {
        viewModelScope.launch {
            userRepository.fetchFavoriteUsers(withLoading).asFlow().collect {
                _favorite.value = it
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