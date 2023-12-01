package com.hwido.pieceofdayfront

import com.hwido.pieceofdayfront.datamodel.BaseResponse2
import com.hwido.pieceofdayfront.datamodel.ListResponse
import com.hwido.pieceofdayfront.datamodel.WriteDataRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface SeverMemberRequestAPI {
    @GET("diary/")
    fun getMemeberDiary(@Header("Authorization") authToken: String?): Call<ListResponse> // 수정 해야됨


    //Call과 CallBack 차이 정리 필요
    @POST("diary/write")
    fun postMemberDairy(@Header("Authorization") authToken: String?, @Body requestData: WriteDataRequest): Call<BaseResponse2>

}