package com.hwido.pieceofdayfront

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.JsonSyntaxException
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageBinding
import com.hwido.pieceofdayfront.datamodel.ListResponse
import com.hwido.pieceofdayfront.login.LoginMainpage
import com.hwido.pieceofdayfront.myPage.MainMypage
import com.hwido.pieceofdayfront.writeNew.MainDiaryWritepageContent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainDiaryWritepage : AppCompatActivity() {
    private lateinit var binding: MainDiarywritepageBinding


    val sharedPreferences: SharedPreferences by lazy {
        val masterKeyAlias = MasterKey
            .Builder(applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            applicationContext,
            LoginMainpage.FILE_NAME,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }


    //레트로핏 설정
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://3.20.166.136/") // 서버의 기본 URL
        .addConverterFactory(GsonConverterFactory.create()) // JSON 변환을 위한 GsonConverterFactory 사용//이건 갈떄
        .build()


    //목록 가져오는 파일 만들어야 된다
    //수정필요
    private fun getDiaryList() {
        val writeRequest = retrofit.create(ServerApiService::class.java)
        val accessToken = sharedPreferences.getString(LoginMainpage.app_JWT_token, "access").toString()

//        Log.d("ITM","$accessToken")
        //프로그래스 바 추가
        writeRequest.getMemeberDiary("Bearer $accessToken").enqueue(object :
            Callback<ListResponse> {
            override fun onResponse(call: Call<ListResponse>, response: Response<ListResponse>) {
                if (response.isSuccessful) {
                    val baseResponse  = response.body()

                    Log.d("ITM","${baseResponse?.data?.hashTags.toString()}")

                    when (baseResponse?.status) {
                        200 -> {
                            try {

                                val longToInt =baseResponse?.data?.title ?.toInt()

                            }catch (e: JsonSyntaxException) {
                                Log.e("ITM", "JSON 파싱 오류: ", e)
                            }

                        }
                    }
                } else
                {Log.d("ITM", "${response}")}
            }
            // onFailure 구현...
            override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                Log.d("ITM", "이건 아지 아님 뺵엔드 연결실패 ${t.message}")

            }
        })
    }




    //엑티비티에 들어올떄마다 자동으로 체크하면서 실행하도록
    override fun onStart() {
        super.onStart()
        getDiaryList()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainDiarywritepageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.mainDiarywritepageWriteDiary.setOnClickListener {
            var intent = Intent(this, MainDiaryWritepageContent::class.java)
            startActivity(intent)
        }


        // 하단 버튼 통한 페이지 변경
        val mainBtnMain = findViewById<Button>(R.id.main_button_diaryList)
        val mainBtnShare = findViewById<Button>(R.id.main_button_diaryShare)
        val mainBtnMyPage = findViewById<Button>(R.id.main_button_myPage)


        mainBtnMain.setOnClickListener {
            val intent = Intent(baseContext, MainMainpage::class.java)
            startActivity(intent)
        }

        mainBtnShare.setOnClickListener {
            val intent = Intent(baseContext, MainDiarySharepage::class.java)
            startActivity(intent)
        }

        mainBtnMyPage.setOnClickListener {
            val intent = Intent(baseContext, MainMypage::class.java)
            startActivity(intent)
        }



    }


    ///////////////////////////////////////////////////////



}