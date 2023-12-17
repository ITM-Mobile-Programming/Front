package com.hwido.pieceofdayfront.ServerAPI



import com.hwido.pieceofdayfront.DT.BaseResponse
import com.hwido.pieceofdayfront.DT.BaseResponse2
import com.hwido.pieceofdayfront.DT.BasicResponse
import com.hwido.pieceofdayfront.DT.CheckResponse
import com.hwido.pieceofdayfront.DT.FriendCode
import com.hwido.pieceofdayfront.DT.ListResponse
import com.hwido.pieceofdayfront.DT.OneDayCheck
import com.hwido.pieceofdayfront.DT.RelayDataRequest
import com.hwido.pieceofdayfront.DT.SendMBTI
import com.hwido.pieceofdayfront.DT.ServerAccessTokenRequest
import com.hwido.pieceofdayfront.DT.SignUpRequest
import com.hwido.pieceofdayfront.DT.WriteDataRequest
import com.hwido.pieceofdayfront.DT.diaryID
import com.hwido.pieceofdayfront.DT.getDiaryResponse
import com.hwido.pieceofdayfront.DT.myPageBaseData
import com.hwido.pieceofdayfront.DT.reloadDairy
import com.hwido.pieceofdayfront.DT.DateDiary
import com.hwido.pieceofdayfront.DT.FriendResponse


import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


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


    @POST("diary/image")
    fun reloadMemberDairy(@Header("Authorization") authToken: String?, @Body  request: reloadDairy): Call<BaseResponse2>


    @POST("diary/update/mbti")
    fun sendDiaryWithMBTI(@Header("Authorization") authToken: String?, @Body  request: SendMBTI):Call<BasicResponse>


    //다이어리 목록
    @GET("diary/")
    fun getDiaryList(@Header("Authorization") authToken: String?) : Call<getDiaryResponse>


    @GET("diary/verification")
    fun checkVerification(@Header("Authorization") authToken: String?) : Call<OneDayCheck>


    //마이페이지
    @GET("member/")
    fun getMyPage(@Header("Authorization") authToken: String?) : Call<myPageBaseData>

    @POST("friend/check")
    fun checkIfFriend(@Header("Authorization") authToken: String?, @Body request : FriendCode) : Call<CheckResponse>

    @POST("diary/delete")
    fun deleteDiary(@Header("Authorization") authToken: String?, @Body request : diaryID) : Call<BasicResponse>


    @GET("share/")
    fun getSharedDiary(@Header("Authorization") authToken: String?) : Call<getDiaryResponse>
//    @POST("diary/delete")
//    fun deleteDiary(@Header("Authorization") authToken: String?, @Body request : FriendCode) : Call<BasicResponse>

    @GET("profile/read/{id}")
    fun getImagePage(@Path("id") memberId: Int) : Call<ResponseBody>


    @POST("friend/request")
    fun AddFriend(@Header("Authorization") authToken: String?, @Body request : FriendCode) : Call<BasicResponse>



    @POST("share/write")
    fun relayWrite(@Header("Authorization") authToken: String?, @Body request : RelayDataRequest) : Call<BasicResponse>



    //중복작성
//    @GET("/diary/verification")
//    fun checkDouble(@Header("Authorization") authToken: String?) :
    //다이어리 수정
    //다이어리 삭제

    // 친구 목록
    @GET("friend/")
    fun getFriendList(@Header("Authorization") authToken: String?) : Call<FriendResponse>

    // 날짜별 다이어리
    @POST("diary/dateSearch")
    fun getDateDiary(@Header("Authorization") authToken: String?, @Body writtenDate : DateDiary) : Call<ListResponse>

}

