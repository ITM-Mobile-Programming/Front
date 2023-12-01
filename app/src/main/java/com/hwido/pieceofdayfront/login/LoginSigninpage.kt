package com.hwido.pieceofdayfront.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.hwido.pieceofdayfront.ServerApiService
import com.hwido.pieceofdayfront.databinding.LoginSigninpageBinding
import com.hwido.pieceofdayfront.datamodel.BaseResponse
import com.hwido.pieceofdayfront.datamodel.SignUpRequest
import com.hwido.pieceofdayfront.datamodel.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginSigninpage : AppCompatActivity() {
    private lateinit var  binding : LoginSigninpageBinding
    private lateinit var email : String
    private lateinit var password: String

    //여기서는 프로필 사진이랑 텍스트 추가 데이터를 보낸다, 일단 비싸게 한다
    // storage 권한 처리에 필요한 변수


    //공유 sharedPreference
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginSigninpageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 서버 api로 넣어준다
        // 넣고 다시 access 토큰 받은거로 요청해서 접근 access 토큰 받아온다
        // 여기서 다시 설정 해야할 듯
        // 로그인 계정을 바로 불러오면 좋을텐데
        // 1. 계정확인 or 재입력후 access_token 받는다
        // 2. 메인 페이지로 돌아간다 그 후 access_token 받는다

        binding.loginSigninpageSigninBtn.setOnClickListener {
            // 메인 페이지 access 토큰 사용
            // email, password 불러온다

            // 등록
            // email로 일단, password, introduce, nickname 보낸다
            // signUp() + profile 엑티비티로 넘어가기
//            signUpToSpringServer(email, password, intro, nickname)
            var nickname = binding.loginSigninpageNickNameSigninArea.text.toString()
            var intro = binding.loginSigninpageSelfShortIntroSigninArea.text.toString()

            if(sharedPreferences.contains(LoginMainpage.google_access_token)){
                //getString 사용필요
                email = sharedPreferences.getString(LoginMainpage.google_email, "email").toString()
                password = sharedPreferences.getString(LoginMainpage.server_password, "pw").toString()

                Log.d("ITM", "이메일 : $email, 비밀번호 : $password")

                Toast.makeText(this, "SHSignNeeded정보로드.", Toast.LENGTH_SHORT).show()
            }
            signUpToSpringServer(email, password, intro, nickname)


        }
    }

    fun signUpToSpringServer(email : String?, password: String?, intro: String?, nickname: String?){
        Log.d("ITM", "서버 레트로 핏 설정")
        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.20.166.136/") // 서버의 기본 URL
            .addConverterFactory(GsonConverterFactory.create()) // JSON 변환을 위한 GsonConverterFactory 사용//이건 갈떄
            .build()

        val Server_service = retrofit.create(ServerApiService::class.java)

        val signUprequestform  = SignUpRequest(email, password, intro, nickname)

        Server_service.signUp(signUprequestform).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    val baseResponse  = response.body()

                    when (baseResponse?.status) {
                        200 -> {
                            // loginResponse 처리
                            val loginResponse = Gson().fromJson(baseResponse.data.toString(), SignUpResponse::class.java)
//                            intent.putExtra("access_token","${loginResponse.accessToken}")

                            val intent = Intent(this@LoginSigninpage, LoginSignInProfilePicture::class.java)
                            Log.d("ITM","${loginResponse.memberId}")
                            val memberValue = loginResponse.memberId?.split(".")

                            intent.putExtra("memberId", memberValue?.get(0))
                            startActivity(intent)
                        }
                    }
                }


                Log.d("ITM", "${response}")
            }
            // onFailure 구현...
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.d("ITM", "뺵엔드 연결실패 ${t.message}")

            }
        })
    }





}