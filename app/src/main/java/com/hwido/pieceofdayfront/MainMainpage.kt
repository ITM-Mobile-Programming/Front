package com.hwido.pieceofdayfront

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hwido.pieceofdayfront.databinding.MainMainpageBinding
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.hwido.pieceofdayfront.login.LoginMainpage
import com.hwido.pieceofdayfront.myPage.MainMypageFragment

class MainMainpage : AppCompatActivity() {


    private lateinit var binding: MainMainpageBinding
    private lateinit var secondFragment : Fragment

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


    // 누르면 여기로 바로 이졷
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.getStringExtra("FRAGMENT_NAME") == "MainWriteFragment") {
            displaySecondFragment()
        }
    }

    private fun displaySecondFragment() {
        // SecondFragment를 표시하는 코드
        supportFragmentManager.beginTransaction()
            .replace(binding.mainMainpageBaseframe.id, secondFragment)
            .commit()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainMainpageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val accessToken = sharedPreferences.getString(LoginMainpage.app_JWT_token, "access").toString()

        Log.d("ITM"," $accessToken")
        val firstFragment = MainListpageFragment.newInstance(accessToken)
        secondFragment = MainDiaryWritepageFragment.newInstance(accessToken)
        val thridFragment = MainDiarySharepageFragment.newInstance(accessToken)
        val fourthFragment = MainMypageFragment.newInstance(accessToken)

        val fManager = supportFragmentManager
        fManager.commit {
            setReorderingAllowed(true)
            addToBackStack(null)
            add(binding.mainMainpageBaseframe.id, firstFragment)
        }


        // 하단 버튼 통한 페이지 변경
        binding.mainButtonDiaryList.setOnClickListener {
            fManager.commit{
                replace(binding.mainMainpageBaseframe.id,firstFragment)
            }
        }


        // 여기 부분 넘겨주면 Fragment에서
        binding.mainButtonDiaryWrite.setOnClickListener {
            fManager.commit{
//                getDiaryList()
                replace(binding.mainMainpageBaseframe.id,secondFragment)
            }
        }

        binding.mainButtonDiaryShare.setOnClickListener {

            fManager.commit{
                replace(binding.mainMainpageBaseframe.id,thridFragment)
            }
        }

        binding.mainButtonMyPage.setOnClickListener {
            fManager.commit{
                replace(binding.mainMainpageBaseframe.id,fourthFragment)
            }
        }


    }




}