package com.hwido.pieceofdayfront

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageContentBinding
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageGetimageBinding
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageMbtiBinding

class MainDiaryWritepageMBTI : AppCompatActivity() {
    private lateinit var binding : MainDiarywritepageMbtiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainDiarywritepageMbtiBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // 엄청난 MBTI 검사의 향연
        // 내부 로직을 통해 만든 후에  서버로 MBTI만 보내면된다
        binding.mainDiarywritepageMBTISaveBtn.setOnClickListener {

            val intent = Intent(this, MainDiaryWritepage::class.java)
            startActivity(intent)

        }
    }
}