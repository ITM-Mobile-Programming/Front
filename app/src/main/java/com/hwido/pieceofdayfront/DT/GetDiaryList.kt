package com.hwido.pieceofdayfront.DT

import com.google.gson.annotations.SerializedName

data class getDiaryResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message")val message: String,
    @SerializedName("data")val data: List<DiaryEntry>
)

data class DiaryEntry(
    @SerializedName("diaryId") val diaryId : String,
    @SerializedName("title") val title: String,
    @SerializedName("context") val context: String,
    @SerializedName("location") val location: String,
    @SerializedName("weatherCode") val weatherCode: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String,
    @SerializedName("hashTagList") val hashTagList: List<HashTag>
    //MBTI가 있는지?
)

data class HashTag(
    @SerializedName("createdDate") val createdDate: Long,
    @SerializedName("updatedDate") val updatedDate: Long,
    @SerializedName("diaryToHashTagIde") val diaryToHashTagId: Int,
    @SerializedName("hashTag") val hashTag: String
)