package com.hwido.pieceofdayfront.writeNew

import android.util.Log
import com.hwido.pieceofdayfront.datamodel.WeatherTotalResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WriteNewPageRetrofitClient {

        private val okHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // 상세 로그 출력 설정
        }

        OkHttpClient.Builder()
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(logging) // 로깅 인터셉터 추가
            .build()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val WeatherAPI = retrofit.create(WeatherLocationAPI::class.java)
    fun getWeather(apikey:String, baseDate : String, baseTime:String, latitude: Short, longitude: Short, callback: WeatherCallback) {
        Log.d("ITM", "Weather 함수 입성")

        WeatherAPI.getWeather(apikey,24,1, "JSON", baseDate, baseTime, latitude, longitude).enqueue(object :
            Callback<WeatherTotalResponse> {
            override fun onResponse(call: Call<WeatherTotalResponse>, response: Response<WeatherTotalResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("ITM", "weather api연결성공 ${responseBody?.response?.body?.items.toString()}")
                    //기계시간을 받아와서 넣어준다
                    //1123 하면 12시부터 나온다  30분 미만이면 그렇다면 한시간을 뺸 시간을 매개 변수로 넣어준다
                    //30분 이상이면 그냥 그대로 입력한다 => 한번 그냥 처리하자 1120 이면 10시 1130이면 11시로

                    //24개를 받고 현재 시간에서 시간만 떄고
                    //LGT, PTY, RN1, SKY
                    //0~5, 6~11, 12~17, 18~23
                    //그냥 첫번쨰꺼 긁어 오면 된다
                    //PTY - 6, SKY - 18


                    // 안드로이드에서 날짜랑 시간 가져오기
                    // api 타입설정, item에서 catagory = PTY, SKY 둘중하나이고 fcstTime이 baseTime에서 1이 추가 되었는지 체크후 가져온다

                    //가져오면  if pty가 0이고 1이 맑, 2가 흐림, 3. 구름 많음,, 1256이라면 비, 3,7이면 눈

                    //    category, FcstTime 가져와서 확인후 fcstValue를 체크한다
                    // 만약 0이면 sky -> 1 -> 맑음,2 -> 흐림 ,3 -> 구름 많음
                    // 만약 1,2,5,6 이면  -> 비

                    // 이외에 것들은 눈으로 한다
                    val pty = responseBody?.response?.body?.items?.item?.get(6)
                    val sky = responseBody?.response?.body?.items?.item?.get(18)
                    //2340 들어오면  0000 나온다  근데 나누면 2300 이여서 틀렸다고 나온다
                    //0010 들어오면  0000나온다  그러면 +100 한값이 들어가도록

                    val weatherCategory = if((pty?.category?.equals("PTY") == true )&& (sky?.category.equals("SKY"))){
                        when(pty.fcstValue){
                            "0"-> {
                                when(sky?.fcstValue){
                                    "1"-> { "Sunny"}
                                    "2" -> {"LittleCloud"}
                                    else->{
                                        "Cloud"
                                    }

                                }
                            }
                            "1", "2","5","6" -> {"Rain"}
                            else -> {
                                "Snow"
                            }
                        }
                    }else{
                         "해당하는 시간, 정보가 아닙니다"
                    }

//                    1) Sunny 2) LittleCloud 3)Cloud 4)Rain 5) Snow
                    Log.d("ITM", "$weatherCategory")
                    callback.onSuccessWeather(weatherCategory)

                }else{
                    Log.d("ITM", "${response}")
                    callback.onErrorWeather(Exception("Response not successful"))
                }
            }

            override fun onFailure(call: Call<WeatherTotalResponse>, t: Throwable) {
                // 오류 처리
                Log.d("ITM", "weather 연결실패 ${t.message}")
                callback.onErrorWeather(t)
            }
        })
    }


}