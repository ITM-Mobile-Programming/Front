package com.hwido.pieceofdayfront

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.hwido.pieceofdayfront.databinding.MainMypageBinding

class MainMypage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 버튼 클릭 통한 페이지 변경
        binding.mainMypageSharepageBtn.setOnClickListener {
            val intent = Intent(this, MainMypageSharelist::class.java)
            startActivity(intent)
        }

        binding.mainMypageMbtipageBtn.setOnClickListener {
            val intent = Intent(this, MainMypageMBTI::class.java)
            startActivity(intent)
        }

        // 하단 버튼 통한 페이지 변경
        val mainBtnMain = findViewById<Button>(R.id.main_button_diaryList)
        val mainBtnShare = findViewById<Button>(R.id.main_button_diaryShare)
        val mainBtnWrite = findViewById<Button>(R.id.main_button_diaryWrite)

        mainBtnMain.setOnClickListener {
            val intent = Intent(baseContext, MainMainpage::class.java)
            startActivity(intent)
        }

        mainBtnShare.setOnClickListener {
            val intent = Intent(baseContext, MainDiarySharepage::class.java)
            startActivity(intent)
        }

        mainBtnWrite.setOnClickListener {
            val intent = Intent(baseContext, MainDiaryWritepage::class.java)
            startActivity(intent)
        }
    }
}