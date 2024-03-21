package com.sugarygary.gitconnect.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sugarygary.gitconnect.data.model.User
import com.sugarygary.gitconnect.data.response.SearchUserResponse
import com.sugarygary.gitconnect.data.service.GithubApiConfig.Companion.getApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    private val _listUsers = MutableLiveData<List<User>>()
    val listUsers: LiveData<List<User>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    private val _isError = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    val isError: LiveData<Boolean> = _isError

    fun fetchUsers(q: String = "") {
        _isError.value = false
        _isLoading.value = true
        if (q.isNotEmpty()) {
            getApiService().searchUsers(q).enqueue(object : Callback<SearchUserResponse> {
                override fun onResponse(
                    call: Call<SearchUserResponse>,
                    response: Response<SearchUserResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _listUsers.value = response.body()?.items
                    } else {
                        _isError.value = true
                    }
                }

                override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = true
                }

            })
        } else {
            getApiService().getUsers().enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _listUsers.value = response.body()
                    } else {
                        _isError.value = true
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = true
                }

            })
        }
    }
}