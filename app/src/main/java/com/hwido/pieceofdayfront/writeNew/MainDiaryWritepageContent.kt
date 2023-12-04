package com.hwido.pieceofdayfront.writeNew

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.hwido.pieceofdayfront.R
import com.hwido.pieceofdayfront.ServerAPI.ServerResponseCallback
import com.hwido.pieceofdayfront.ServerAPI.SpringServerAPI
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageContentBinding
import com.hwido.pieceofdayfront.datamodel.DiaryEntry
import com.hwido.pieceofdayfront.datamodel.WriteDataRequest
import com.hwido.pieceofdayfront.login.LoginMainpage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainDiaryWritepageContent : AppCompatActivity(), KakaoResponseCallback, WeatherCallback,
    ServerResponseCallback {

    private lateinit var binding : MainDiarywritepageContentBinding
    private val kakaoAPI = KakaoRetrofitClient()
    private val weatherAPI = WriteNewPageRetrofitClient()
    private val conConverter = CoordinateTransformer()
    private val  SpringServerCall= SpringServerAPI()

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

    @RequiresApi(Build.VERSION_CODES.Q)
    private val permissions29 = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
//        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    private val permissions28 = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // Handle the permissions result
            val allPermissionsGranted = permissions.entries.all { it.value }
            if (allPermissionsGranted) {
                // All permissions are granted
                getLocation()
            } else {
                showRotationalDialogForPermission()
            }
        }


    //back 해도 돌아가게 해야됨
    override fun onResume() {
        super.onResume()
        requestLocation()
    }

    override fun onSuccessSpring(diaryId: Int, hashTags: String, imageUrl: String) {
        val intent = Intent(this@MainDiaryWritepageContent, MainDiaryWritepageGetImage::class.java)
        intent.putExtra("numberPost", "$diaryId")
        intent.putExtra("url","$hashTags")
        intent.putExtra("hashTags","$imageUrl")


        hideProgressBar()
        startActivity(intent)
    }

    override fun onSuccessSpring(ouPutData: String) {
        //방치
    }

    override fun onErrorSpring(error: Throwable) {
        Log.d("ITM","Content 가져올 수 없음 ")
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainDiarywritepageContentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val accessToken = sharedPreferences.getString(LoginMainpage.app_JWT_token, "access").toString()
        //여기 인스턴스  전역으로 만들고
        binding.mainDiarywritepageContentBtn.setOnClickListener {
            val title = binding.writeTitle.text.toString()
            val content = binding.writeContent.text.toString()
            val location = binding.mainWriteLocation.text.toString()
            val weather = binding.weatherText.text.toString()

            //수정필요
            var writeRequestForm  = WriteDataRequest(title, content, location, weather)
//            Log.d("ITM", "$writeRequestForm")

            SpringServerCall.sendDiaryToGetImage(writeRequestForm , accessToken, this)

            showProgressBar()
        }


    }


    // 수정 필요
    private fun requestLocation() {
        Log.d("ITM","위치권한요청")
        if (Build.VERSION.SDK_INT >= 29) {
            // PermissionSupport.java 클래스 객체 생성
            requestMultiplePermissions.launch(
                permissions29
            )
        }else {
            requestMultiplePermissions.launch(
                permissions28
            )


        }
    }




    @SuppressLint("MissingPermission")//경고 무시해~~
    private fun getLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { success: Location? ->
                success?.let { location ->
                    val kaKaoApikey = getString(R.string.kaKaoApi)
                    val weatherAPIKey = getString(R.string.weatherAPI)
                    // SimpleDateFormat을 사용하여 현재 날짜와 시간 형식 지정
                    val sdf = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())

                    // 현재 날짜와 시간 가져오기
                    val currentDateTime = sdf.format(Date())
                    var currentDate = currentDateTime.substring(0,8)
                    var currentTime = currentDateTime.substring(8,12)
                    //12부분 수정 필요 30이 안넘으면 -1하자  31부터는 그대로

                    val minute = currentTime.substring(2,4)
                    var hour = currentTime.substring(0,2).toInt()
                    if(minute.toInt()<31){
                        when (hour){
                            0 ->{
                                hour = 23
                            }
                            else -> {
                                hour -= 1
                            }
                        }
                        //만약 hour 이 10보다 작으면 앞에 0 추가
                        //만약 hour이 1보다 크면 그냥 유지
                        if(hour < 10){
                            currentTime = "0$hour$minute"
                        }else{
                            currentTime = "$hour$minute"
                        }

                    }
                    // 로그에 출력 또는 화면에 표시
                    Log.d("ITM", "날짜 :${currentDate}")
                    Log.d("ITM", "시간  :${currentTime}")

                    Log.d("ITM","$kaKaoApikey")

                    Log.d("ITM","${location.longitude}, ${location.latitude}")
                    kakaoAPI.getAddressFromCoordinates(kaKaoApikey, location.longitude, location.latitude, this)



                    val (latToGrid,longToGrid) = conConverter.convertLatLonToXY(location.latitude, location.longitude)
//                    apikey:String, baseDate :String, baseTime:String, latitude: Double, longitude: Double, callback: ResponseCallback
                    Log.d("ITM", " 경도 위도 ${latToGrid}, ${longToGrid}")
                    weatherAPI.getWeather(weatherAPIKey, currentDate, currentTime, latToGrid.toShort(), longToGrid.toShort(), this)
                }
            }
            .addOnFailureListener { fail ->
                Log.d("ITM","${fail.localizedMessage}")
            }


    }

    override fun onSuccessSpringDiaryList(diaryList: List<DiaryEntry>) {
    }

    //위치 날씨 콜백함수 구현
    override fun onSuccessLocation(ouPutData: String) {
        binding.mainWriteLocation.text = ouPutData
    }

    override fun onErrorLocation(error: Throwable) {
        binding.mainWriteLocation.text = error.toString()
    }


    override fun onSuccessWeather(weatherCategory: String) {
        binding.weatherText.text = weatherCategory
    }

    override fun onErrorWeather(weatherError: Throwable) {
        binding.weatherText.text = weatherError.toString()
    }
    ///


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

    // 프로그레스바 보이기

    private fun showProgressBar() {
        val pBar = binding.mainDiarywritepageContentProgressBar
        blockLayoutTouch()
        pBar.isVisible = true
    }

    // 프로그레스바 숨기기
    private fun hideProgressBar() {
        val pBar = binding.mainDiarywritepageContentProgressBar
        clearBlockLayoutTouch()
        pBar.isVisible = false
    }

    // 화면 터치 막기
    private fun blockLayoutTouch() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    // 화면 터치 풀기
    private fun clearBlockLayoutTouch() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


}