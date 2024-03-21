package com.sugarygary.gitconnect.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sugarygary.gitconnect.data.model.User
import com.sugarygary.gitconnect.data.service.GithubApiConfig.Companion.getApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    private val _isError = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    val isError: LiveData<Boolean> = _isError

    fun fetchUserData(username: String) {
        if (username.isEmpty()) {
            _isError.value = true
            return
        }
        _isError.value = false
        _isLoading.value = true
        getApiService().getUserProfile(username).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
            }
        })
    }
}