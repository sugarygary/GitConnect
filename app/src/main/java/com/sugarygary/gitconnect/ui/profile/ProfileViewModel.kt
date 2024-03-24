package com.sugarygary.gitconnect.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.sugarygary.gitconnect.data.repository.Result
import com.sugarygary.gitconnect.data.repository.UserRepository
import com.sugarygary.gitconnect.data.repository.model.UserModel
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<Result<UserModel>>()
    val user: LiveData<Result<UserModel>> = _user

    fun fetchUserProfile(username: String, withLoading: Boolean = true) {
        viewModelScope.launch {
            userRepository.fetchUserProfile(username, withLoading).asFlow().collect {
                _user.value = it
            }
        }
    }
}