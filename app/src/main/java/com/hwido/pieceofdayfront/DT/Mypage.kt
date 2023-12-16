package com.hwido.pieceofdayfront.DT

import com.google.gson.annotations.SerializedName

data class myPageBaseData(
    @SerializedName("statusCode")  val statusCode: Int?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data:getMyData?
)
data class getMyData(
    @SerializedName("diaryCount")  val diaryCount: Int?,
    @SerializedName("nickName") val nickName: String?,
    @SerializedName("email") val email : String?,
    @SerializedName("code") val code: String?,
    @SerializedName("introduce") val introduce: String?,
)


