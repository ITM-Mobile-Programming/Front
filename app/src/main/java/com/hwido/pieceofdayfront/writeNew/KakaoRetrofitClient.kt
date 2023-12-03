package com.hwido.pieceofdayfront.writeNew

import android.util.Log
import com.hwido.pieceofdayfront.datamodel.KaKaoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//콜백 구현으로 클래스와 엑티비티 소통
class KakaoRetrofitClient {

//    private val okHttpClient by lazy {
//        val logging = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY // 상세 로그 출력 설정
//        }
//
//        OkHttpClient.Builder()
//            .readTimeout(100, TimeUnit.SECONDS)
//            .writeTimeout(100, TimeUnit.SECONDS)
//            .connectTimeout(100, TimeUnit.SECONDS)
//            .addInterceptor(logging) // 로깅 인터셉터 추가
//            .build()
//    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dapi.kakao.com/v2/local/geo/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val kakaoAPI = retrofit.create(WeatherLocationAPI::class.java)


    fun getAddressFromCoordinates(apikey:String, longitude: Double, latitude: Double,callback: KakaoResponseCallback) {
        Log.d("ITM", "카카오 위치 함수 입성")

        kakaoAPI.getAddress(apikey, longitude, latitude).enqueue(object :
            Callback<KaKaoResponse> {
            override fun onResponse(call: Call<KaKaoResponse>, response: Response<KaKaoResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("ITM", "카카오 api연결성공 ${responseBody?.documents?.get(0)?.address_name.toString()}")
//                    location = "군산"
//                    location =
                    val addressName = responseBody?.documents?.get(0)?.address_name.toString()
                    callback.onSuccessLocation(addressName)
                    // 여기에서 regionName과 dongName을 사용하세요.
//                    Log.d("ITM", "$location")

                }else{
                    Log.d("ITM", "${response}")
                    callback.onErrorLocation(Exception("Response not successful"))
                }
            }


            override fun onFailure(call: Call<KaKaoResponse>, t: Throwable) {
                // 오류 처리
                Log.d("ITM", "카카오 연결실패 ${t.message}")
                callback.onErrorLocation(t)
            }
        })
    }
}