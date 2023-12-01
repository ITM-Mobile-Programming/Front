package com.hwido.pieceofdayfront

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.hwido.pieceofdayfront.databinding.MainMainpageBinding
import com.hwido.pieceofdayfront.myPage.MainMypage
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.SharedPreferences
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.gms.location.LocationServices
import com.hwido.pieceofdayfront.login.LoginMainpage
import kotlin.properties.Delegates

class MainMainpage : AppCompatActivity() {


    private lateinit var binding: MainMainpageBinding

    // sharepreference에  서버 토큰 저장하는것은 맞다
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
        binding = MainMainpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 객체 생성
        val dayText: TextView = binding.mainMainpageDay
        val calendarView: CalendarView = binding.mainMainpageCalendar

        // 날짜 형태
        val dateFormat: DateFormat = SimpleDateFormat("yyyy년 MM월 dd일")

        // date 타입
        val date = Date(calendarView.date)

        // 현재 날짜
        dayText.text = dateFormat.format(date)
        Log.d("mainpage","${dayText.text}")

        // 날짜 변환
        calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->

            // 날짜 변수에 담기
            val day  = "${year}년 ${month+1}월 ${dayOfMonth}일"

            // 변수를 텍스트뷰에 담아준다
            dayText.text = day
        }

        // 하단 버튼 통한 페이지 변경
        binding.mainButtonDiaryWrite.setOnClickListener {

            val intent = Intent(baseContext, MainDiaryWritepage::class.java)
            startActivity(intent)
        }

        binding.mainButtonDiaryShare.setOnClickListener {
            val intent = Intent(baseContext, MainDiarySharepage::class.java)
            startActivity(intent)
        }

        binding.mainButtonMyPage.setOnClickListener {
            val intent = Intent(baseContext, MainMypage::class.java)
            startActivity(intent)
        }


    }




}