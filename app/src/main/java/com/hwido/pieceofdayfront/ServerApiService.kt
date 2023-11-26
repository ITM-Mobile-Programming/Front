package com.hwido.pieceofdayfront


import com.hwido.pieceofdayfront.datamodel.BaseResponse
import com.hwido.pieceofdayfront.datamodel.ServerAccessTokenRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServerApiService {
    //반환은 ResponseData 형식으로
    // 인터페이스에서 와일드카드 쓰면 아노딘다
    @POST("member/oAuth")
    fun login(@Body request: ServerAccessTokenRequest): Call<BaseResponse>
}