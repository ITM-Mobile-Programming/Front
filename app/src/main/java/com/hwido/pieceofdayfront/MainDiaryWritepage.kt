package com.hwido.pieceofdayfront

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageBinding

class MainDiaryWritepage : AppCompatActivity() {
    private lateinit var binding: MainDiarywritepageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_diarywritepage)

        binding = MainDiarywritepageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.mainDiarywritepageWriteDiary.setOnClickListener {
            var intent = Intent(this, MainDiaryWritepageBase::class.java)
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
}