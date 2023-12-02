package com.hwido.pieceofdayfront.writeNew

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.bumptech.glide.Glide
import com.hwido.pieceofdayfront.ServerResponseCallback
import com.hwido.pieceofdayfront.SpringServerAPI
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageGetimageBinding
import com.hwido.pieceofdayfront.datamodel.DiaryEntry
import com.hwido.pieceofdayfront.datamodel.reloadDairy
import com.hwido.pieceofdayfront.login.LoginMainpage
import kotlin.properties.Delegates

class MainDiaryWritepageGetImage : AppCompatActivity(), ServerResponseCallback{
    private lateinit var binding : MainDiarywritepageGetimageBinding
    private var retryCount by Delegates.notNull<Int>()
    private val springServer = SpringServerAPI()

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


    override fun onResume() {
        super.onResume()
        retryCount = 0
        Log.d("ITM","횟수 초기화")
    }

    override fun onSuccessSpring(ouPutData: String) {
        Glide.with(this)
                    .load(ouPutData)
                    .fitCenter()
                    .into(binding.mainDiarywritepageSecondShowImage)
    }

    override fun onSuccessSpring(diaryId: Int, hashTags: String, imageUrl: String) {
        // 방치
    }

    override fun onErrorSpring(error: Throwable) {

    }

    override fun onSuccessSpringDiaryList(diaryList: List<DiaryEntry>) {
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainDiarywritepageGetimageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //이제 글라인드 import 하고
        //3번 비동기 요청 count
        val diaryId = intent.getStringExtra("numberPost")?.toInt()!!
        val imageUrl = intent.getStringExtra("url")
        val hashTags = intent.getStringExtra("hashTags")

        Glide.with(this)
            .load(imageUrl)
            .fitCenter()
            .into(binding.mainDiarywritepageSecondShowImage)

        binding.mainDiarywritepageGetImageShowHashtag.text = hashTags

        val accessToken = sharedPreferences.getString(LoginMainpage.app_JWT_token, "access").toString()
        val diaryForm = reloadDairy(diaryId)
        binding.writeImageReload.setOnClickListener {
//            Log.d("ITM","intent 데이터 추출 $diaryId, $imageUrl , $hashTags")
            if (retryCount >= 3){
                Toast.makeText(this, "사용만료 입니다", Toast.LENGTH_SHORT).show()

            }else{
                //api  호출
                springServer.reloadDiaryToGetImage(diaryForm,accessToken,this)
            }
            retryCount+=1
        }



        //완료가 됬으면 다음 엑티비티로 넘어간다
        binding.mainDiarywritepageSecondfragmentBtn.setOnClickListener {
            val intent = Intent(this, MainDiaryWritepageMBTI::class.java)
            //intent로 다이어 숫자 넘겨야된다
            intent.putExtra("numberPost", "$diaryId")
            startActivity(intent)
        }

    }


}