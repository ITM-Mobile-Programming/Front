package com.hwido.pieceofdayfront.writeNew

import com.hwido.pieceofdayfront.datamodel.KaKaoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoAPI {
    @GET("coord2regioncode.json")
    fun getAddress(
        @Header("Authorization")  apiKey: String,
        @Query("x") longitude: Double,
        @Query("y") latitude: Double
    ): Call<KaKaoResponse>
}