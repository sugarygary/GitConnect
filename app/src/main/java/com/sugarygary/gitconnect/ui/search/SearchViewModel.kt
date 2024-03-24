package com.sugarygary.gitconnect.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.sugarygary.gitconnect.data.repository.Result
import com.sugarygary.gitconnect.data.repository.UserRepository
import com.sugarygary.gitconnect.data.repository.model.UserModel
import kotlinx.coroutines.launch

class SearchViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _listUsers = MutableLiveData<Result<List<UserModel>>>()
    val listUsers: LiveData<Result<List<UserModel>>> = _listUsers
    fun searchUsers(username: String, withLoading: Boolean = true) {
        viewModelScope.launch {
            userRepository.searchUsers(username, withLoading).asFlow()
                .collect { _listUsers.postValue(it) }
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