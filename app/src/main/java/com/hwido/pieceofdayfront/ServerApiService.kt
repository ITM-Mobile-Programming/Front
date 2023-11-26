package com.hwido.pieceofdayfront


import com.hwido.pieceofdayfront.datamodel.BaseResponse
import com.hwido.pieceofdayfront.datamodel.ServerAccessTokenRequest
import com.hwido.pieceofdayfront.datamodel.SignUpRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ServerApiService {
    //반환은 ResponseData 형식으로
    // 인터페이스에서 와일드카드 쓰면 아노딘다
    @POST("member/oAuth")
    fun login(@Body request: ServerAccessTokenRequest): Call<BaseResponse>


    // email, password, introduce, nickname 다시요청
    @POST("member/oAuth/signUp")
    fun signUp(@Body request : SignUpRequest): Call<BaseResponse>

    @Multipart
    @POST("profile/upload/{id}")
    fun postProfileSignUp(@Path("id") memberId: String, @Part file: MultipartBody.Part) : Call<BaseResponse>

}