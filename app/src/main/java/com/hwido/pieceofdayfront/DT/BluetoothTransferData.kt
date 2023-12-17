package com.hwido.pieceofdayfront.DT

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WriteDataRequestTransfer(
    @SerializedName("diaryId") val diaryId: Int,
    @SerializedName("code") val code: String,
    @SerializedName("title") val title: String,
    @SerializedName("context") val context: String,
    @SerializedName("location") val location: String?,
    @SerializedName("weatherCode") val weatherCode: String?,

): Serializable

data class FriendCode(
    @SerializedName("code") val code: String?
)


