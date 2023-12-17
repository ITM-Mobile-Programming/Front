package com.hwido.pieceofdayfront.ServerAPI;

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.gson.JsonSyntaxException

import com.hwido.pieceofdayfront.DT.BaseResponse2
import com.hwido.pieceofdayfront.DT.BasicResponse
import com.hwido.pieceofdayfront.DT.CheckResponse
import com.hwido.pieceofdayfront.DT.DiaryEntry
import com.hwido.pieceofdayfront.DT.FriendCode
import com.hwido.pieceofdayfront.DT.OneDayCheck
import com.hwido.pieceofdayfront.DT.RelayDataRequest
import com.hwido.pieceofdayfront.DT.SendMBTI
import com.hwido.pieceofdayfront.DT.WriteDataRequest
import com.hwido.pieceofdayfront.DT.diaryID
import com.hwido.pieceofdayfront.DT.getDiaryResponse
import com.hwido.pieceofdayfront.DT.getMyData
import com.hwido.pieceofdayfront.DT.myPageBaseData
import com.hwido.pieceofdayfront.DT.reloadDairy


import com.hwido.pieceofdayfront.DT.DateDiary

import com.hwido.pieceofdayfront.DT.DiaryListLoad
import com.hwido.pieceofdayfront.DT.FriendData
import com.hwido.pieceofdayfront.DT.FriendResponse
import com.hwido.pieceofdayfront.DT.ListResponse



import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody
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
    fun sendDiaryToGetImage(firstRequest : WriteDataRequest, accessToken :String,  onSuccess: (Int, String,String) -> Unit,
                            onFailure: () -> Unit) {

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

                                onSuccess(longToInt, url, hashTag)

                            }catch (e: JsonSyntaxException) {
                                Log.e("ITM", "JSON 파싱 오류: ", e)
                                onFailure()
                            }

                        }
                    }
                } else
                {Log.d("ITM", "${response}")
                    onFailure()}

            }
            // onFailure 구현...
            override fun onFailure(call: Call<BaseResponse2>, t: Throwable) {
                Log.d("ITM", "뺵엔드 연결실패 ${t.message}")
                onFailure()

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


    fun getDiaryList(accessToken :String, onSuccess: (List<DiaryEntry>) -> Unit,
                     onFailure: () -> Unit) {
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
//                                callback.onSuccessSpringDiaryList(dairyList)
                                onSuccess(dairyList)

                            }catch (e: JsonSyntaxException) {
                                Log.e("ITM", "JSON 파싱 오류: ", e)
                                onFailure
                            }

                        }
                    }
                } else
                { Log.d("ITM", "리스트 함수 들어옴4 ")
                    Log.d("ITM", "$response")}
                onFailure
            }
            // onFailure 구현...
            override fun onFailure(call: Call<getDiaryResponse>, t: Throwable) {
                Log.d("ITM", "리스트 가져오기 실패 ${t.message}")
                onFailure
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







//500 /404 수정 필요
    fun  checkOneDay(accessToken :String , onSuccess: (String) -> Unit,
                     onFailure: () -> Unit) {
        Log.d("ITM", "리스트 함수 들어옴1 ")
        writeRequest.checkVerification("Bearer $accessToken").enqueue(object :
            Callback<OneDayCheck> {
            override fun onResponse(call: Call<OneDayCheck>, response: Response<OneDayCheck>) {
                Log.d("ITM", "리스트 함수 들어옴2 ")

                Log.d("ITM", "JSON 파싱 오류: ${accessToken}" )
                if (response.isSuccessful) {
                    Log.d("ITM", "리스트 함수 들어옴3 ")
                    val baseResponse  = response.body()

                    when (baseResponse?.statusCode) {
                        200 -> {
//                            Log.d("ITM","${baseResponse.data.toString()}")
                            try {
                                //받은 데이터을 받은 폼으로 리스트로 넘겨준다
                                //바로 변수로 받고 리사이클러 뷰에 넣는다

                                //writeForm
                                //accesstoken 넣어주기
                                Log.e("ITM", "JSON 파싱 오류: ${accessToken}" )
                                //일기작성
                            }catch (e: JsonSyntaxException) {
                                Log.e("ITM", "JSON 파싱 오류: ", e)
                                //일기
                                onFailure()
                            }
                        }

                    }
                } else { Log.d("ITM", "리스트 함수 들어옴4 ")
                    Log.d("ITM_verification", "${response.code()}")}

                val baseResponseCode  = response.code()
                when (baseResponseCode) {
                    404 -> {
//                            Log.d("ITM","${baseResponse.data.toString()}")
                        try {
                            //받은 데이터을 받은 폼으로 리스트로 넘겨준다
                            //바로 변수로 받고 리사이클러 뷰에 넣는다
                            //writeForm
                            //accesstoken 넣어주기
                            Log.e("ITM", "JSON 파싱 오류: ${accessToken}")
                            onSuccess(accessToken)
                            //일기작성
                        } catch (e: JsonSyntaxException) {
                            Log.e("ITM", "JSON 파싱 오류: ", e)
                            //일기
                            onFailure()
                        }
                    }
                    else ->{
                        onFailure()
                    }
                }


            }
            // onFailure 구현...
            override fun onFailure(call: Call<OneDayCheck>, t: Throwable) {
                Log.d("ITM", "리스트 가져오기 실패 ${t.message}")

                onFailure()
            }
        })
    }


    //마이페이지 를 넘길떄 writeform으로 넘긴다
    //   fun getMyPage(@Header("Authorization") authToken: String?) : Call<getMyData>
    fun getMyPage(accessToken :String, onSuccess: (getMyData) -> Unit,
                     onFailure: () -> Unit) {
        Log.d("ITM", "마이페이지함수 들어옴1 ")
        writeRequest.getMyPage("Bearer $accessToken").enqueue(object :
            Callback<myPageBaseData> {
            override fun onResponse(call: Call<myPageBaseData>, response: Response<myPageBaseData>) {
                Log.d("ITM", "마이페이지함수 들어옴2 ")

                if (response.isSuccessful) {
                    Log.d("ITM", "마이페이지함수 들어옴3 ")
                    val baseResponse  = response.body()

                    when (baseResponse?.statusCode) {
                        200 -> {
                            Log.d("ITM", "마이페이지함수 들어옴4 ")
//                            Log.d("ITM","${baseResponse.data.toString()}")
                            try {
                                //받은 데이터을 받은 폼으로 리스트로 넘겨준다
                                //바로 변수로 받고 리사이클러 뷰에 넣는다

                                val MyData = baseResponse?.data
//                                callback.onSuccessSpringDiaryList(dairyList)
                                Log.d("ITM", "$MyData")
                                if (MyData != null) {
                                    onSuccess(MyData)
                                }

                            }catch (e: JsonSyntaxException) {
                                Log.e("ITM", "JSON 파싱 오류: ", e)
                                onFailure
                            }

                        }
                    }
                } else
                { Log.d("ITM", "리스트 함수 들어옴4 ")
                    Log.d("ITM", "$response")}
                onFailure
            }
            // onFailure 구현...
            override fun onFailure(call: Call<myPageBaseData>, t: Throwable) {
                Log.d("ITM", "리스트 가져오기 실패 ${t.message}")
                onFailure
            }
        })
    }

    fun checkIfFriend(accessToken :String, code:String, onSuccess: (Int) -> Unit,
                  onFailure: () -> Unit) {
        Log.d("ITM", "찬구체크 들어옴1")
        val freindCode = FriendCode(code)
        writeRequest.checkIfFriend("Bearer $accessToken", freindCode).enqueue(object :
            Callback<CheckResponse> {
            override fun onResponse(call: Call<CheckResponse>, response: Response<CheckResponse>) {
                Log.d("ITM", "찬구체크 들어옴 2")

                if (response.isSuccessful) {
                    Log.d("ITM", "찬구체크 들어옴 3")
                    val baseResponse  = response.body()

                    when (baseResponse?.status) {
                        200 -> {
                            Log.d("ITM", "찬구체크 들어옴 4")
//                            Log.d("ITM","${baseResponse.data.toString()}")
                            try {
                                //받은 데이터을 받은 폼으로 리스트로 넘겨준다
                                //바로 변수로 받고 리사이클러 뷰에 넣는다

                                val whetherFriend = baseResponse?.data!!
//                                callback.onSuccessSpringDiaryList(dairyList)
                                Log.d("ITM", "$whetherFriend")
                                //친구면 0 친구아니면 1

                                if(whetherFriend){
                                    //친구면 0
                                    onSuccess(0)
                                }else{
                                    //친구 아니면 1
                                    onSuccess(1)
                                }

                            }catch (e: JsonSyntaxException) {
                                Log.e("ITM", "JSON 파싱 오류: ", e)
                                onFailure
                            }

                        }
                    }
                } else
                { Log.d("ITM", "리스트 함수 들어옴4 ")
                    Log.d("ITM", "$response")}
                onFailure
            }
            // onFailure 구현...
            override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
                Log.d("ITM", "리스트 가져오기 실패 ${t.message}")
                onFailure
            }
        })
    }


//    deleteDiary


    fun deleteDiary(accessToken :String, diaryID: Int, onSuccess: () -> Unit,
                    onFailure: () -> Unit) {
        Log.d("ITM", "삭제체크 들어옴1")
        val diaryID = diaryID(diaryID)
        writeRequest.deleteDiary("Bearer $accessToken", diaryID).enqueue(object :
            Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                Log.d("ITM", "삭제체크 들어옴 2")

                if (response.isSuccessful) {
                    Log.d("ITM", "삭제체크 들어옴 3")
                    val baseResponse  = response.body()

                    when (baseResponse?.status) {
                        200 -> {
                            Log.d("ITM", "삭제체크 들어옴 4")
//                            Log.d("ITM","${baseResponse.data.toString()}")
                            try {
                                //받은 데이터을 받은 폼으로 리스트로 넘겨준다
                                //바로 변수로 받고 리사이클러 뷰에 넣는다
                                onSuccess()

                                Log.d("ITM", "다이어리 삭제완료 ")
                            }catch (e: JsonSyntaxException) {
                                Log.e("ITM", "JSON 파싱 오류: ", e)

                            }

                        }
                    }
                } else
                { Log.d("ITM", "삭제체크  들어옴4 ")
                    Log.d("ITM", "$response")
                    onFailure()}

            }
            // onFailure 구현...
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d("ITM", "삭제체크  실패 ${t.message}")
                onFailure()
            }
        })
    }





        fun getSharedDiary(accessToken :String, onSuccess: (List<DiaryEntry>) -> Unit,
                       onFailure: () -> Unit) {
            Log.d("ITM", "리스트 함수 들어옴1 ")
            writeRequest.getSharedDiary("Bearer $accessToken").enqueue(object :
                Callback<getDiaryResponse> {
                override fun onResponse(call: Call<getDiaryResponse>, response: Response<getDiaryResponse>) {
                    Log.d("ITM", "리스트 함수 들어옴2 ")

                    if (response.isSuccessful) {
                        Log.d("ITM", "리스트 함수 들어옴3 ")
                        val baseResponse = response.body()


                        when (baseResponse?.statusCode) {
                            200 -> {
                                Log.d("ITM", "삭제체크 들어옴 4")
//                            Log.d("ITM","${baseResponse.data.toString()}")
                                try {
                                    //받은 데이터을 받은 폼으로 리스트로 넘겨준다
                                    //바로 변수로 받고 리사이클러 뷰에 넣는다


                                    val sharedDataList = baseResponse.data

                                    onSuccess(sharedDataList)


                                    Log.d("ITM", "다이어리 삭제완료 ")
                                }catch (e: JsonSyntaxException) {
                                    Log.e("ITM", "JSON 파싱 오류: ", e)

                                }

                            }
                        }
                    } else
                    { Log.d("ITM", "삭제체크  들어옴4 ")
                        Log.d("ITM", "$response")
                        onFailure()}

                }
                // onFailure 구현...
                override fun onFailure(call: Call<getDiaryResponse>, t: Throwable) {
                    Log.d("ITM", "삭제체크  실패 ${t.message}")
                    onFailure()
                }
            })
        }




        //response form 확인 해야함
        fun getDateDiary(
            accessToken: String,
            selectedDate: String,
            onSuccess: (DiaryListLoad) -> Unit,
            onFailure: () -> Unit
        ) {
            Log.d("ITM", "Daily diary function entered1 ")
            val selectDate = DateDiary(selectedDate)
            writeRequest.getDateDiary("Bearer $accessToken", selectDate)
                .enqueue(object :
                    Callback<ListResponse> {
                    override fun onResponse(
                        call: Call<ListResponse>,
                        response: Response<ListResponse>
                    ) {
                        Log.d("ITM", "Daily diary function entered2 ")

                        if (response.isSuccessful) {
                            Log.d("ITM", "Daily diary function entered3 ")

                            val baseResponse = response.body()

                            when (baseResponse?.statusCode) {
                                200 -> {
//                            Log.d("ITM","${baseResponse.data.toString()}")
                                    try {
                                        //받은 데이터을 받은 폼으로 리스트로 넘겨준다
                                        //바로 변수로 받고 리사이클러 뷰에 넣는다


                                        val dairyList = baseResponse.data!!
//                                callback.onSuccessSpringDiaryList(dairyList)
                                        onSuccess(dairyList)

                                    } catch (e: JsonSyntaxException) {
                                        Log.e("ITM", "JSON 파싱 오류: ", e)
                                        onFailure
                                    }

                                }
                            }
                        } else {
                            Log.d("ITM", "리스트 함수 들어옴4 ")
                            Log.d("ITM", "$response")
                        }
                        onFailure
                    }

                    // onFailure 구현...
                    override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                        Log.d("ITM", "리스트 가져오기 실패 ${t.message}")
                        onFailure
                    }
                })
        }


        fun getImagePage(
            memberID: Int, onSuccess: (Bitmap) -> Unit,
            onFailure: () -> Unit
        ) {
            Log.d("ITM", "리스트 함수 들어옴1 ")
            writeRequest.getImagePage(memberID).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("ITM", "리스트 함수 들어옴2 ")

                    if (response.isSuccessful) {
                        Log.d("ITM", "리스트 함수 들어옴3 ")
                        val baseResponse = response.body()


                        //받아와서 사진 스트림처리
                        val inputStream = response.body()?.byteStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        onSuccess(bitmap)



                    } else {
                        Log.d("ITM", "리스트 함수 들어옴4 ")
                        Log.d("ITM", "$response")
                    }
                    onFailure
                }

                // onFailure 구현...
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("ITM", "리스트 가져오기 실패 ${t.message}")
                    onFailure
                }
            })
        }


                    fun AddFriend(accessToken: String, code: String) {
                        Log.d("ITM", "마이페이지함수 들어옴1 ")

                        val friendcode = FriendCode(code)
                        writeRequest.AddFriend("Bearer $accessToken", friendcode).enqueue(object :
                            Callback<BasicResponse> {
                            override fun onResponse(
                                call: Call<BasicResponse>,
                                response: Response<BasicResponse>
                            ) {
                                Log.d("ITM", "마이페이지함수 들어옴2 ")

                                if (response.isSuccessful) {
                                    Log.d("ITM", "마이페이지함수 들어옴3 ")
                                    val baseResponse = response.body()

                                    when (baseResponse?.status) {
                                        200 -> {
                                            Log.d("ITM", "마이페이지함수 들어옴4 ")
//                            Log.d("ITM","${baseResponse.data.toString()}")
                                            try {
                                                //받은 데이터을 받은 폼으로 리스트로 넘겨준다
                                                //바로 변수로 받고 리사이클러 뷰에 넣는다

                                                Log.d("ITM", "친구추가 성공")
                                            } catch (e: JsonSyntaxException) {
                                                Log.e("ITM", "JSON 파싱 오류: ", e)
                                            }
                                        }
                                    }
                                } else {
                                    Log.d("ITM", "리스트 함수 들어옴4 ")
                                    Log.d("ITM", "$response")
                                }

                            }

                            // onFailure 구현...
                            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                                Log.d("ITM", "리스트 가져오기 실패 ${t.message}")

                            }
                        })
                    }


                    //릴레이 함수

                    //여기서 왜?
                    fun relayWrite(
                        accessToken: String,
                        diaryID: Int,
                        friendCode: String,
                        updateContext: String,
                        onSuccess: () -> Unit,
                        onFailure: () -> Unit
                    ) {
                        Log.d("ITM", "마이페이지함수 들어옴1 ")

                        val relayform = RelayDataRequest(diaryID, friendCode, updateContext)
                        writeRequest.relayWrite("Bearer $accessToken", relayform).enqueue(object :
                            Callback<BasicResponse> {
                            override fun onResponse(
                                call: Call<BasicResponse>,
                                response: Response<BasicResponse>
                            ) {
                                Log.d("ITM", "마이페이지함수 들어옴2 ")

                                if (response.isSuccessful) {
                                    Log.d("ITM", "마이페이지함수 들어옴3 ")
                                    val baseResponse = response.body()

                                    when (baseResponse?.status) {
                                        200 -> {
                                            Log.d("ITM", "공유성공 ")
//                            Log.d("ITM","${baseResponse.data.toString()}")
                                            try {
                                                //받은 데이터을 받은 폼으로 리스트로 넘겨준다
                                                //바로 변수로 받고 리사이클러 뷰에 넣는다
                                                onSuccess()

                                            } catch (e: JsonSyntaxException) {
                                                Log.e("ITM", "JSON 파싱 오류: ", e)
                                                onFailure()
                                            }

                                        }
                                    }
                                } else {
                                    Log.d("ITM", "리스트 함수 들어옴4 ")
                                    Log.d("ITM", "$response")
                                    onFailure()
                                }

                            }

                            // onFailure 구현...
                            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                                Log.d("ITM", "리스트 가져오기 실패 ${t.message}")
                                onFailure()


                            }
                        })
                    }

                }