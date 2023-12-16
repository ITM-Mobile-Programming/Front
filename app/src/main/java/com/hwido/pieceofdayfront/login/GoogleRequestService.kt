package com.hwido.pieceofdayfront.login

import com.hwido.pieceofdayfront.DT.GoogleApiAccessTokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
//비동기 작업이여서 suspend 사용합니다
interface GoogleRequestService {
    //데이터 클래스 정의 필요
    @FormUrlEncoded
    @POST("token")
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String = "authorization_code",  //이거는 자동 생성이여
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("code") code: String
    ): GoogleApiAccessTokenResponse

}