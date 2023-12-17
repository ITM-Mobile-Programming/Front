package com.hwido.pieceofdayfront.ServerAPI;

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.hwido.pieceofdayfront.datamodel.BaseResponse2
import com.hwido.pieceofdayfront.datamodel.BasicResponse
import com.hwido.pieceofdayfront.datamodel.DateDiary
import com.hwido.pieceofdayfront.datamodel.DiaryEntry
import com.hwido.pieceofdayfront.datamodel.DiaryListLoad
import com.hwido.pieceofdayfront.datamodel.FriendData
import com.hwido.pieceofdayfront.datamodel.FriendResponse
import com.hwido.pieceofdayfront.datamodel.ListResponse
import com.hwido.pieceofdayfront.datamodel.SendMBTI
import com.hwido.pieceofdayfront.datamodel.WriteDataRequest
import com.hwido.pieceofdayfront.datamodel.getDiaryResponse
import com.hwido.pieceofdayfront.datamodel.reloadDairy
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class SpringServerAPI {
    // 그림하는지 아닌지 아닌지 구분하고 그림이면 이부분으로 한다
    val okHttpClient = OkHttpClient.Builder()
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
            .build()

    //레트로핏 설정
    private val retrofit = Retrofit.Builder()
            .baseUrl("http://3.20.166.136/") // 서버의 기본 URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // JSON 변환을 위한 GsonConverterFactory 사용//이건 갈떄
            .build()

    val writeRequest = retrofit.create(ServerApiService ::class.java)

    //콜백으로 받아온다 그후 intent 한다
    fun sendDiaryToGetImage(firstRequest : WriteDataRequest, accessToken :String, callback: ServerResponseCallback) {


        
        writeRequest.postMemberDairy("Bearer $accessToken", firstRequest).enqueue(object :
            Callback<BaseResponse2> {
            override fun onResponse(call: Call<BaseResponse2>, response: Response<BaseResponse2>) {
                if (response.isSuccessful) {
                    val baseResponse  = response.body()

//                    Log.d("ITM","${baseResponse?.data?.hashTags.toString()}")
//                    Log.d("ITM","${baseResponse?.data.toString().substring(20,30)}")

                    when (baseResponse?.status) {
                        200 -> {
                            try {
                                val longToInt =baseResponse.data?.diaryId?.toInt()!!
                                val hashTag = baseResponse.data?.hashTags.toString()
                                val url = baseResponse.data?.imageUrl!!

                                callback.onSuccessSpring(longToInt, url, hashTag)

                            }catch (e: JsonSyntaxException) {
                                Log.e("ITM", "JSON 파싱 오류: ", e)
                            }

                        }
                    }
                } else
                {Log.d("ITM", "${response}")}
            }
            // onFailure 구현...
            override fun onFailure(call: Call<BaseResponse2>, t: Throwable) {
                Log.d("ITM", "뺵엔드 연결실패 ${t.message}")
                callback.onErrorSpring(t)

            }
        })
    }



    fun reloadDiaryToGetImage(firstRequest : reloadDairy, accessToken :String, callback: ServerResponseCallback) {
        writeRequest.reloadMemberDairy("Bearer $accessToken",firstRequest).enqueue(object :
            Callback<BaseResponse2> {
            override fun onResponse(call: Call<BaseResponse2>, response: Response<BaseResponse2>) {
                if (response.isSuccessful) {
                    val baseResponse  = response.body()

                    when (baseResponse?.status) {
                        200 -> {
                            try {
                                val url = baseResponse.data?.imageUrl!!

                                callback.onSuccessSpring(url)

                            }catch (e: JsonSyntaxException) {
                                Log.e("ITM", "JSON 파싱 오류: ", e)
                            }

                        }
                    }
                } else
                {Log.d("ITM", "${response}")}
            }
            // onFailure 구현...
            override fun onFailure(call: Call<BaseResponse2>, t: Throwable) {
                Log.d("ITM", "뺵엔드 연결실패 ${t.message}")
                callback.onErrorSpring(t)

            }
        })
    }


    fun sendDiaryWithMBTI(firstRequest : SendMBTI, accessToken :String) {
        writeRequest.sendDiaryWithMBTI("Bearer $accessToken", firstRequest).enqueue(object :
            Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    val baseResponse  = response.body()

                    when (baseResponse?.status) {
                        200 -> {
                            try {
//                                val url = baseResponse.data?.imageUrl!!
                                Log.d("ITM","최종 작성 성공")


                            }catch (e: JsonSyntaxException) {
                                Log.e("ITM", "JSON 파싱 오류: ", e)
                            }

                        }
                    }
                } else
                {Log.d("ITM", "${response}")}
            }
            // onFailure 구현...
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d("ITM", "뺵엔드 연결실패 ${t.message}")
            }
        })
    }


    fun getDiaryList(accessToken :String, callback: ServerResponseCallback) {
        Log.d("ITM", "리스트 함수 들어옴1 ")
        writeRequest.getDiaryList("Bearer $accessToken").enqueue(object :
            Callback<getDiaryResponse> {
            override fun onResponse(call: Call<getDiaryResponse>, response: Response<getDiaryResponse>) {
                Log.d("ITM", "리스트 함수 들어옴2 ")

                if (response.isSuccessful) {
                    Log.d("ITM", "리스트 함수 들어옴3 ")
                    val baseResponse  = response.body()


                    when (baseResponse?.statusCode) {
                        200 -> {
//                            Log.d("ITM","${baseResponse.data.toString()}")
                            try {
                                //받은 데이터을 받은 폼으로 리스트로 넘겨준다
                                //바로 변수로 받고 리사이클러 뷰에 넣는다

                                val dairyList = baseResponse.data
                                callback.onSuccessSpringDiaryList(dairyList)

                            }catch (e: JsonSyntaxException) {
                                Log.e("ITM", "JSON 파싱 오류: ", e)
                            }

                        }
                    }
                } else
                { Log.d("ITM", "리스트 함수 들어옴4 ")
                    Log.d("ITM", "$response")}
            }
            // onFailure 구현...
            override fun onFailure(call: Call<getDiaryResponse>, t: Throwable) {
                Log.d("ITM", "리스트 가져오기 실패 ${t.message}")
            }
        })
    }

    fun getFriendList(accessToken :String, onSuccess: (List<FriendData>) -> Unit, onFailure: () -> Unit) {
        Log.d("ITM", "친구 리스트 함수 들어옴1 ")
        writeRequest.getFriendList("Bearer $accessToken").enqueue(object :
            Callback<FriendResponse> {
            override fun onResponse(call: Call<FriendResponse>, response: Response<FriendResponse>) {
                Log.d("ITM", "친구 리스트 함수 들어옴2 ")

                if (response.isSuccessful) {
                    Log.d("ITM", "친구 리스트 함수 들어옴3 ")
                    val baseResponse  = response.body()

                    when (baseResponse?.statusCode) {
                        200 -> {
//                            Log.d("ITM","${baseResponse.data.toString()}")
                            try {
                                //받은 데이터을 받은 폼으로 리스트로 넘겨준다
                                //바로 변수로 받고 리사이클러 뷰에 넣는다

                                val friendDataList = baseResponse.data

                                onSuccess(friendDataList)

                            }catch (e: JsonSyntaxException) {
                                Log.e("ITM", "JSON 파싱 오류: ", e)
                                onFailure()
                            }

                        }
                    }
                } else
                { Log.d("ITM", "친구 리스트 함수 들어옴4 ")
                    Log.d("ITM", "$response")
                    onFailure()
                }
            }
            // onFailure 구현...
            override fun onFailure(call: Call<FriendResponse>, t: Throwable) {
                Log.d("ITM", "친구 리스트 가져오기 실패 ${t.message}")
                onFailure()
            }
        })
    }

    //response form 확인 해야함
    fun getDateDiary(accessToken: String, selectedDate: String, onSuccess: (DiaryListLoad) -> Unit, onFailure: () -> Unit) {
        Log.d("ITM", "Daily diary function entered1 ")
        val selectDate =  DateDiary(selectedDate)
        writeRequest.getDateDiary("Bearer $accessToken", selectDate).enqueue(object :
            Callback<ListResponse> {
            override fun onResponse(call: Call<ListResponse>, response: Response<ListResponse>) {
                Log.d("ITM", "Daily diary function entered2 ")

                if (response.isSuccessful) {
                    Log.d("ITM", "Daily diary function entered3 ")

                    val baseResponse  = response.body()
                    when (baseResponse?.statusCode) {
                        200 -> {
//                            Log.d("ITM","${baseResponse.data.toString()}")
                            try {
                                //받은 데이터을 받은 폼으로 리스트로 넘겨준다
                                //바로 변수로 받고 리사이클러 뷰에 넣는다

                                val diaryEntries = baseResponse.data
                                Log.d("ITM", "Diary Entries: $diaryEntries")

                                if (diaryEntries != null) {
                                    onSuccess(diaryEntries)
                                }

                            }catch (e: JsonSyntaxException) {
                                Log.e("ITM", "JSON 파싱 오류: ", e)
                                onFailure()
                            }

                        }
                    }

                } else {
                    Log.d("ITM", "Daily diary function entered4 ")
                    Log.d("ITM", "$response")
                    onFailure()
                }
            }

            override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                Log.d("ITM", "Failed to import ${t.message} into daily diary")
                onFailure()
            }
        })
    }
}
