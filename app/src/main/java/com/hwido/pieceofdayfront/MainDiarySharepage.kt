package com.hwido.pieceofdayfront

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainDiarySharepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_diarysharepage)

        // 하단 버튼 통한 페이지 변경
        val mainBtnMain = findViewById<Button>(R.id.main_button_diaryList)
        val mainBtnMyPage = findViewById<Button>(R.id.main_button_myPage)
        val mainBtnWrite = findViewById<Button>(R.id.main_button_diaryWrite)

        mainBtnMain.setOnClickListener {
            val intent = Intent(baseContext, MainMainpage::class.java)
            startActivity(intent)
        }

        mainBtnMyPage.setOnClickListener {
            val intent = Intent(baseContext, MainMypage::class.java)
            startActivity(intent)
        }

        mainBtnWrite.setOnClickListener {
            val intent = Intent(baseContext, MainDiaryWritepage::class.java)
            startActivity(intent)
        }
    }
}