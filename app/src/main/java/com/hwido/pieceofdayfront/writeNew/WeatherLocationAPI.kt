package com.hwido.pieceofdayfront.writeNew

import com.hwido.pieceofdayfront.DT.KaKaoResponse
import com.hwido.pieceofdayfront.DT.WeatherTotalResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherLocationAPI {
    @GET("coord2regioncode.json")
    fun getAddress(
        @Header("Authorization")  apiKey: String,
        @Query("x") longitude: Double,
        @Query("y") latitude: Double
    ): Call<KaKaoResponse>

    @GET("getUltraSrtFcst")
    fun getWeather(
        @Query("serviceKey") serviceKey: String,
        @Query("numOfRows") numOfRows: Int, // 타입을 Int로 변경
        @Query("pageNo") pageNo: Int, // 타입을 Int로 변경
        @Query("dataType") dataType: String, // dataType 추가
        @Query("base_date") baseDate: String, // 타입을 Int로 변경
        @Query("base_time") baseTime: String, // 타입을 Int로 변경
        @Query("nx") latitude: Short, // 타입을 Double로 변경
        @Query("ny") longitude: Short // 타입을 Double로 변경
    ): Call<WeatherTotalResponse>


}