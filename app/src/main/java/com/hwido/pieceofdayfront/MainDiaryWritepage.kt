package com.hwido.pieceofdayfront

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
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
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageBinding
import com.hwido.pieceofdayfront.myPage.MainMypage
import java.io.IOException
import java.util.Locale
import kotlin.properties.Delegates

class MainDiaryWritepage : AppCompatActivity() {
    private lateinit var binding: MainDiarywritepageBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_diarywritepage)

        binding = MainDiarywritepageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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