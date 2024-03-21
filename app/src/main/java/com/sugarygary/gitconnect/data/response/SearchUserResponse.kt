package com.sugarygary.gitconnect.data.response

import com.google.gson.annotations.SerializedName
import com.sugarygary.gitconnect.data.model.User

data class SearchUserResponse(
    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @field:SerializedName("items")
    val items: List<User>
)

