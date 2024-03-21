package com.sugarygary.gitconnect.data.service

import com.sugarygary.gitconnect.data.model.User
import com.sugarygary.gitconnect.data.response.SearchUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("search/users")
    fun searchUsers(@Query("q") q: String): Call<SearchUserResponse>

    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("users/{username}")
    fun getUserProfile(@Path("username") username: String): Call<User>

    @GET("users/{username}/followers")
    fun getUserFollowers(@Path("username") username: String): Call<List<User>>

    @GET("users/{username}/following")
    fun getUserFollowing(@Path("username") username: String): Call<List<User>>
}