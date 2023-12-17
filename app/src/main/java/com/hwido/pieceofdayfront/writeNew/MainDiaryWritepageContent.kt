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
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.hwido.pieceofdayfront.BluetoothClient.BluetoothClientActivity
import com.hwido.pieceofdayfront.BluetoothServer.BluetoothServerActivity
import com.hwido.pieceofdayfront.R
import com.hwido.pieceofdayfront.ServerAPI.SpringServerAPI
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageContentBinding
import com.hwido.pieceofdayfront.DT.WriteDataRequest
import com.hwido.pieceofdayfront.DT.WriteDataRequestTransfer
import com.hwido.pieceofdayfront.login.LoginMainpage
import com.hwido.pieceofdayfront.writeNew.Location.CoordinateTransformer
import com.hwido.pieceofdayfront.writeNew.Location.KakaoResponseCallback
import com.hwido.pieceofdayfront.writeNew.Location.KakaoRetrofitClient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainDiaryWritepageContent : AppCompatActivity(), KakaoResponseCallback, WeatherCallback{

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainDiarywritepageContentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val accessToken =
            sharedPreferences.getString(LoginMainpage.app_JWT_token, "access").toString()




        //여기 인스턴스  전역으로 만들고
        binding.mainDiarywritepageContentBtn.setOnClickListener {
            val title = binding.writeTitle.text.toString()
            val content = binding.writeContent.text.toString()
            val location = binding.mainWriteLocation.text.toString()
            val weather = binding.weatherText.text.toString()


//            springServer.getDiaryList(it1,  onSuccess = { diaryList ->
//                // 성공 시 실행될 코드
//                Log.d("ITM", "리스트 콜백 ${diaryList.reversed()}")
//                diaryAdapter.updateData(diaryList.reversed())
//            }, onFailure = {
//                // 실패 시 실행될 코드
//                Toast.makeText(activity, "NONO", Toast.LENGTH_SHORT).show()
//            })

//            onSuccess(longToInt, url, hashTag, resultCode)

            //처음 다이어리 등록할때 yes  no에 따라서 이미지를 띄울지 아니면 다른 멤버한테 보낼지 정해야됨


            AlertDialog.Builder(this)
                .setMessage(
                    "Do you want Share?"
                )
                .setPositiveButton("Share") { dialog, _ ->

                    var writeRequestForm = WriteDataRequest(title, content, location, weather)
                    SpringServerCall.sendDiaryToGetImage(writeRequestForm, accessToken,  onSuccess =
                    { longToInt, url,  hashTag->
                        // 성공 시 실행될 코드

                        SpringServerCall.getMyPage(accessToken, onSuccess = { mycode ->
                            // 성공 시 실행될 코드
                            //데이터 클래스에 넣어 둔다
                            val transferData =
                                WriteDataRequestTransfer(longToInt, mycode, title, content, location, weather)
                            val intent = Intent(this, BluetoothClientActivity::class.java)

                            //데이터 클래스로 보낸
                            intent.putExtra("codeAndContent", transferData)
                            Log.d("ITMM","${transferData.toString()}")
                            //위치
                            hideProgressBar()
                            startActivity(intent)

                        }, onFailure = {
                            // 실패 시 실행될 코드
                            Toast.makeText(this, "NONO", Toast.LENGTH_SHORT).show()
                        })


                    }, onFailure = {
                        // 실패 시 실행될 코드
                        Toast.makeText(this, "NONO", Toast.LENGTH_SHORT).show()
                    })


                    showProgressBar()
                    dialog.dismiss()
                }
                //_-> 이게 뭐야
                .setNegativeButton("Just for me") { dialog, _ ->

                    var writeRequestForm = WriteDataRequest(title, content, location, weather)
                    SpringServerCall.sendDiaryToGetImage(writeRequestForm, accessToken, onSuccess =
                    { longToInt, url,  hashTag ->
                        // 성공 시 실행될 코드

                        val intent = Intent(this@MainDiaryWritepageContent, MainDiaryWritepageGetImage::class.java)
                        intent.putExtra("numberPost", "$longToInt")
                        intent.putExtra("url","$url")
                        intent.putExtra("hashTags","$hashTag")

                        hideProgressBar()
                        startActivity(intent)


                    }, onFailure = {
                        // 실패 시 실행될 코드
                        Toast.makeText(this, "NONO", Toast.LENGTH_SHORT).show()
                    })


                    dialog.dismiss()
                    showProgressBar()
                }.show()


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