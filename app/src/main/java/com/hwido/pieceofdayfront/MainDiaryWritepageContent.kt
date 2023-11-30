package com.hwido.pieceofdayfront

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.gms.location.LocationServices
import com.google.gson.JsonSyntaxException
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageContentBinding
import com.hwido.pieceofdayfront.datamodel.BaseResponse2
import com.hwido.pieceofdayfront.datamodel.DiarySendSuccessResponse
import com.hwido.pieceofdayfront.datamodel.WriteDataRequest
import com.hwido.pieceofdayfront.login.LoginMainpage
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json

class MainDiaryWritepageContent : AppCompatActivity() {

    private lateinit var binding : MainDiarywritepageContentBinding

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

    //사진
    private val REQUEST_LOCATION = 1
    @RequiresApi(Build.VERSION_CODES.Q)
    private val permissionsLocationUpApi29Impl = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )
    @TargetApi(Build.VERSION_CODES.P)
    private val permissionsLocationDownApi29Impl = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )


    override fun onStart() {
        super.onStart()
        requestLocation() // 위치 권한 요청
        Log.d("ITM","위치권한요청")
        //타이밍 중요
        getLocation(binding.mainWriteLocation)
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

        Log.d("ITM","$accessToken")
        //프로그래스 바 추가

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
    /** 위치정보 권한 요청, 수정 필요, 허락안했을 때**/

    private fun requestLocation() {
        if (Build.VERSION.SDK_INT >= 29) {
            if (ActivityCompat.checkSelfPermission(
                    this, permissionsLocationUpApi29Impl[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                    this, permissionsLocationUpApi29Impl[1]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    this, permissionsLocationUpApi29Impl[2]) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, permissionsLocationUpApi29Impl, REQUEST_LOCATION)
                showRotationalDialogForPermission()
            }
        }else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permissionsLocationDownApi29Impl[0]
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                    this,
                    permissionsLocationDownApi29Impl[1]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this as Activity,
                    permissionsLocationDownApi29Impl,
                    REQUEST_LOCATION
                )
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLocation(textView: TextView) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { success: Location? ->
                success?.let { location ->
                    val address = getAddress(location.latitude, location.longitude)?.get(0)
                    textView.text =
                        address?.let {
                            "${it.adminArea} ${it.locality} ${it.thoroughfare}"
                        }


                }
            }
            .addOnFailureListener { fail ->
                textView.text = fail.localizedMessage
            }
    }


    private fun getAddress(lat: Double, lng: Double): List<Address>? {
        lateinit var address: List<Address>

        return try {
            val geocoder = Geocoder(this, Locale.KOREA)
            address = geocoder.getFromLocation(lat, lng, 1) as List<Address>
            address
        } catch (e: IOException) {
            Toast.makeText(this, "주소를 가져 올 수 없습니다", Toast.LENGTH_SHORT).show()
            null
        }
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