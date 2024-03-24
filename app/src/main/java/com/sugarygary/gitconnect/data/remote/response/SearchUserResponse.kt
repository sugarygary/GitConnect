package com.sugarygary.gitconnect.data.remote.response

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
    @field:SerializedName("total_count") val totalCount: Int,
    @field:SerializedName("items") val items: List<UserResponse>
)

