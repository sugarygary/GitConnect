package com.sugarygary.gitconnect.data.remote.retrofit

import com.sugarygary.gitconnect.data.remote.response.SearchUserResponse
import com.sugarygary.gitconnect.data.remote.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("search/users")
    suspend fun searchUsers(@Query("q") q: String): SearchUserResponse

    @GET("users")
    suspend fun getUsers(): List<UserResponse>

    @GET("users/{username}")
    suspend fun getUserProfile(@Path("username") username: String): UserResponse

    @GET("users/{username}/followers")
    suspend fun getUserFollowers(@Path("username") username: String): List<UserResponse>

    @GET("users/{username}/following")
    suspend fun getUserFollowing(@Path("username") username: String): List<UserResponse>
}