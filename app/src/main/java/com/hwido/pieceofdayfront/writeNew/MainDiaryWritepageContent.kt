package com.hwido.pieceofdayfront.writeNew

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.gson.JsonSyntaxException
import com.hwido.pieceofdayfront.R
import com.hwido.pieceofdayfront.SeverMemberRequestAPI
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageContentBinding
import com.hwido.pieceofdayfront.datamodel.BaseResponse2
import com.hwido.pieceofdayfront.datamodel.WriteDataRequest
import com.hwido.pieceofdayfront.login.LoginMainpage
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainDiaryWritepageContent : AppCompatActivity() , KakaoResponseCallback{

    private lateinit var binding : MainDiarywritepageContentBinding
    private val kakaoAPI = KakaoRetrofitClient()
    //공유 sharedPreference
    // 이거  object class로 만들어서 뺼거임
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

    //back 해도 돌아가게 해야됨
    override fun onResume() {
        super.onResume()
        requestLocation()
    }

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


    private fun sendDiaryToGetImage(firstRequest : WriteDataRequest) {
        val writeRequest = retrofit.create(SeverMemberRequestAPI::class.java)
        val accessToken = sharedPreferences.getString(LoginMainpage.app_JWT_token, "access").toString()

        writeRequest.postMemberDairy("Bearer $accessToken", firstRequest).enqueue(object :
            Callback<BaseResponse2> {
            override fun onResponse(call: Call<BaseResponse2>, response: Response<BaseResponse2>) {
                if (response.isSuccessful) {
                    val baseResponse  = response.body()

                    Log.d("ITM","${baseResponse?.data?.hashTags.toString()}")
                    Log.d("ITM","${baseResponse?.data.toString().substring(20,30)}")

                    when (baseResponse?.status) {
                        200 -> {
                            try {
                                val longToInt =baseResponse?.data?.diaryId?.toInt()
                                val intent = Intent(this@MainDiaryWritepageContent, MainDiaryWritepageGetImage::class.java)
                                intent.putExtra("numberPost", "$longToInt")
                                intent.putExtra("url","${baseResponse?.data?.imageUrl}")
                                intent.putExtra("hashTags","${baseResponse?.data?.hashTags}")

                                startActivity(intent)
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

            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainDiarywritepageContentBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //여기 인스턴스  전역으로 만들고
        binding.mainDiarywritepageContentBtn.setOnClickListener {
            val title = binding.writeTitle.text.toString()
            val content = binding.writeContent.text.toString()
            val location = binding.mainWriteLocation.text.toString()

            var writeRequestForm  = WriteDataRequest(title, content, location, "good")
            Log.d("ITM", "$writeRequestForm")

            sendDiaryToGetImage(writeRequestForm)
        }

    }


    // 수정 필요
    private fun requestLocation() {
        Log.d("ITM","위치권한요청")
        if (Build.VERSION.SDK_INT >= 29) {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report.let {

                            if (report!!.areAllPermissionsGranted()) {
                                //여기를 못들어옴
                                Toast.makeText(
                                    this@MainDiaryWritepageContent,
                                    "Permission Granted",
                                    Toast.LENGTH_SHORT
                                ).show()
                                getLocation()
                            }//여쪽으로 못들어오는것 같은데
                            else {
                                Toast.makeText(
                                    this@MainDiaryWritepageContent,
                                    "onePermission notGranted",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        showRotationalDialogForPermission()

                    }
                }
                ).onSameThread().check()

        }else {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report.let {
                            //카메라 허락 받으면 카메라 킨다
                            //처음 허락 받았을때말고 다음에는 허락 없이가나 그럼?
                            //암튼 버튼 눌렀을떄 어떤 기능이 지금 실행이 안여기
                            if (report!!.areAllPermissionsGranted()) {
                                //여기를 못들어옴
                                Toast.makeText(
                                    this@MainDiaryWritepageContent,
                                    "Permission Granted",
                                    Toast.LENGTH_SHORT
                                ).show()
                                getLocation()
                            }//여쪽으로 못들어오는것 같은데
                            else {
                                Toast.makeText(
                                    this@MainDiaryWritepageContent,
                                    "onePermission notGranted",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        showRotationalDialogForPermission()

                    }
                }
                ).onSameThread().check()

        }
    }

    @SuppressLint("MissingPermission")//경고 무시해~~
    private fun getLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { success: Location? ->
                success?.let { location ->
                    val apikey = getString(R.string.kaKaoApi)

                    Log.d("ITM","$apikey")

                    Log.d("ITM","${location.longitude}, ${location.latitude}")
                    //x, y 좌표까지 나옴
                    kakaoAPI.getAddressFromCoordinates(apikey, location.longitude, location.latitude, this)
//                    textView.text = kakaoAPI.location

                }
            }
            .addOnFailureListener { fail ->
                Log.d("ITM","${fail.localizedMessage}")
            }


    }

    override fun onSuccess(addressName: String) {

        binding.mainWriteLocation.text = addressName

    }

    override fun onError(error: Throwable) {

        binding.mainWriteLocation.text = error.toString()
    }





    //거절했는데 다시 누르면 설정가서 바꾸라
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