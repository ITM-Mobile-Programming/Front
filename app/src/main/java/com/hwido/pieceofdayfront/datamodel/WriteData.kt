package com.hwido.pieceofdayfront.datamodel

import com.google.gson.annotations.SerializedName

data class WriteDataRequest(
    @SerializedName("title") val title: String,
    @SerializedName("context") val context: String,
    @SerializedName("location") val location: String?,
    @SerializedName("weatherCode") val weatherCode: String?
)

data class DiarySendSuccessResponse(
    @SerializedName("diaryId") val diaryId: Long?,
    @SerializedName("hashTags") val hashTags: List<String>?,
    @SerializedName("imageUrl") val imageUrl: String?,
)

data class ListResponse(
    @SerializedName("statusCode")  val statusCode: Int?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: DiaryListLoad?

)
data class DiaryListLoad(
    @SerializedName("diaryId") val diaryId: Int,
    @SerializedName("title") val title:String?,
    @SerializedName("context") val context:String?,
    @SerializedName("location") val location: String?,
    @SerializedName("weatherCode") val weatherCode:String?,
    @SerializedName("thumbnailUrl") val thumbnailUrl:String?,
    //@SerializedName("hashTagList") val hashTags: List<String>
)


data class KaKaoResponse(
    @SerializedName("meta") val meta : Meta?,
    @SerializedName("documents") val documents :List<KaKaoAddress>?
)

data class Meta(
    @SerializedName("total_count") val totalCount: Int,
)


data class KaKaoAddress(
    @SerializedName("region_type") val region : String?,
    @SerializedName("address_name") val address_name : String?,
    @SerializedName("region_1depth_name") val region_1depth:String?,
    @SerializedName("region_2depth_name") val region_2depth:String?,
    @SerializedName("region_3depth_name") val region_3depth:String?,
    @SerializedName("region_4depth_name") val region_4depth:String?,
    @SerializedName("code") val code :String?,
    @SerializedName("x") val log :String?,
    @SerializedName("y") val lat :String?,
)

data class reloadDairy(
    @SerializedName("diaryId") val diaryId : Int
)

data class SendMBTI(
    @SerializedName("diaryId") val diaryId : Int,
    @SerializedName("mbtiCode") val mbtiCode : String
)

