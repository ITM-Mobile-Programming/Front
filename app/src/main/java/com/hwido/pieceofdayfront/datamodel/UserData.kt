package com.hwido.pieceofdayfront.datamodel

import com.google.gson.annotations.SerializedName

//Field => requestform
//SerializedName => responseform


//구글 api토큰 받기
data class GoogleApiAccessTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Long,
    @SerializedName("scope") val scope: String,
    @SerializedName("id_token") val idToken: String
)

//서버로 엑세스 토큰 연결
data class ServerAccessTokenRequest(
    @SerializedName("access_token") val accessToken: String, //"access_token":"dd"이런 형태로 간다
//    @Field("access_token")
//    val accessToken2: String //"access_token":"dd"이런 형태로 간다
)
data class SignUpRequest(
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password : String?,
    @SerializedName("introduce") val introduce : String?,
    @SerializedName("nickName") val nickName : String?
)

//첫번쨰 json,, http 폼으로 받아온다 T는 2가지 상황을 대처할 수 있도록 제너릭으로 설정
//Any => object 모든객체 가능
data class BaseResponse(
    @SerializedName("statusCode")  val status: Int?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: Any? // 이 부분이 문제인데...
)

// 회원가입 핑요
data class SignupNeededResponse(
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?
)

//기존회원
data class LoginSuccessResponse(
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("key") val key: String?
)


data class SignUpResponse(
    @SerializedName("memberId") val memberId : String?
)







