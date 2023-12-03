package com.hwido.pieceofdayfront.datamodel

import com.google.gson.annotations.SerializedName

// 이중도 가능 합니다
data class WeatherTotalResponse(
    @SerializedName("response") val response: WeatherSecondResponse
){
    data class WeatherSecondResponse(
        @SerializedName("header") val header:WeatherResponseHeader,
        @SerializedName("body") val body: WeatherResponseBody
    )

    data class WeatherResponseHeader(
        @SerializedName("resultCode") val resultCode: String,
        @SerializedName("resultMsg") val resultMsg: String
    )


    data class WeatherResponseBody(
        @SerializedName("dataType") val dataType : String,
        @SerializedName("items") val items : WeatherItems,
        @SerializedName("numOfRows") val numberRow : Int,
        @SerializedName("pageNo") val pageNo : Int,
        @SerializedName("totalCount") val totalCount: Int,
    )



    data class WeatherItems(
        val item: List<WeatherItem>
    )


    data class WeatherItem(
        @SerializedName("baseDate") val baseDate: String,
        @SerializedName("baseTime") val baseTime: String,
        @SerializedName("category") val category: String,
        @SerializedName("fcstDate") val fcstDate: String,
        @SerializedName("fcstTime") val fcstTime :String,
        @SerializedName("fcstValue") val fcstValue : String,
        @SerializedName("nx") val nx :Int,
        @SerializedName("ny") val ny :Int
    )

}

