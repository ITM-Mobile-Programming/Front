package com.hwido.pieceofdayfront.writeNew

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageGetimageBinding
import kotlin.properties.Delegates

class MainDiaryWritepageGetImage : AppCompatActivity() {
    private lateinit var binding : MainDiarywritepageGetimageBinding
    private var retryCount by Delegates.notNull<Int>()

    override fun onResume() {
        super.onResume()
        retryCount = 0
        Log.d("ITM","횟수 초기화")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainDiarywritepageGetimageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //이제 글라인드 import 하고
        //3번 비동기 요청 count
        val diaryId = intent.getStringExtra("numberPost")
        val imageUrl = intent.getStringExtra("url")
        val hashTags = intent.getStringExtra("hashTags")

        Glide.with(this)
            .load(imageUrl)
            .centerCrop()
            .into(binding.mainDiarywritepageSecondShowImage);


        binding.writeImageReload.setOnClickListener {
            Log.d("ITM","intent 데이터 추출 $diaryId, $imageUrl , $hashTags")
            if (retryCount < 3){
                Toast.makeText(this, "사용만료 입니다", Toast.LENGTH_SHORT).show()
            }else{
                retryCount+=1
                //api 다시 호출

            }

        }

        //완료가 됬으면 다음 엑티비티로 넘어간다
        binding.mainDiarywritepageSecondfragmentBtn.setOnClickListener {

            val intent = Intent(this, MainDiaryWritepageMBTI::class.java)
            startActivity(intent)
        }

    }


}