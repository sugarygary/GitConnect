package com.sugarygary.gitconnect.data.remote.retrofit

import com.sugarygary.gitconnect.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GithubApiConfig {
    companion object {
        fun getApiService(): GithubApiService {
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val client =
                OkHttpClient.Builder().addInterceptor(loggingInterceptor).addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "token ${BuildConfig.TOKEN}").build()
                    chain.proceed(newRequest)
                }.build()
            val retrofit = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(client).build()
            return retrofit.create(GithubApiService::class.java)
        }
    }
}