package com.hwido.pieceofdayfront.login

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.hwido.pieceofdayfront.R
import com.hwido.pieceofdayfront.ServerApiService
import com.hwido.pieceofdayfront.databinding.LoginSignInProfilePictureBinding
import com.hwido.pieceofdayfront.datamodel.BaseResponse
import com.hwido.pieceofdayfront.datamodel.LoginSuccessResponse
import com.hwido.pieceofdayfront.datamodel.ServerAccessTokenRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.FileOutputStream
import java.text.SimpleDateFormat



class LoginSignInProfilePicture : AppCompatActivity() {

    private lateinit var  binding : LoginSignInProfilePictureBinding
    private lateinit var pictureBinaryFileUri : Uri
//    private lateinit var body: MultipartBody.Part

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


    companion object {
        val CAMERA = arrayOf(android.Manifest.permission.CAMERA)
        val STORAGE = arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES)
        const val CAMERA_CODE = 98
        const val STORAGE_CODE = 99
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginSignInProfilePictureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginSigninpageTakePicture.setOnClickListener {
            CallCamera()
        }
        binding.loginSigninpageTakePicture.setOnClickListener {
            CallCamera()
        }

        // 사진 저장
        binding.loginSigninpageChangeProfileGalleryPicture.setOnClickListener {
            GetAlbum()
        }

        binding.loginFinalBtn.setOnClickListener {
            Log.d("ITM","Final  여기부터 시작")
            val google_access_token = sharedPreferences.getString(LoginMainpage.google_access_token, "access").toString()

            //memeberid를 받는다// 수정필요
            val memberId = intent.getStringExtra("memberId")
            Log.d("ITM", "번호 $memberId")
            //여기서 사진을 보낸다
            if(memberId != null){
                deliverToSpringServer_Picture(this, memberId, pictureBinaryFileUri , google_access_token)
            }
//            Log.d("ITM","엑세스 토큰 $google_access_token")


        }



    }

    fun deliverToSpringServer_Picture(context: Context, memberId:String?, fileUri:Uri, google_access_token: String){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.20.166.136/") // 서버의 기본 URL
            .addConverterFactory(GsonConverterFactory.create()) // JSON 변환을 위한 GsonConverterFactory 사용//이건 갈떄
            .build()

        val Server_service = retrofit.create(ServerApiService::class.java)

//        val contentResolver = context.contentResolver
//        val inputStream = contentResolver.openInputStream(fileUri)

//        //파일이름 찾지 말고 uri로 바로하기
        //inputStream?.use  분석필요
//        inputStream?.use { stream ->
//            val byteArray = stream.readBytes()
//            val fileBody = byteArray.toRequestBody("image/*".toMediaTypeOrNull())
//            if (fileBody != null) {
//                val fileName = fileUri.lastPathSegment ?: "${memberId}.jpg"
//                body = MultipartBody.Part.createFormData("file", fileName, fileBody)
//
//            } else {
//                // Handle the case where fileBody is null (e.g., log an error or show a message)
//                Log.d("ITM","filebody가 0임")
//            }
//
//        }

        //스트림 사용이 문제 였다 왜 문제인지는 모르겠긴함// 스트림이 완료가 된고 나서 해야 되나 싶기도 합니다,, 순서의 차일 일 수도
        // 그외 다른것이 없음
        val inputStream = context.contentResolver.openInputStream(fileUri) ?: return

        val byteArray = inputStream.readBytes()
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), byteArray)
        val filePart = MultipartBody.Part.createFormData("file", "filename${memberId}.png", requestFile)

//        val filePath = fileUri.path
//        val file = File(filePath)


//        Log.d("ITM","이미지 파일 담기 완료 ${body}")

        //여기서 리퀘스트 실패 한거임
            Server_service.postProfileSignUp(memberId!!, filePart).enqueue(object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    if (response.isSuccessful) {
                        val baseResponse  = response.body()
                        when (baseResponse?.status) {
                            200 -> {
                                // loginResponse 처리
//                            val loginResponse = Gson().fromJson(baseResponse.data.toString(), LoginSuccessResponse::class.java)
//                            intent.putExtra("access_token","${loginResponse.accessToken}")
                                Log.d("ITM","이미지 전송완료 이메일")
                                deliverTokenToSpringServer_accessToken(google_access_token)
                            }
                        }
                    }else{
                        Log.d("ITM", "final_pioture ${response}")
                    }
                }
                // onFailure 구현...
                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    Log.d("ITM", "뺵엔드 연결실패 ${t.message}")

                }
            })
    }


    private fun showPopup() {
        val popup = LoginSigninpagePopup(this)
        popup.show()
    }
    //여기서는 basecontext도 그건데 음...엑세스 토큰을 받아와야한다
    fun deliverTokenToSpringServer_accessToken(google_accessToken : String){
        Log.d("ITM", "서버 레트로 핏 설정")
        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.20.166.136/") // 서버의 기본 URL
            .addConverterFactory(GsonConverterFactory.create()) // JSON 변환을 위한 GsonConverterFactory 사용//이건 갈떄
            .build()

        val Server_service = retrofit.create(ServerApiService::class.java)
//        val accessTokenRequest = ServerAccessTokenRequest(accessToken = accessToken)


        val accessTokenRequest = ServerAccessTokenRequest(google_accessToken)
        //받는건 하나인데 message에 따라서 으답이 변해댜 된다 근데 받는 폼이 다르다
        //와일드 카드 안된다.

        Server_service.login(accessTokenRequest).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    val baseResponse  = response.body()

                    Log.d("ITM","${baseResponse?.data}")
                    when (baseResponse?.status) {

                        200 -> {
                            // loginResponse 처리
                            // 연결은 되는데 access_token은 없다
                            val loginResponse = Gson().fromJson(baseResponse.data.toString(), LoginSuccessResponse::class.java)
//                            intent.putExtra("access_token","${loginResponse.accessToken}")
                            Log.d("ITM","회원가입완료 access_token")

                            loginResponse.accessToken?.let {
                                AddToGoogleToSpringToken(it)
                                showPopup()}

                        }
                    }
                }else{
                    Log.d("ITM", "${response}")
                }



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

        if(sharedPreferences.contains(LoginMainpage.google_access_token)){
            editor.remove(LoginMainpage.google_access_token)
            editor.remove(LoginMainpage.google_email)
            editor.remove(LoginMainpage.server_password)
        }

        editor.putString(LoginMainpage.app_JWT_token, JWTaccessToken)

        editor.apply()//shared Preference에 저장
        Toast.makeText(this, "SH, JWT정보저장.", Toast.LENGTH_SHORT).show()
    }


    // 카메라 권한, 저장소 권한
// 요청 권한
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            CAMERA_CODE -> {
                for (grant in grantResults){
                    if(grant != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "카메라 권한을 승인해 주세요", Toast.LENGTH_LONG).show()
                        showRotationalDialogForPermission()
                    }
                }
            }

            STORAGE_CODE -> {
                for(grant in grantResults){
                    if(grant != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "저장소 권한을 승인해 주세요", Toast.LENGTH_LONG).show()
                        showRotationalDialogForPermission()
                    }
                }
            }
        }
    }


    // 다른 권한등도 확인이 가능하도록
    fun checkPermission(permissions: Array<out String>, type:Int):Boolean{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for (permission in permissions){
                if(ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, permissions, type)
                    return false
                }
            }
        }
        return true
    }


    // 카메라 촬영 - 권한 처리
    fun CallCamera(){
        if(checkPermission(CAMERA, CAMERA_CODE) && checkPermission(
                STORAGE,
                STORAGE_CODE
            )){
            val itt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(itt, CAMERA_CODE)
        }
    }


    // 사진 저장 // 이미지를 저장하고 해당 uri를 반환한다
    fun saveFile(fileName:String, mimeType:String, bitmap: Bitmap): Uri?{

        var CV = ContentValues()

        // MediaStore 에 파일명, mimeType 을 지정
        CV.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        CV.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        // 안정성 검사
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            CV.put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        // MediaStore 에 파일을 저장
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CV)
        if(uri != null){
            var scriptor = contentResolver.openFileDescriptor(uri, "w")

            val fos = FileOutputStream(scriptor?.fileDescriptor)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                CV.clear()
                // IS_PENDING 을 초기화
                CV.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(uri, CV, null, null)
            }
        }
        return uri
    }

    // 결과
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val imageView = findViewById<ImageView>(R.id.individualPicture)

        if(resultCode == RESULT_OK){
            when(requestCode){
                CAMERA_CODE -> {
                    if(data?.extras?.get("data") != null){
                        val img = data?.extras?.get("data") as Bitmap
                        val uri = saveFile(RandomFileName(), "image/jpeg", img)
                        pictureBinaryFileUri =  uri!!
                        imageView.setImageURI(uri)
                    }
                }
                STORAGE_CODE -> {
                    val uri = data?.data
                    pictureBinaryFileUri  =  uri!!
                    imageView.setImageURI(uri)
                }
            }
        }
    }

    // 파일명을 날짜 저장
    fun RandomFileName() : String{
        val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
        return fileName
    }

    // 갤러리 취득
    fun GetAlbum(){
        if(checkPermission(STORAGE, STORAGE_CODE)){
            val itt = Intent(Intent.ACTION_PICK)
            itt.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(itt, STORAGE_CODE)

        }
    }


    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(this)
            .setMessage(
                "It looks like you have turned off permissions"
                        + "required for this feature. It can be enable under App settings!!!"
            )

            //세팅으로 간다
            .setPositiveButton("Go To SETTINGS") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)

                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            //_-> 이게 뭐야
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }





}














































