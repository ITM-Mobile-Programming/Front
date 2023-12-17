package com.hwido.pieceofdayfront.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.hwido.pieceofdayfront.databinding.LoginMainpageBinding
import com.hwido.pieceofdayfront.DT.BaseResponse
import com.hwido.pieceofdayfront.DT.LoginSuccessResponse

import com.hwido.pieceofdayfront.DT.ServerAccessTokenRequest
import com.hwido.pieceofdayfront.DT.SignupNeededResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.firebase.auth.GoogleAuthProvider
import com.hwido.pieceofdayfront.MainMainpage
import com.hwido.pieceofdayfront.R
import com.hwido.pieceofdayfront.ServerAPI.ServerApiService

// 제대로 안나오는거 해결해야된다
class LoginMainpage : AppCompatActivity() {

    private lateinit var  binding : LoginMainpageBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    // 세션 생성
    // Encrypted SharedPreference로 로그인 세션 유지 =>기존 회원것만 저장 |
    // 기존 회원이면  Encrypted SharedPreference 사용


    // 1. 서버에서 받아오는 함수 클래스화  2. Encryoted preference에 엑세스 토큰 넣고
    // 2-2. 기존 회원이면 sharedPreference에 바로 넣는다, 기존 회원이 아니면 비게 두고,
    // 회원 등록 완료 시에 encyted를 요청해서 방아온다

    // sharepreference에  서버 토큰 저장하는것은 맞다
    //object class로 변환필요하다
    val sharedPreferences: SharedPreferences by lazy {
        val masterKeyAlias = MasterKey
            .Builder(applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()


        EncryptedSharedPreferences.create(
            applicationContext,
            FILE_NAME,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }


    //로그인
    private val signInActivityResult : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        Log.d("ITM","signInActivityResult들어옴")
        //여기서 it.data는 intent
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        handleSignInResult(task)

        Log.d("ITM","handleSignInResult, signInActivityResult나감")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginMainpageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(getString(R.string.gcp_client_id))
            .requestIdToken(getString(R.string.gcp_client_id))
            .requestEmail()
            .build()
        // GoogleSignInClient 인스턴스를 생성합니다.
        // 후에 signIn에 사용
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        // 현재 로그인된 사용자 확인
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            // 이미 로그인된 사용자가 있으면 로그아웃 처리
            mGoogleSignInClient.signOut().addOnCompleteListener {
                // 로그아웃 후 수행할 동작 (예: 토스트 메시지 표시)
                Toast.makeText(this, "이미 로그인된 사용자가 있어 로그아웃되었습니다.", Toast.LENGTH_SHORT).show()
            }

        }

        binding.loginMainpageLoginButton.setOnClickListener {
            //    revokeAccess 엑서스 권한을 취소  나중에 수정할듯
            val signInIntent = mGoogleSignInClient.signInIntent
            signInActivityResult.launch(signInIntent)
        }


    }

    //서버한테 보내는 함수
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        Log.d("ITM","handleSignInResult들어옴")
        //레트로핏 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/oauth2/v4/") // access 토큰 내놔
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        Log.d("ITM","레트로핏 설정 완료")
        val google_request = retrofit.create(GoogleRequestService::class.java)

        Log.d("ITM","$completedTask")
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
            Log.d("ITM","account연결 성공")

            account?.let{ account ->
                // 인증 코드를 사용하여 서버로부터 액세스 토큰 요청
                val authCode = account.serverAuthCode

                //레트로핏 사용 하던가
                //비동기적인 연결 코투틴사용 suspend는 비동기적으로 사용해야한다
                CoroutineScope(Dispatchers.IO).launch {
                    val response = google_request.getAccessToken(
                        code = authCode!!,
                        clientId = getString(R.string.gcp_client_id),  //프로젝트 클라이언트
                        clientSecret = getString(R.string.gcp_client_secret), // 비밀번호
                        redirectUri = "" //프론트엔드로 다시와야됨근데 공백할래
                    )
                    //메인 스레드로 전환
                    withContext(Dispatchers.Main) {
                        //access 토큰을 받아온다
                        response.accessToken?.let {
                            Log.d("ITM","토큰 받아옴 ${response.accessToken}")
                            deliverTokenToSpringServer(response.accessToken)
                            Log.d("ITM","서버 전송완료")
                        }
                        //이제 백엔드 서버에 토큰을 넘겨준다
                    }
                }
            }
        } catch (e: ApiException) {

            Log.w("ITM", "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this, "다시 로그인을 시도해 주세요", Toast.LENGTH_SHORT).show()
        }

    }



    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = FirebaseAuth.getInstance().currentUser
                    Log.w("ITM", "Firebase authentication success ${user.toString()}")
                    // Do something with the Firebase user, e.g., store in your database
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("ITM", "Firebase authentication failed", task.exception)
                }
            }
    }



    fun deliverTokenToSpringServer(accessToken : String){
        Log.d("ITM", accessToken)
        Log.d("ITM", "서버 레트로 핏 설정")
        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.20.166.136/") // 서버의 기본 URL
            .addConverterFactory(GsonConverterFactory.create()) // JSON 변환을 위한 GsonConverterFactory 사용//이건 갈떄
            .build()

        val Server_service = retrofit.create(ServerApiService::class.java)
//        val accessTokenRequest = ServerAccessTokenRequest(accessToken = accessToken)
        val accessTokenRequest = ServerAccessTokenRequest(accessToken)
        //받는건 하나인데 message에 따라서 으답이 변해댜 된다 근데 받는 폼이 다르다
        //와일드 카드 안된다.
        Server_service.login(accessTokenRequest).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    val baseResponse  = response.body()

                    when (baseResponse?.status) {
                        201 -> {

                            // signupResponse 처리
                            //gson 파싱 코드 분석 필요 ,, 일단 연결은 잘된다
                            val signupResponse = Gson().fromJson(baseResponse?.data.toString(), SignupNeededResponse::class.java)
                            Log.d("ITM","회원가입필요 이메일 : ${signupResponse?.email}, 비밀번호 ${signupResponse?.password}")


                            // 이메일이랑 password를 받아야하나??
                            val intent = Intent(this@LoginMainpage, LoginSigninpage::class.java)
//                            intent.putExtra("email","${signupResponse.email}.")

                            signupResponse?.email?.let { signupResponse?.password?.let { it1 ->
                                Google_Access_EmailPW_SavePref(accessToken, it, it1)
                            } }

                            startActivity(intent)
                        }
                        200 -> {
                            // loginResponse 처리
                            val loginResponse = Gson().fromJson(baseResponse.data.toString(), LoginSuccessResponse::class.java)
//                            intent.putExtra("access_token","${loginResponse.accessToken}")

                            loginResponse.accessToken?.let { AddToGoogleToSpringToken(it) }
                            val intent = Intent(this@LoginMainpage, MainMainpage::class.java)
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

    // JWT 저장
    fun AddToGoogleToSpringToken(JWTaccessToken: String){
        val editor = sharedPreferences.edit()

        if(sharedPreferences.contains(google_access_token)){
            editor.remove(google_access_token)
            editor.remove(google_email)
            editor.remove(server_password)
        }

        editor.putString(app_JWT_token, JWTaccessToken)

        editor.apply()//shared Preference에 저장
        Toast.makeText(this, "SH, JWT정보저장.", Toast.LENGTH_SHORT).show()
    }

    fun Google_Access_EmailPW_SavePref(google_access: String, email: String, password:String){
        val editor = sharedPreferences.edit()

        editor.putString(google_email, email)
        editor.putString(server_password, password)
        editor.putString(google_access_token, google_access)

        editor.apply()//shared Preference에 저장
        Toast.makeText(this, "email, password 정보저장.", Toast.LENGTH_SHORT).show()
    }


    companion object{
        const val FILE_NAME = "encrypted_settings"
        const val google_access_token = "google_access_token"
        const val app_JWT_token = "app_JWT_token"
        const val google_email = "google_email"
        const val server_password = "google_password"
    }


}