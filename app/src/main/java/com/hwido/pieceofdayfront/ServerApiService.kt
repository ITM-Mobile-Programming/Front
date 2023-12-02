package com.hwido.pieceofdayfront


import com.hwido.pieceofdayfront.datamodel.BaseResponse
import com.hwido.pieceofdayfront.datamodel.BaseResponse2
import com.hwido.pieceofdayfront.datamodel.ListResponse
import com.hwido.pieceofdayfront.datamodel.ServerAccessTokenRequest
import com.hwido.pieceofdayfront.datamodel.SignUpRequest
import com.hwido.pieceofdayfront.datamodel.WriteDataRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
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


    @GET("diary/")
    fun getMemeberDiary(@Header("Authorization") authToken: String?): Call<ListResponse> // 수정 해야됨


    //Call과 CallBack 차이 정리 필요
    @POST("diary/write")
    fun postMemberDairy(@Header("Authorization") authToken: String?, @Body requestData: WriteDataRequest): Call<BaseResponse2>


}