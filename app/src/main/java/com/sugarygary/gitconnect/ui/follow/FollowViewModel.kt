package com.sugarygary.gitconnect.ui.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sugarygary.gitconnect.data.model.User
import com.sugarygary.gitconnect.data.service.GithubApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _follow = MutableLiveData<List<User>>()
    val follow: LiveData<List<User>> = _follow

    private val _isLoading = MutableLiveData<Boolean>()
    private val _isError = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean> = _isLoading
    val isError: LiveData<Boolean> = _isError

    fun fetchUserFollowing(username: String) {
        if (username.isEmpty()) {
            _isError.value = true
            return
        }
        _isError.value = false
        _isLoading.value = true
        GithubApiConfig.getApiService().getUserFollowing(username)
            .enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _follow.value = response.body()
                    } else {
                        _isError.value = true
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    _isError.value = true
                    _isLoading.value = false
                }
            })
    }

    fun fetchUserFollowers(username: String) {
        if (username.isEmpty()) {
            _isError.value = true
            return
        }
        _isError.value = false
        _isLoading.value = true
        GithubApiConfig.getApiService().getUserFollowers(username)
            .enqueue(object : Callback<List<User>> {

                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _follow.value = response.body()
                    } else {
                        _isError.value = true
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    _isError.value = true
                    _isLoading.value = false
                }
            })
    }
}