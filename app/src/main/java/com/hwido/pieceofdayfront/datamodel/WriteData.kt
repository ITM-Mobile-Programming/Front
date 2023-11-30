package com.hwido.pieceofdayfront.datamodel

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class WriteDataRequest(
    @SerializedName("title") val title: String,
    @SerializedName("context") val context: String,
    @SerializedName("location") val location: String?,
    @SerializedName("weatherCode") val weatherCode: String?
)

@Serializable
data class DiarySendSuccessResponse(
    @SerializedName("diaryId") val diaryId: Long?,
    @SerializedName("hashTags") val hashTags: List<String?>?,
    @SerializedName("imageUrl") val imageUrl: String?,
)